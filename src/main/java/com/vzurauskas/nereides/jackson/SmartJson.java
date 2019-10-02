package com.vzurauskas.nereides.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * A smart JSON. It can represent itself in other data types such as,
 * byte arrays, {@link String}s, {@link InputStream}, and so forth. It can also
 * give its nested JSONs and leaves, tell if it is missing and do other useful
 * things. To use it, you need to wrap another {@link Json} in it, e.g.
 * <pre>
 * {@code
 * Json original = ...;
 * SmartJson smart = new SmartJson(original);
 * String textual = smart.textual();
 * InputStream stream = smart.inputStream();
 * SmartJson nested = smart.at("/path/to/nested/json");
 * if (!nested.isMissing()) {
 *     String value = nested.leaf("fieldName");
 * }
 * }
 * </pre>
 */
public final class SmartJson implements Json {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final byte[] NULL_BYTES = {
        (byte) 110, (byte) 117, (byte) 108, (byte) 108
    };

    private final Json origin;
    private final Unchecked<ObjectNode> jackson;

    /**
     * Constructor.
     * @param origin Original JSON as basis to this {@code SmartJson}.
     */
    public SmartJson(Json origin) {
        this(
            origin,
            new Unchecked<>(
                () -> MAPPER.readValue(origin.bytes(), ObjectNode.class)
            )
        );
    }

    private SmartJson(Json origin, Unchecked<ObjectNode> jackson) {
        this.origin = origin;
        this.jackson = jackson;
    }

    @Override
    public byte[] bytes() {
        return origin.bytes();
    }

    /**
     * Represent this JSON in textual form.
     * @return String representing this JSON in textual form.
     */
    public String textual() {
        return new Unchecked<>(
            () -> MAPPER.writeValueAsString(jackson.value())
        ).value();
    }

    /**
     * Represent this JSON in pretty format textual form.
     * @return String representing this JSON in pretty format textual form.
     */
    public String pretty() {
        return new Unchecked<>(
            () -> MAPPER.writerWithDefaultPrettyPrinter()
                .writeValueAsString(jackson.value())
        ).value();
    }

    /**
     * Represent this JSON in {@link InputStream}.
     * @return {@link InputStream} representing this JSON.
     */
    public InputStream inputStream() {
        return new ByteArrayInputStream(bytes());
    }

    /**
     * Method to get a {@code String} type field of this JSON.
     * @param name Name of the field to return.
     * @return Value of the field.
     */
    public String leaf(String name) {
        return jackson.value().get(name).textValue();
    }

    /**
     * Method to get an {@code int} type field of this JSON.
     * @param name Name of the field to return.
     * @return Value of the field.
     */
    public int leafAsInt(String name) {
        return jackson.value().get(name).intValue();
    }

    /**
     * Method to get a {@code double} type field of this JSON.
     * @param name Name of the field to return.
     * @return Value of the field.
     */
    public double leafAsDouble(String name) {
        return jackson.value().get(name).doubleValue();
    }

    /**
     * Method to get a {@code boolean} type field of this JSON.
     * @param name Name of the field to return.
     * @return Value of the field.
     */
    public boolean leafAsBool(String name) {
        return jackson.value().get(name).booleanValue();
    }

    /**
     * Represent this JSON as {@link ObjectNode} in case full JSON manipulation
     * capabilities offered by jackson-databind library are needed.
     * @return This JSON as {@link ObjectNode}.
     */
    public ObjectNode objectNode() {
        return jackson.value();
    }

    /**
     * Method to get a JSON nested within this JSON, specified by path. Path
     * starts with a forward slash, and path elements are separated by forward
     * slashes also, e.g.
     * <pre>
     * {@code
     * SmartJson nested = json.at("/path/to/nested/json");}
     * </pre>
     * This method never returns null. If there is no JSON as specified by the
     * path, a missing JSON is returned, i.e.
     * {@code returned.isMissing() == true}.
     * @param path Path to the nested JSON.
     * @return The nested JSON, which could be missing.
     */
    public SmartJson at(String path) {
        return new SmartJson(
            new Of(jackson.value().at(path))
        );
    }

    /**\
     * Method which tells if this JSON is missing.
      * @return true if this JSON is missing; otherwise false.
     */
    public boolean isMissing() {
        final byte[] bytes = bytes();
        return bytes.length == 0 || Arrays.equals(bytes, NULL_BYTES);
    }
}
