package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

final class SmartJsonLeafTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // String

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

    // Int

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

    // Double

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

    // Boolean

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
}
