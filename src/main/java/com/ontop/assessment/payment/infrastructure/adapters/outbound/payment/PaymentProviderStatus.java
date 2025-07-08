package com.ontop.assessment.payment.infrastructure.adapters.outbound.payment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum PaymentProviderStatus {

    PROCESSING("Processing"),
    FAILED("Failed"),
    COMPLETED("Completed")
    ;

    private final String status;

    PaymentProviderStatus(String status) {
        this.status = status;
    }

    @JsonCreator
    public static PaymentProviderStatus fromString(String status) {
        return Stream.of(PaymentProviderStatus.values())
                .filter(value -> value.getStatus().equals(status))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @JsonValue
    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
