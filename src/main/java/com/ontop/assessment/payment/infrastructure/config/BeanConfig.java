package com.ontop.assessment.payment.infrastructure.config;

import com.ontop.assessment.payment.domain.adapter.service.PaymentServiceImpl;
import com.ontop.assessment.payment.domain.port.PaymentProviderPort;
import com.ontop.assessment.payment.domain.port.PaymentRepositoryPort;
import com.ontop.assessment.payment.domain.port.PaymentServicePort;
import com.ontop.assessment.payment.domain.port.WalletServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    PaymentServicePort paymentService(PaymentRepositoryPort paymentRepositoryPort,
                                      PaymentProviderPort paymentProviderPort,
                                      WalletServicePort walletServicePort,
                                      PaymentsConfig paymentsConfig
                                      ){
        return new PaymentServiceImpl(paymentRepositoryPort, paymentProviderPort,
                walletServicePort, paymentsConfig);
    }
}
