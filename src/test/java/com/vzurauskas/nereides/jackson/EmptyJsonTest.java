package com.vzurauskas.nereides.jackson;

import org.junit.jupiter.api.Test;

final class EmptyJsonTest {
    @Test
    void creates() {
        new EqualityAssertion(
            new Json.Of("{}"),
            new EmptyJson()
        ).affirm();
    }
}
