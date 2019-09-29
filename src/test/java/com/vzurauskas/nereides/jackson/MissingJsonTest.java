package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

final class MissingJsonTest {
    @Test
    void creates() {
        assertEquals(
            0,
            new MissingJson().bytes().length
        );
    }
}
