package com.vzurauskas.nereides.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

public interface Json {
    byte[] bytes();

    public final class Of implements Json {
        private static final ObjectMapper MAPPER = new ObjectMapper();
        private final Json origin;

        public Of(JsonNode node) {
            this(
                projection(
                    () -> {
                        try {
                            return MAPPER.writeValueAsBytes(node);
                        } catch (JsonProcessingException e) {
                            throw new UncheckedIOException(e);
                        }
                    }
                )
            );
        }

        public Of(String string) {
            this(string::getBytes);
        }

        public Of(InputStream stream) {
            this(() -> {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                try {
                    byte[] data = new byte[1024];
                    while (true) {
                        int size = stream.read(data, 0, data.length);
                        if (size == -1) {
                            break;
                        }
                        output.write(data, 0, size);
                    }
                    output.flush();
                    return output.toByteArray();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }

        public Of(byte[] bytes) {
            this.origin = () -> bytes;
        }

        private Of(Json json) {
            this.origin = json;
        }

        @Override
        public byte[] bytes() {
            return origin.bytes();
        }

        private static <T> T projection(Supplier<T> scalar) {
            return scalar.get();
        }
    }

    public final class Smart implements Json {
        private static final ObjectMapper MAPPER = new ObjectMapper();
        private final Json origin;

        public Smart(Json origin) {
            this.origin = origin;
        }

        @Override
        public byte[] bytes() {
            return origin.bytes();
        }

        public String textual() {
            try {
                return MAPPER.writeValueAsString(
                    MAPPER.readValue(this.bytes(), ObjectNode.class)
                );
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
