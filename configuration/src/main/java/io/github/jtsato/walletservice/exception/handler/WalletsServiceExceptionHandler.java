package io.github.jtsato.walletservice.exception.handler;

import io.github.jtsato.walletservice.core.exception.InvalidActionException;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.entrypoint.rest.common.HttpResponseStatus;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Jorge Takeshi Sato
 */

@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class WalletsServiceExceptionHandler {

    private final MessageSource messageSource;

    private final WebRequest webRequest;

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public HttpResponseStatus handleNotFoundException(final NotFoundException exception, final Locale locale) {
        final String message = messageSource.getMessage(exception.getMessage(), exception.getArgs(), locale);
        return buildHttpResponseStatus(HttpStatus.NOT_FOUND, message, webRequest.getPath());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidActionException.class)
    public HttpResponseStatus handleInvalidActionException(final InvalidActionException exception, final Locale locale) {
        final String message = messageSource.getMessage(exception.getMessage(), exception.getArgs(), locale);
        return buildHttpResponseStatus(HttpStatus.BAD_REQUEST, message, webRequest.getPath());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public HttpResponseStatus handleConstraintViolationException(final ConstraintViolationException exception, final Locale locale) {
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        final Collector<CharSequence, ?, String> joining = Collectors.joining(", ");
        final String message = violations.stream().map(violation -> messageSource.getMessage(violation.getMessage(), null, locale)).collect(joining);
        return buildHttpResponseStatus(HttpStatus.BAD_REQUEST, message, webRequest.getPath());
    }

    public static HttpResponseStatus buildHttpResponseStatus(final HttpStatus httpStatus, final String message, final String path) {
        return new HttpResponseStatus(LocalDateTime.now(), httpStatus.value(), httpStatus.getReasonPhrase(), message, path);
    }
}
