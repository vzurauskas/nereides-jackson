package com.vzurauskas.nereides.jackson;

public final class MissingJson implements Json {

    private static final byte[] MISSING = new byte[0];

    @Override
    public byte[] bytes() {
        return MISSING;
    }
}
