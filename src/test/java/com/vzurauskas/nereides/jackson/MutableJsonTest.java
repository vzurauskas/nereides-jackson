package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

final class MutableJsonTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void createsOneField() {
        new EqualityAssertion(
            new Json.Of(
                MAPPER.createObjectNode()
                    .put("field1", "value1")
            ),
            new MutableJson()
                .with("field1", "value1")
        ).affirm();
    }

    @Test
    void createsOneAndThenAnotherField() {
        MutableJson json = new MutableJson().with("field1", "value1");
        json.bytes();
        json.with("field2", 9.9);
        new EqualityAssertion(
            new Json.Of(
                MAPPER.createObjectNode()
                    .put("field1", "value1")
                    .put("field2", 9.9)
            ),
            json
        ).affirm();
    }

    @Test
    void createsDeepJson() throws URISyntaxException {
        assertEquals(
            new SmartJson(
                new Json.Of(
                    Paths.get(
                        MutableJsonTest.class.getClassLoader().getResource(
                            "deep-noarray.json"
                        ).toURI()
                    )
                )
            ).pretty(),
            new SmartJson(
                new MutableJson().with(
                    "ocean",
                    new MutableJson().with(
                        "rock1",
                        new MutableJson().with(
                            "nereid1",
                            new MutableJson()
                                .with("hair", "black")
                                .with("age", 100)
                        ).with(
                            "nereid2",
                            new MutableJson()
                                .with("hair", "red")
                                .with("age", 77.5)
                        )).with(
                        "rock2",
                        new MutableJson().with(
                            "nereid3",
                            new MutableJson()
                                .with("hair", "blonde")
                                .with("age", 88)
                                .with("fair", true)
                        )
                    )
                )
            ).pretty()
        );
    }

    @Test
    void buildsOnBase() {
        new EqualityAssertion(
            new MutableJson().with(
                "ocean",
                new MutableJson()
                    .with("character", "stormy")
            ).with("nereid", new EmptyJson()),
            new MutableJson(
                new MutableJson().with(
                    "ocean",
                    new MutableJson()
                        .with("character", "stormy")
                )
            ).with("nereid", new EmptyJson())
        ).affirm();
    }

    @Test
    void toStringOnEmpty() {
        assertEquals(
            "{}",
            new MutableJson().toString()
        );
    }
}
