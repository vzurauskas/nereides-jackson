package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

final class JsonSmartTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Path deep;

    JsonSmartTest() throws URISyntaxException {
        this.deep = Paths.get(
            JsonSmartTest.class.getClassLoader()
                .getResource("deep.json").toURI()
        );
    }

    @Test
    void givesBytes() {
        byte[] bytes = "{\"field1\":\"value1\",\"field2\":\"value2\"}"
            .getBytes();
        assertArrayEquals(
            bytes,
            new Json.Smart(
                new Json.Of(bytes)
            ).bytes()
        );
    }

    @Test
    void convertsToString() {
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

    @Test
    void convertsToPrettyString() {
        assertEquals(
            '{' + System.lineSeparator()
                + "  \"field1\" : \"value1\"," + System.lineSeparator()
                + "  \"field2\" : \"value2\"" + System.lineSeparator()
                + '}',
            new Json.Smart(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .put("field2", "value2")
                )
            ).pretty()
        );
    }

    @Test
    void convertsToObjectNode() {
        ObjectNode node = MAPPER.createObjectNode()
            .put("field1", "value1")
            .put("field2", "value2");
        assertEquals(
            node,
            new Json.Smart(
                new Json.Of(node)
            ).objectNode()
        );
    }

    @Test
    void findsLeaf() {
        assertEquals(
            "value2",
            new Json.Smart(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .put("field2", "value2")
                )
            ).leaf("field2")
        );
    }

    @Test
    void findsLeafAsInt() {
        assertEquals(
            14,
            new Json.Smart(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .put("field2", 14)
                )
            ).leafAsInt("field2")
        );
    }

    @Test
    void findsLeafAsDouble() {
        assertEquals(
            14.0,
            new Json.Smart(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", 14.0)
                        .put("field2", "value2")
                )
            ).leafAsDouble("field1")
        );
    }

    @Test
    void findsLeafAsBool() {
        assertTrue(
            new Json.Smart(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .put("field2", true)
                )
            ).leafAsBool("field2")
        );
    }

    @Test
    void findsPath() {
        assertEquals(
            "red",
            new Json.Smart(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid2").leaf("hair")
        );
    }
}
