package com.vzurauskas.nereides.jackson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * {@link Json}, which is missing. It is equivalent to
 * {@code new Json.Of(new byte[0])}.
 */
public final class MissingJson implements Json {

    private static final byte[] MISSING = new byte[0];

    @Override
    public InputStream bytes() {
        return new ByteArrayInputStream(MISSING);
    }
}
