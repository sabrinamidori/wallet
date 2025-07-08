package com.ontop.assessment.payment.infrastructure.adapters.outbound.wallet;

import com.ontop.assessment.payment.infrastructure.config.exception.ErrorType;
import lombok.Getter;

@Getter
public enum WalletError implements ErrorType {
    GENERIC_ERROR("GENERIC_ERROR"),
    INVALID_USER("INVALID_USER"),
    INVALID_BODY("INVALID_BODY")
    ;
    private final String code;

    WalletError(String code) {
        this.code = code;
    }
}
