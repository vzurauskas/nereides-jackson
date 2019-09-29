package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

final class EmptyJsonTest {
    @Test
    void creates() {
        assertArrayEquals(
            "{}".getBytes(),
            new EmptyJson().bytes()
        );
    }
}
