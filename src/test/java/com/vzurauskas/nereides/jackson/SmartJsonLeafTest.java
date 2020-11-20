package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.Test;

final class SmartJsonLeafTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // String, immediate

    @Test
    void findsOptLeaf() {
        assertEquals(
            "value2",
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .put("field2", "value2")
                )
            ).optLeaf("field2").get()
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
    void returnsEmptyOptionalForNonexistentLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).optLeaf("nonexistent").isPresent()
        );
    }

    @Test
    void throwsForNonexistentLeaf() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                    )
                ).leaf("nonexistent")
            ).getMessage().contains("No such field")
        );
    }

    @Test
    void returnsEmptyOptionalIfLeafIsNotString() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .put("intField", 5)
                )
            ).optLeaf("intField").isPresent()
        );
    }

    @Test
    void throwsIfLeafIsNotString() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                            .put("field1", "value1")
                            .put("intField", 5)
                    )
                ).leaf("intField")
            ).getMessage().contains("No such field")
        );
    }

    // String, path

    @Test
    void findsOptLeafInPath() {
        assertEquals(
            "innerValue",
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", "innerValue")
                        )
                )
            ).optLeaf("/field2/innerField").get()
        );
    }

    @Test
    void findsLeafInPath() {
        assertEquals(
            "innerValue",
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", "innerValue")
                        )
                )
            ).leaf("/field2/innerField")
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentLeafInPath() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).optLeaf("/nonexistent/path").isPresent()
        );
    }

    @Test
    void throwsForNonexistentLeafInPath() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                    )
                ).leaf("/nonexistent/path")
            ).getMessage().contains("No such field")
        );
    }

    @Test
    void returnsEmptyOptionalIfLeafInPathIsNotString() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", 7)
                        )
                )
            ).optLeaf("/field2/innerField").isPresent()
        );
    }

    @Test
    void throwsIfLeafInPathIsNotString() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                            .put("field1", "value1")
                            .<ObjectNode>set(
                                "field2",
                                MAPPER.createObjectNode()
                                    .put("innerField", true)
                            )
                    )
                ).leaf("/field2/innerField")
            ).getMessage().contains("No such field")
        );
    }

    // Int, immediate, normal

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

    // Int, immediate, autoconversions

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

    // Int, path, normal

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

    // Int, path, autoconversions

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

    // Double, immediate, normal

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

    // Double, immediate, autoconversions

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

    // Double, path, normal

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

    // Double, path, autoconversions

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

    // Boolean, immediate, normal

    @Test
    void findsOptLeafAsBool() {
        assertTrue(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .put("field2", true)
                )
            ).optLeafAsBool("field2").get()
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
    void returnsEmptyOptionalForNonexistentBooleanLeaf() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).optLeafAsBool("nonexistent").isPresent()
        );
    }

    @Test
    void throwsForNonexistentBooleanLeaf() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                    )
                ).leafAsBool("nonexistent")
            ).getMessage().contains("No such field")
        );
    }

    // Boolean, immediate, autoconversions

    @Test
    void returnsFalseIfOptLeafIsNotBool() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("boolField", true)
                )
            ).optLeafAsBool("stringField").get()
        );
    }

    @Test
    void returnsFalseIfLeafIsNotBool() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("stringField", "value1")
                        .put("boolField", true)
                )
            ).leafAsBool("stringField")
        );
    }

    // Boolean, path, normal

    @Test
    void findsOptBoolLeafInPath() {
        assertTrue(
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
            ).optLeafAsBool("/field2/innerField").get()
        );
    }

    @Test
    void findsLeafBoolInPath() {
        assertTrue(
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
            ).leafAsBool("/field2/innerField")
        );
    }

    @Test
    void returnsEmptyOptionalForNonexistentLeafBoolInPath() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                )
            ).optLeafAsBool("/nonexistent/path").isPresent()
        );
    }

    @Test
    void throwsForNonexistentLeafBoolInPath() {
        assertTrue(
            assertThrows(
                IllegalArgumentException.class,
                () -> new SmartJson(
                    new Json.Of(
                        MAPPER.createObjectNode()
                    )
                ).leafAsBool("/nonexistent/path")
            ).getMessage().contains("No such field")
        );
    }

    // Double, path, autoconversions

    @Test
    void returnsFalseIfOptLeafInPathIsNotBool() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", "string")
                        )
                )
            ).optLeafAsBool("/field2/innerField").get()
        );
    }

    @Test
    void returnsFalseIfLeafInPathIsNotBool() {
        assertFalse(
            new SmartJson(
                new Json.Of(
                    MAPPER.createObjectNode()
                        .put("field1", "value1")
                        .<ObjectNode>set(
                            "field2",
                            MAPPER.createObjectNode()
                                .put("innerField", 17)
                        )
                )
            ).leafAsBool("/field2/innerField")
        );
    }
}
