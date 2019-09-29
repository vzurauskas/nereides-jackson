package com.vzurauskas.nereides.jackson;

public final class EmptyJson implements Json {
    @Override
    public byte[] bytes() {
        return "{}".getBytes();
    }
}
