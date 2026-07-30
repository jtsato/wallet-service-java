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

    @DisplayName("Should mask values in diagnostic JSON")
    @Test
    void shouldMaskValuesInDiagnosticJson() {
        final String json = JsonConverter.maskedOf(new SensitivePayload("alice@example.com", "100.00"));

        assertThat(json).contains("\"userId\":\"a***m\"")
                .contains("\"amount\":\"1***0\"")
                .doesNotContain("alice@example.com");
    }

    @Test
    void shouldMaskEmailWhileKeepingDomain() {
        assertThat(JsonConverter.maskEmail("alice@example.com")).isEqualTo("a***e@example.com");
    }

    @DisplayName("Should return empty string on JsonProcessingException")
    @Test
    void shouldReturnEmptyStringOnJsonProcessingException() {
        final String json = JsonConverter.of(new BadSerializable());

        assertThat(json).isEmpty();
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

    static final class SensitivePayload implements Serializable {
        private final String userId;
        private final String amount;

        SensitivePayload(final String userId, final String amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public String getUserId() {
            return userId;
        }

        public String getAmount() {
            return amount;
        }
    }
}
