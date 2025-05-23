package io.github.jtsato.walletservice.core.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Jorge Takeshi Sato
 */

public class LocalDateTimeValidator implements ConstraintValidator<LocalDateTimeConstraint, String> {

    @Override
    public boolean isValid(final String candidate, final ConstraintValidatorContext constraintContext) {
        return candidate != null ? parseLocalDateTime(candidate) : Boolean.TRUE;
    }

    private boolean parseLocalDateTime(final String candidate) {
        try {
            LocalDateTime.parse(candidate, DateTimeFormatter.ISO_DATE_TIME);
            return true;
        } catch (final Exception exception) {
            return false;
        }
    }
}
