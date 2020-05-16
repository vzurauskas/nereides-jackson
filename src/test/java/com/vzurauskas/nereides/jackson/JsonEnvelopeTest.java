package com.vzurauskas.nereides.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

final class JsonEnvelopeTest {

    @Test
    void smoke() {
        new EqualityAssertion(
            new Json.Of("{\"number\": 12}"),
            new TestJsonEnvelope(new Json.Of("{\"number\": 12}"))
        ).affirm();
    }

    @Test
    void toStringWorksWhenMalformed() {
        assertEquals(
            "malformed",
            new TestJsonEnvelope(new Json.Of("malformed")).toString()
        );
    }

    private static final class TestJsonEnvelope extends JsonEnvelope {
        TestJsonEnvelope(Json origin) {
            super(origin);
        }
    }
}
