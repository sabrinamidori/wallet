package com.ontop.assessment.payment.domain.payment;

import com.ontop.assessment.payment.infrastructure.config.exception.ErrorType;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PaymentStatus implements ErrorType {

    PROCESSING("PROCESSING", "Processing"),
    FAILED("FAILED", "Failed"),
    COMPLETED("COMPLETED", "Completed")
    ;

    private final String code;
    private final String status;

    PaymentStatus(String code, String status) {
        this.code = code;
        this.status = status;
    }

    public static PaymentStatus fromString(String status) {
        return Stream.of(PaymentStatus.values())
                .filter(value -> value.getStatus().equals(status))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public String toString() {
        return this.code;
    }

}
