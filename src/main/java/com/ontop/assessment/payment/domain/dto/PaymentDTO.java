package com.ontop.assessment.payment.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.domain.payment.PaymentStatus;
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
public class PaymentDTO {
    private Long id;
    private Long userId;
    private BankAccountDTO destination;

    private Long withdrawId;
    private BigDecimal amount;
    private BigDecimal feeAmount;
    private PaymentStatus status;
    private String externalId;

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.userId = payment.getUserId();
        this.destination = new BankAccountDTO(payment.getDestination());
        this.withdrawId = payment.getWithdrawId();
        this.amount = payment.getAmount();
        this.feeAmount = payment.getFeeAmount();
        this.status = payment.getStatus();
        this.externalId = payment.getExternalId();

    }
}
