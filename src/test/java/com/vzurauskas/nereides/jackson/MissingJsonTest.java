package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.junit.jupiter.api.Test;

final class MissingJsonTest {
    @Test
    void creates() throws IOException {
        assertEquals(
            0,
            new MissingJson().bytes().available()
        );
    }
}
