package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

final class MutableJsonTest {

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
}
