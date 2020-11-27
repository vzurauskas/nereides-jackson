package com.vzurauskas.nereides.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

/**
 * A smart JSON. It can represent itself in other data types such as,
 * byte arrays, {@link String}s, {@link InputStream}s, and so forth. It can also
 * give its nested JSONs and leaves, tell if it is missing and do other useful
 * things. To use it, you need to wrap another {@link Json} in it, e.g.
 * <pre>
 * {@code
 * Json original = new Json.Of(...);
 * SmartJson smart = new SmartJson(original);
 * String textual = smart.textual();
 * InputStream stream = smart.inputStream();
 * SmartJson nested = smart.at("/path/to/nested/json");
 * if (!nested.isMissing()) {
 *     Optional<String> value = nested.leaf("fieldName");
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
     * Represent this JSON in an array of bytes.
     * @return Byte array representing this JSON.
     */
    public byte[] byteArray() {
        return new ByteArray(bytes()).value();
    }

    /**
     * Method to get a {@code String} type  leaf (primitive field) of this JSON.
     * @param path JSON path to the leaf or the name of the leaf if it is
     * directly at the root of this JSON.
     * @return Optional value of the leaf.
     */
    public Optional<String> optLeaf(String path) {
        return nodeAt(path).map(JsonNode::textValue);
    }

    /**
     * Method to get a {@code String} type leaf (primitive field) of this JSON.
     * @param path JSON path to the leaf or the name of the leaf if it is
     * directly at the root of this JSON.
     * @return String value of the field, if the leaf exists.
     * @throws IllegalArgumentException if leaf does not exist.
     */
    public String leaf(String path) {
        return optLeaf(path).orElseThrow(
            () -> new IllegalArgumentException(
                "No such field of specified type: " + path
            )
        );
    }

    /**
     * Method to get an {@code int} type  leaf (primitive field) of this JSON.
     * @param path JSON path to the leaf or the name of the leaf if it is
     * directly at the root of this JSON.
     * @return Optional value of the leaf.
     */
    public Optional<Integer> optLeafAsInt(String path) {
        return nodeAt(path).map(JsonNode::intValue);
    }

    /**
     * Method to get an {@code int} type  leaf (primitive field) of this JSON.
     * @param path JSON path to the leaf or the name of the leaf if it is
     * directly at the root of this JSON.
     * @return Int value of the leaf.
     * @throws IllegalArgumentException if leaf does not exist.
     */
    public int leafAsInt(String path) {
        return optLeafAsInt(path).orElseThrow(
            () -> new IllegalArgumentException(
                "No such field of specified type: " + path
            )
        );
    }

    /**
     * Method to get a {@code double} type  leaf (primitive field) of this JSON.
     * @param path JSON path to the leaf or the name of the leaf if it is
     * directly at the root of this JSON.
     * @return Optional value of the leaf.
     */
    public Optional<Double> optLeafAsDouble(String path) {
        return nodeAt(path).map(JsonNode::doubleValue);
    }

    /**
     * Method to get an {@code double} type leaf (primitive field) of this JSON.
     * @param path JSON path to the leaf or the name of the leaf if it is
     * directly at the root of this JSON.
     * @return Double value of the leaf.
     * @throws IllegalArgumentException if leaf does not exist.
     */
    public double leafAsDouble(String path) {
        return optLeafAsDouble(path).orElseThrow(
            () -> new IllegalArgumentException(
                "No such field of specified type: " + path
            )
        );
    }

    /**
     * Method to get a {@code boolean} type  leaf (primitive field) of this
     * JSON.
     * @param path JSON path to the leaf or the name of the leaf if it is
     * directly at the root of this JSON.
     * @return Optional value of the leaf.
     */
    public Optional<Boolean> optLeafAsBool(String path) {
        return nodeAt(path).map(JsonNode::booleanValue);
    }

    /**
     * Method to get an {@code int} type field  leaf (primitive field) of this
     * JSON.
     * @param path JSON path to the leaf or the name of the leaf if it is
     * directly at the root of this JSON.
     * @return Boolean value of the leaf.
     * @throws IllegalArgumentException if field does not exist.
     */
    public boolean leafAsBool(String path) {
        return optLeafAsBool(path).orElseThrow(
            () -> new IllegalArgumentException(
                "No such field of specified type: " + path
            )
        );
    }

    private Optional<JsonNode> nodeAt(String path) {
        JsonNode node = !path.isEmpty() && path.charAt(0) == '/'
            ? jackson.value().at(path)
            : jackson.value().path(path);
        return node.isMissingNode()
            ? Optional.empty()
            : Optional.of(node);
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

    /**
     * Method which tells if this JSON is missing.
      * @return true if this JSON is missing; otherwise false.
     */
    public boolean isMissing() {
        final byte[] bytes = byteArray();
        return bytes.length == 0 || Arrays.equals(bytes, NULL_BYTES);
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
