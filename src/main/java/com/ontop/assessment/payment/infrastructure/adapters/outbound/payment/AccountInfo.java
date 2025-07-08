package com.ontop.assessment.payment.infrastructure.adapters.outbound.payment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class AccountInfo {
    private String accountNumber;
    private String currency;
    private String routingNumber;
}
