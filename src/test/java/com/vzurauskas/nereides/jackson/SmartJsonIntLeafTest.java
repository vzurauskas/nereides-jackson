package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

final class SmartJsonIntLeafTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Immediate, normal

    @Test
    void findsOptLeafAsInt() {
        assertEquals(
            14,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .put("field2", 14)
                )
            ).optLeafAsInt("field2").get().intValue()
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
    void returnsEmptyOptionalForNonexistentIntLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).optLeafAsInt("nonexistent").isPresent()
        );
    }

    @Test
    void throwsForNonexistentIntLeaf() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                    )
                ).leafAsInt("nonexistent")
            ).getMessage().contains("No such field")
        );
    }

    // Immediate, autoconversion

    @Test
    void returnsZeroIfOptLeafIsNotInt() {
        assertEquals(
            0,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("intField", 5)
                )
            ).optLeafAsInt("stringField").get().intValue()
        );
    }

    @Test
    void returnsZeroIfLeafIsNotInt() {
        assertEquals(
            0,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("intField", 5)
                )
            ).leafAsInt("stringField")
        );
    }

    @Test
    void returnsIntEvenIfOptLeafIsDouble() {
        assertEquals(
            5,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("doubleField", 5.9)
                )
            ).optLeafAsInt("doubleField").get().intValue()
        );
    }

    @Test
    void returnsIntEvenIfLeafIsDouble() {
        assertEquals(
            5,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("doubleField", 5.9)
                )
            ).leafAsInt("doubleField")
        );
    }

    // Path, normal

    @Test
    void findsOptIntLeafInPath() {
        assertEquals(
            999,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", 999)
                        )
                )
            ).optLeafAsInt("/field2/innerField").get().intValue()
        );
    }

    @Test
    void findsLeafIntInPath() {
        assertEquals(
            12,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", 12)
                        )
                )
            ).leafAsInt("/field2/innerField")
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentLeafIntInPath() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).optLeafAsInt("/nonexistent/path").isPresent()
        );
    }

    @Test
    void throwsForNonexistentLeafIntInPath() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                    )
                ).leafAsInt("/nonexistent/path")
            ).getMessage().contains("No such field")
        );
    }

    // Path, autoconversion

    @Test
    void returnsZeroIfOptLeafInPathIsNotInt() {
        assertEquals(
            0,
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
            ).optLeafAsInt("/field2/innerField").get().intValue()
        );
    }

    @Test
    void returnsZeroIfLeafInPathIsNotInt() {
        assertEquals(
            0,
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
            ).leafAsInt("/field2/innerField")
        );
    }

    @Test
    void returnsIntEvenIfOptLeafInPathIsDouble() {
        assertEquals(
            5,
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", 5.9)
                        )
                )
            ).optLeafAsInt("/field2/innerField").get().intValue()
        );
    }
}
