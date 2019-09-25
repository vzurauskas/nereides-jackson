package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

final class JsonSmartTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void convertsToObjectNode() {
        assertEquals(
            "{\"field1\":\"value1\","
                + "\"field2\":\"value2\"}",
            new Json.Smart(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .put("field2", "value2")
                )
            ).textual()
        );
    }
}
