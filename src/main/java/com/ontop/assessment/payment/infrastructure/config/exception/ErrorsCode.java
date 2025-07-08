package com.ontop.assessment.payment.infrastructure.config.exception;

import lombok.Getter;
@Getter
public enum ErrorsCode implements ErrorType  {
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND")      ,
    EXTERNAL_PROVIDER_ERROR("EXTERNAL_PROVIDER_ERROR") ,
    INVALID_BODY("INVALID_BODY")           ,
    INVALID_OPERATION("INVALID_OPERATION")       ,
    NOT_ENOUGH_BALANCE("NOT_ENOUGH_BALANCE")      ,
    INVALID_AMOUNT("INVALID_AMOUNT")
    ;
    private final String code;

    ErrorsCode(String code) {
        this.code = code;
    }
}
