package com.ontop.assessment.payment.infrastructure.config.exception;

public record ErrorDetails (
    String code,
    String message,
    String detail
){}
