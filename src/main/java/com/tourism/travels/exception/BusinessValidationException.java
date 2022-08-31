package com.tourism.travels.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }

}
