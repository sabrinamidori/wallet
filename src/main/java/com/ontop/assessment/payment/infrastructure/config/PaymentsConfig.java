package com.ontop.assessment.payment.infrastructure.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "payment")
public class PaymentsConfig {
    private String withdrawFee;
    private int minAmount;
    private PaymentSourceConfig source;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentSourceConfig {
        private String type;
        private String sourceInformationName;
        private String accountNumber;
        private String accountCurrency;
        private String accountRoutingNumber;
    }
}
