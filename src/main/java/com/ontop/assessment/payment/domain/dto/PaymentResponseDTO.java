package com.ontop.assessment.payment.domain.dto;

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
public class PaymentResponseDTO {
    private String paymentId;
    private BigDecimal paymentAmount;
    private PaymentStatus status;
    private String error;
}
