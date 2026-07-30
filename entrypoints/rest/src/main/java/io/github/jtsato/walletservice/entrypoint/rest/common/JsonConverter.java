package io.github.jtsato.walletservice.entrypoint.rest.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author Jorge Takeshi Sato
 */

@Generated
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonConverter {

    private static final Logger log = LoggerFactory.getLogger(JsonConverter.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String of(final Serializable serializable) {

        try {
            return OBJECT_MAPPER.writeValueAsString(serializable);
        } catch (final JsonProcessingException jsonProcessingException) {
            log.warn("Failed to write value as JSON", jsonProcessingException);
            return StringUtils.EMPTY;
        }
    }

    /** Serializes request data with values masked for safe diagnostic logging. */
    public static String maskedOf(final Serializable serializable) {
        try {
            final JsonNode root = OBJECT_MAPPER.valueToTree(serializable);
            maskNode(root);
            return OBJECT_MAPPER.writeValueAsString(root);
        } catch (final IllegalArgumentException | JsonProcessingException exception) {
            log.warn("Failed to write masked value as JSON", exception);
            return StringUtils.EMPTY;
        }
    }

    public static String maskEmail(final String email) {
        if (StringUtils.isBlank(email)) {
            return StringUtils.EMPTY;
        }
        final int at = email.indexOf('@');
        if (at <= 0) {
            return maskValue(email);
        }
        return maskValue(email.substring(0, at)) + email.substring(at);
    }

    private static void maskNode(final JsonNode node) {
        if (node instanceof ObjectNode objectNode) {
            objectNode.fieldNames().forEachRemaining(fieldName -> {
                final JsonNode value = objectNode.get(fieldName);
                if (value.isValueNode()) {
                    objectNode.put(fieldName, maskValue(value.asText()));
                } else {
                    maskNode(value);
                }
            });
        } else if (node.isArray()) {
            node.forEach(JsonConverter::maskNode);
        }
    }

    private static String maskValue(final String value) {
        if (value.length() <= 2) {
            return "**";
        }
        return value.charAt(0) + "***" + value.charAt(value.length() - 1);
    }
}
