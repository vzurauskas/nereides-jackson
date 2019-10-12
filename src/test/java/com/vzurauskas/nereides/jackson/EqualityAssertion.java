package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class EqualityAssertion {
    private final Json first;
    private final Json second;

    public EqualityAssertion(Json first, Json second) {
        this.first = first;
        this.second = second;
    }

    public void affirm() {
        assertEquals(
            new SmartJson(first).pretty(),
            new SmartJson(second).pretty()
        );
    }
}
