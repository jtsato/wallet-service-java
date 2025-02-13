package io.github.jtsato.walletservice.entrypoint.rest.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author Jorge Takeshi Sato
 */

@Generated
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonConverter {

    public static String of(final Serializable serializable) {

        try {
            return new ObjectMapper().writeValueAsString(serializable);
        } catch (final JsonProcessingException jsonProcessingException) {
            return StringUtils.EMPTY;
        }
    }
}
