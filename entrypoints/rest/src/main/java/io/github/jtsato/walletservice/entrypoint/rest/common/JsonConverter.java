package io.github.jtsato.walletservice.entrypoint.rest.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
}
