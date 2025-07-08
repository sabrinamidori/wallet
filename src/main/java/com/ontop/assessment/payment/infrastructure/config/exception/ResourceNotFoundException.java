package com.ontop.assessment.payment.infrastructure.config.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final transient ErrorType code;
    public ResourceNotFoundException(final String message) {
        super(message);
        this.code = ErrorsCode.RESOURCE_NOT_FOUND;
    }
    public ResourceNotFoundException(final ErrorType code, final String message) {
        super(message);
        this.code = code;
    }
}