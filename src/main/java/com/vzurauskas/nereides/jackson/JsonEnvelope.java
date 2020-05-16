package com.vzurauskas.nereides.jackson;

import java.io.InputStream;

public abstract class JsonEnvelope implements Json {
    private final Json origin;

    protected JsonEnvelope(Json origin) {
        this.origin = origin;
    }

    @Override
    public final String toString() {
        return new String(new ByteArray(origin).value());
    }

    @Override
    public final InputStream bytes() {
        return origin.bytes();
    }
}
