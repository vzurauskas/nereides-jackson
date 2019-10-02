package com.vzurauskas.nereides.jackson;

/**
 * {@link Json}, which is missing. It is equivalent to
 * {@code new Json.Of(new byte[0])}.
 */
public final class MissingJson implements Json {

    private static final byte[] MISSING = new byte[0];

    @Override
    public byte[] bytes() {
        return MISSING;
    }
}
