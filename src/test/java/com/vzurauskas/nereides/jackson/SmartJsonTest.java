package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
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
    void convertsToInputStream() throws JsonProcessingException {
        final byte[] bytes = MAPPER.writeValueAsBytes(
            MAPPER.createObjectNode()
                .put("field1", "value1")
                .put("field2", "value2")
        );
        assertEquals(
            new BufferedReader(
                new InputStreamReader(
                    new ByteArrayInputStream(bytes)
                )
            ).lines().collect(Collectors.toList()),
            new BufferedReader(
                new InputStreamReader(
                    new SmartJson(new Json.Of(bytes)).inputStream()
                )
            ).lines().collect(Collectors.toList())
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
            ).leaf("field2").get()
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).leaf("nonexistent").isPresent()
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
            ).leafAsInt("field2").get().intValue()
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentIntLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).leafAsInt("nonexistent").isPresent()
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
            ).leafAsDouble("field1").get().doubleValue()
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentDoubleLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).leafAsDouble("nonexistent").isPresent()
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
            ).leafAsBool("field2").get()
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentBooleanLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).leafAsBool("nonexistent").isPresent()
        );
    }

    @Test
    void findsPath() {
        assertEquals(
            "red",
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid2").leaf("hair").get()
        );
    }

    @Test
    void handlesNonExistentPaths() {
        assertTrue(
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/nothing").isMissing()
        );
    }

    @Test
    void findsPathInArray() {
        assertEquals(
            "Jason",
            new SmartJson(
                new Json.Of(deep)
            ).at("/ocean/rock1/nereid1/associates/0").leaf("name").get()
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
            ).at("/ocean/rock1/nereid1/associates").at("/0").leaf("name").get()
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
