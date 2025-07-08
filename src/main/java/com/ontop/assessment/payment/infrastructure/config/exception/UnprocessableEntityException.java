package com.ontop.assessment.payment.infrastructure.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2021-10-22
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
@Getter
public class UnprocessableEntityException extends RuntimeException {

    private final transient ErrorType code;
    public UnprocessableEntityException(String message) {
        super(message);
        this.code = ErrorsCode.EXTERNAL_PROVIDER_ERROR;
    }
    public UnprocessableEntityException(final ErrorType code, final String message) {
        super(message);
        this.code = code;
    }
}
