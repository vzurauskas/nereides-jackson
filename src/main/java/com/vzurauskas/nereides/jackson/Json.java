package com.vzurauskas.nereides.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
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
     * @return {@link InputStream} with bytes representing this {@code Json}.
     */
    InputStream bytes();

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
    final class Of implements Json {
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
                flattened(
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

        private static <T> T flattened(Supplier<T> scalar) {
            return scalar.get();
        }

        /**
         * Constructor.
         * @param string JSON represented by a {@link String}.
         */
        public Of(String string) {
            this(string.getBytes());
        }

        /**
         * Constructor.
         * @param bytes JSON represented by an array of bytes.
         */
        public Of(byte[] bytes) {
            this(new ByteArrayInputStream(bytes));
        }

        /**
         * Constructor.
         * @param stream JSON represented by the bytes in an
         * {@link InputStream}.
         */
        public Of(InputStream stream) {
            this.origin = () -> stream;
        }

        /**
         * Constructor.
         * @param path Path to a JSON in a file.
         */
        public Of(Path path) {
            this(
                () -> new Unchecked<>(
                    () -> new ByteArrayInputStream(Files.readAllBytes(path))
                ).value()
            );
        }

        private Of(Json json) {
            this.origin = json;
        }

        @Override
        public InputStream bytes() {
            return origin.bytes();
        }

        @Override
        public String toString() {
            return new String(new ByteArray(this).value());
        }
    }
}
