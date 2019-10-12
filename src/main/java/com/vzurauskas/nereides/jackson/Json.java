package com.vzurauskas.nereides.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * JSON document. This is the type to be implemented by all objects which
 * represent JSONs.
 *
 * In addition to writing custom {@code Json} objects, various data types
 * can be represented as {@code Json} by instantiating a {@link Json.Of} object.
 */
public interface Json {

    /**
     * Tell this {@code Json} to represent itself as bytes.
     * @return Bytes representing this {@code Json}.
     */
    byte[] bytes();

    /**
     * {@link Json}, constructed from JSON represented by other data types
     * such as byte array, {@code String}, {@code InputStream} and so forth.
     * E.g.
     * <pre>
     * {@code
     * String jsonAsString = ...;
     * Json json = new Json.Of(jsonAsString);
     * }
     * </pre>
     */
    public final class Of implements Json {
        private static final ObjectMapper MAPPER = new ObjectMapper();
        private final Json origin;

        /**
         * Constructor.
         * @param node JSON represented by {@link JsonNode} from
         * 'jackson-databind' library.
         */
        public Of(JsonNode node) {
            this(() -> node);
        }

        /**
         * Constructor.
         * @param node JSON represented by {@link JsonNode} from
         * 'jackson-databind' library.
         */
        public Of(Supplier<JsonNode> node) {
            this(
                projection(
                    () -> {
                        try {
                            return MAPPER.writeValueAsBytes(node.get());
                        } catch (JsonProcessingException e) {
                            throw new UncheckedIOException(e);
                        }
                    }
                )
            );
        }

        /**
         * Constructor.
         * @param string JSON represented by a {@link String}.
         */
        public Of(String string) {
            this((Json) string::getBytes);
        }

        /**
         * Constructor.
         * @param stream JSON represented by an {@link InputStream}.
         */
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

        /**
         * Constructor.
         * @param bytes JSON represented by a byte array.
         */
        public Of(byte[] bytes) {
            this.origin = () -> bytes;
        }

        /**
         * Constructor.
         * @param path Path to a JSON in a file.
         */
        public Of(Path path) {
            this(
                () -> new Unchecked<>(() -> Files.readAllBytes(path)).value()
            );
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

}
