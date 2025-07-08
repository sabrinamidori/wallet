package com.ontop.assessment.payment.infrastructure.adapters.outbound.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProviderResponse {
    private RequestInfo requestInfo;
    private PaymentInfo paymentInfo;
}
