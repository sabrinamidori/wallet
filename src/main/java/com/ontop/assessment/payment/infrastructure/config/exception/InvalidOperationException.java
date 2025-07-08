package com.ontop.assessment.payment.infrastructure.config.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class InvalidOperationException extends RuntimeException implements Serializable {
    private final transient ErrorType code;
    public InvalidOperationException(final String message) {
        super(message);
        this.code = ErrorsCode.INVALID_OPERATION;
    }
    public InvalidOperationException(final ErrorType code, final String message) {
        super(message);
        this.code = code;
    }
}