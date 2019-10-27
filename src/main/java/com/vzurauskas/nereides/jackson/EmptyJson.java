package com.vzurauskas.nereides.jackson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Empty {@link Json}. It is equivalent to {@code new Json.Of("{}")}.
 */
public final class EmptyJson implements Json {
    @Override
    public InputStream bytes() {
        return new ByteArrayInputStream("{}".getBytes());
    }
}
