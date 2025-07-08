package com.ontop.assessment.payment.infrastructure.adapters.outbound.wallet;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionResponse {
    private final long walletTransactionId;
    private final long userId;
    private final String amount;
}
