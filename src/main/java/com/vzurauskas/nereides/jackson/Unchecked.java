package com.vzurauskas.nereides.jackson;

import java.io.IOException;
import java.io.UncheckedIOException;

final class Unchecked<T> {
    private final Checked<T> checked;

    Unchecked(Checked<T> checked) {
        this.checked = checked;
    }

    // @checkstyle IllegalCatch (30 lines)
    T value() {
        try {
            return checked.value();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
