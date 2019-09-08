package com.vzurauskas.nereides.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

public interface Json {
    byte[] bytes();

    public final class Of implements Json {
        private static final ObjectMapper mapper = new ObjectMapper();
        private final Json origin;

        public Of(JsonNode node) {
            this(
                projection(
                    () -> {
                        try {
                            return mapper.writeValueAsBytes(node);
                        } catch (JsonProcessingException e) {
                            throw new UncheckedIOException(e);
                        }
                    }
                )
            );
        }

        public Of(byte[] bytes) {
            this.origin = () -> bytes;
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
        private static final ObjectMapper mapper = new ObjectMapper();
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
                return mapper.writeValueAsString(
                    mapper.readValue(this.bytes(), ObjectNode.class)
                );
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}