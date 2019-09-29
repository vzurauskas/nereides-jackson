package com.vzurauskas.nereides.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class SmartJson implements Json {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Json origin;
    private final Unchecked<ObjectNode> jackson;

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

    public String textual() {
        return new Unchecked<>(
            () -> MAPPER.writeValueAsString(jackson.value())
        ).value();
    }

    public String pretty() {
        return new Unchecked<>(
            () -> MAPPER.writerWithDefaultPrettyPrinter()
                .writeValueAsString(jackson.value())
        ).value();
    }

    public String leaf(String key) {
        return jackson.value().get(key).textValue();
    }

    public int leafAsInt(String key) {
        return jackson.value().get(key).intValue();
    }

    public double leafAsDouble(String key) {
        return jackson.value().get(key).doubleValue();
    }

    public boolean leafAsBool(String key) {
        return jackson.value().get(key).booleanValue();
    }

    public ObjectNode objectNode() {
        return jackson.value();
    }

    public SmartJson at(String path) {
        return new SmartJson(
            new Of(jackson.value().at(path))
        );
    }

    public boolean isMissing() {
        return bytes().length == 0;
    }
}
