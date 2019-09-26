package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

final class JsonOfTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void constructsFromBytes() throws JsonProcessingException {
        byte[] bytes = MAPPER.writeValueAsBytes(
            MAPPER.createObjectNode()
                .put("field1", "value1")
                .put("field2", "value2")
        );
        assertArrayEquals(
            bytes,
            new Json.Of(bytes).bytes()
        );
    }
}
