package com.vzurauskas.nereides.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;

/**
 * JSON which is mutable and can be used to build custom JSONs, e.g.
 * <pre>
 * {@code
 * new MutableJson().with(
 *     "ocean",
 *     new MutableJson().with(
 *         "nereid",
 *         new MutableJson()
 *             .with("hair", "black")
 *             .with("age", 100)
 *             .with("fair", true)
 *     )
 * )
 * }
 * </pre>
 */
public final class MutableJson implements Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ObjectNode base;

    /**
     * Constructor.
     */
    public MutableJson() {
        this(MAPPER.createObjectNode());
    }

    /**
     * Constructor.
     * @param base The base JSON to build upon.
     */
    public MutableJson(Json base) {
        this(
            (ObjectNode) new Unchecked<>(
                () -> MAPPER.readTree(base.bytes())
            ).value()
        );
    }

    private MutableJson(ObjectNode base) {
        this.base = base;
    }

    @Override
    public InputStream bytes() {
        return new Json.Of(base).bytes();
    }

    /**
     * Add a {@code String} field to this JSON.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, String value) {
        base.put(name, value);
        return this;
    }

    /**
     * Add an {@code int} field to this JSON.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, int value) {
        base.put(name, value);
        return this;
    }

    /**
     * Add a {@code double} field to this JSON.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, double value) {
        base.put(name, value);
        return this;
    }

    /**
     * Add a {@code boolean} field to this JSON.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, boolean value) {
        base.put(name, value);
        return this;
    }

    /**
     * Add a {@link Json} field to this JSON. If the added {@link Json} is a,
     * other fields can be added to it, thus enabling nesting.
     * @param name Name of the field.
     * @param value Value of the field.
     * @return This JSON.
     */
    public MutableJson with(String name, Json value) {
        base.set(name, new SmartJson(value).objectNode());
        return this;
    }
}
