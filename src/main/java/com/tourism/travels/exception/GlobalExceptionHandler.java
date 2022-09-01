package com.tourism.travels.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        var fieldErrors = e.getBindingResult().getFieldErrors();

        var errors = fieldErrors.stream()
                .map(x -> new Error(x.getField(), x.getDefaultMessage()))
                .toList();

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> alreadyExistsException(BusinessValidationException e) {

        var exceptionMessage = e.getMessage();

        var message = exceptionMessage == null ? "already exists" : exceptionMessage;

        var error = new Error(null, message);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> notFoundException(NotFoundException e) {

        var error = new Error(null, "not found");

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Error> httpMessageNotReadableException(HttpMessageNotReadableException e) {

        var cause = e.getCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {

            var targetType = invalidFormatException.getTargetType();

            if (Enum.class.isAssignableFrom(targetType)) {

                var enumConstants = (Enum[]) targetType.getEnumConstants();

                var values = Arrays.stream(enumConstants)
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));

                var message  = "Invalid value. Valid values: " + values;
                var field = invalidFormatException.getPath().get(0).getFieldName();

                var error = new Error(field, message);

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
        }

        var error = new Error(null, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Error> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        var error = new Error(null, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> exception(Exception e) {

        log.error("ERROR", e);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    record Error (
            @JsonInclude(JsonInclude.Include.NON_NULL)
            String field,
            String message) {

    }

}
