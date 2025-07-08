package com.ontop.assessment.payment.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WithdrawRequestDTO {
    @NotNull(message = "User can't be null.")
    private Long userId;
    @NotNull(message = "Destination account can't be null.")
    private Long accountId;
    @NotNull(message = "Destination currency can't be null.")
    @Size(min = 3, max = 3, message = "Invalid destination currency")
    private String currency;
    @NotNull(message = "Destination bank holder name can't be null.")
    private String holderName;
    @NotNull(message = "Destination bank account number can't be null.")
    private String accountNumber;
    @NotNull(message = "Destination bank routing number can't be null.")
    private String routingNumber;
    @NotNull(message = "Amount can't be null.")
    private BigDecimal amount;
}
