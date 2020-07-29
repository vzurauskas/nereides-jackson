package com.vzurauskas.nereides.jackson;

import java.util.function.Supplier;

final class Cached<T> {
    private final Supplier<T> scalar;
    private T value;

    Cached(Supplier<T> scalar) {
        this.scalar = scalar;
    }

    public T value() {
        if (value == null) {
            value = scalar.get();
        }
        return value;
    }
}
