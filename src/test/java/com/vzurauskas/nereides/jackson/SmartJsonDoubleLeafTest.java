package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

final class SmartJsonDoubleLeafTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Immediate, normal

    @Test
    void findsOptLeafAsDouble() {
        assertEquals(
            14.9,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", 14.9)
                        .put("field2", "value2")
                )
            ).optLeafAsDouble("field1").get().doubleValue()
        );
    }

    @Test
    void findsLeafAsDouble() {
        assertEquals(
            14.9,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", 14.9)
                        .put("field2", "value2")
                )
            ).leafAsDouble("field1")
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentDoubleLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).optLeafAsDouble("nonexistent").isPresent()
        );
    }

    @Test
    void throwsForNonexistentDoubleLeaf() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                    )
                ).leafAsDouble("nonexistent")
            ).getMessage().contains("No such field")
        );
    }

    // Immediate, autoconversions

    @Test
    void returnsZeroIfOptLeafIsNotDouble() {
        assertEquals(
            0.0,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("doubleField", 5.9)
                )
            ).optLeafAsDouble("stringField").get().doubleValue()
        );
    }

    @Test
    void returnsZeroIfLeafIsNotDouble() {
        assertEquals(
            0.0,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("doubleField", 5.9)
                )
            ).leafAsDouble("stringField")
        );
    }

    @Test
    void returnsDoubleEvenIfOptLeafIsInt() {
        assertEquals(
            5.0,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("intField", 5)
                )
            ).optLeafAsDouble("intField").get().doubleValue()
        );
    }

    @Test
    void returnsDoubleEvenIfLeafIsInt() {
        assertEquals(
            5.0,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("intField", 5)
                )
            ).leafAsDouble("intField")
        );
    }

    // Path, normal

    @Test
    void findsOptDoubleLeafInPath() {
        assertEquals(
            999.17,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", 999.17)
                        )
                )
            ).optLeafAsDouble("/field2/innerField").get().doubleValue()
        );
    }

    @Test
    void findsLeafDoubleInPath() {
        assertEquals(
            12.45,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", 12.45)
                        )
                )
            ).leafAsDouble("/field2/innerField")
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentLeafDoubleInPath() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).optLeafAsDouble("/nonexistent/path").isPresent()
        );
    }

    @Test
    void throwsForNonexistentLeafDoubleInPath() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                    )
                ).leafAsDouble("/nonexistent/path")
            ).getMessage().contains("No such field")
        );
    }

    // Path, autoconversions

    @Test
    void returnsZeroIfOptLeafInPathIsNotDouble() {
        assertEquals(
            0.0,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", true)
                        )
                )
            ).optLeafAsDouble("/field2/innerField").get().doubleValue()
        );
    }

    @Test
    void returnsZeroIfLeafInPathIsNotDouble() {
        assertEquals(
            0.0,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", true)
                        )
                )
            ).leafAsDouble("/field2/innerField")
        );
    }

    @Test
    void returnsDoubleEvenIfOptLeafInPathIsInt() {
        assertEquals(
            5.0,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", 5)
                        )
                )
            ).optLeafAsDouble("/field2/innerField").get().doubleValue()
        );
    }
}
