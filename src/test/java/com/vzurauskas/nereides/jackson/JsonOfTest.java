package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Test
    void constructsFromString() {
        String string = "{\"number\": 12}";
        assertArrayEquals(
            string.getBytes(),
            new Json.Of(string).bytes()
        );
    }

    @Test
    void constructsFromInputStream() {
        String string = "{\"number\": 12}";
        assertArrayEquals(
            string.getBytes(),
            new Json.Of(new ByteArrayInputStream(string.getBytes())).bytes()
        );
    }

    @Test
    void constructsFromFile() throws IOException, URISyntaxException {
        Path path = Paths.get(
            JsonOfTest.class.getClassLoader().getResource("deep.json").toURI()
        );
        assertArrayEquals(
            Files.readAllBytes(path),
            new Json.Of(path).bytes()
        );
    }

    @Test
    void understandsArrays() {
        String string = "[{\"name\":\"Jason\"},{\"name\":\"Thetis\"}]";
        assertArrayEquals(
            string.getBytes(),
            new Json.Of(string).bytes()
        );
    }
}
