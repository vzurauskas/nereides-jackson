package com.vzurauskas.jason.jackson;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

final class JsonSmartTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void convertsToObjectNode() {
        assertEquals(
            "{\"field1\":\"value1\","
                + "\"field2\":\"value2\"}",
            new Json.Smart(
                new Json.Of(
                    mapper.createObjectNode()
                        .put("field1", "value1")
                        .put("field2", "value2")
                )
            ).textual()
        );
    }
}