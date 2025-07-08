package com.ontop.assessment.payment.infrastructure.adapters.outbound.payment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class SourceAccount {
    private String type;
    private SourceInfo sourceInformation;
    private AccountInfo account;
}
