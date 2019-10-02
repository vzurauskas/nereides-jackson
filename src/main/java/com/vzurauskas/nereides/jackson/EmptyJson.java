package com.vzurauskas.nereides.jackson;

/**
 * Empty {@link Json}. It is equivalent to {@code new Json.Of("{}")}.
 */
public final class EmptyJson implements Json {
    @Override
    public byte[] bytes() {
        return "{}".getBytes();
    }
}
