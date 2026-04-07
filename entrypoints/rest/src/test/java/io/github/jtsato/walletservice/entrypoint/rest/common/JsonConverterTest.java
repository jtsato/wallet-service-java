package io.github.jtsato.walletservice.entrypoint.rest.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Json Converter Test")
class JsonConverterTest {

    @DisplayName("Should serialize a serializable object")
    @Test
    void shouldSerializeSerializableObject() {
        final TestPayload payload = new TestPayload("abc");

        final String json = JsonConverter.of(payload);

        assertThat(json)
                .isNotBlank()
                .contains("\"value\":\"abc\"");
    }

    @DisplayName("Should return empty string on JsonProcessingException")
    @Test
    void shouldReturnEmptyStringOnJsonProcessingException() {
        final String json = JsonConverter.of(new BadSerializable());

        assertThat(json).isEqualTo("");
    }

    static final class TestPayload implements Serializable {
        private final String value;

        TestPayload(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    static final class BadSerializable implements Serializable {
        @SuppressWarnings("unused")
        public String getValue() throws JsonProcessingException {
            throw new JsonProcessingException("boom") {
            };
        }
    }
}

