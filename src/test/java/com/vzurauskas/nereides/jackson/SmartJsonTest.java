package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

final class SmartJsonTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Path deep;

    SmartJsonTest() throws URISyntaxException {
        this.deep = Paths.get(
            SmartJsonTest.class.getClassLoader()
                .getResource("deep.json").toURI()
        );
    }

    @Test
    void givesBytes() {
        byte[] bytes = "{\"field1\":\"value1\",\"field2\":\"value2\"}"
            .getBytes();
        assertArrayEquals(
            bytes,
            new SmartJson(
                new Json.Of(bytes)
            ).bytes()
        );
    }

    @Test
    void convertsToString() {
        assertEquals(
            "{\"field1\":\"value1\","
                + "\"field2\":\"value2\"}",
            new SmartJson(
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
            new SmartJson(
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
            new SmartJson(
                new Json.Of(node)
            ).objectNode()
        );
    }

    @Test
    void findsLeaf() {
        assertEquals(
            "value2",
            new SmartJson(
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
            new SmartJson(
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
            new SmartJson(
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
            new SmartJson(
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
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid2").leaf("hair")
        );
    }

    @Test
    void findsPathInArray() {
        assertEquals(
            "Jason",
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid1/associates/0").leaf("name")
        );
    }

    @Test
    void understandsArrays() {
        String array = "[{\"name\":\"Jason\"},{\"name\":\"Thetis\"}]";
        assertArrayEquals(
            array.getBytes(),
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid1/associates").bytes()
        );
    }

    @Disabled("https://github.com/vzurauskas/nereides-jackson/issues/39")
    @Test
    void reallyUnderstandsArrays() {
        String array = "[{\"name\":\"Jason\"},{\"name\":\"Thetis\"}]";
        assertEquals(
            "Jason",
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid1/associates").at("/0").leaf("name")
        );
    }

    @Test
    void knowsIfMissing() {
        assertTrue(new SmartJson(new MissingJson()).isMissing());
    }

    @Test
    void knowsIfNotMissing() {
        assertFalse(new SmartJson(new Json.Of("{}")).isMissing());
    }
}
