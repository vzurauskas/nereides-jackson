package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

final class SmartJsonBoolLeafTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Immediate, normal

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

    // Immediate, autoconversion

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

    // Path, normal

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

    // Path, autoconversion

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
