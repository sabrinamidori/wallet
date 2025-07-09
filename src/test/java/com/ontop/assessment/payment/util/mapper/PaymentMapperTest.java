package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.domain.dto.DestinationAccountDTO;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.entity.JpaDestinationAccount;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.entity.JpaPaymentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
class PaymentMapperTest {
    @InjectMocks
    PaymentMapperImpl paymentMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDomainToEntity(){
        var payment = new Payment(100000L, BigDecimal.valueOf(1000), BigDecimal.valueOf(100),5L,
                new DestinationAccountDTO("Jon Doe", "212356456468", "USD", "9998888888"));
        var result = paymentMapper.domainToEntity(payment);
        Assertions.assertEquals(result.getUserId(), payment.getUserId());
        Assertions.assertEquals(result.getDestination().getAccountNumber(), payment.getDestination().getAccountNumber());
        Assertions.assertEquals(result.getDestination().getRoutingNumber(), payment.getDestination().getRoutingNumber());
        Assertions.assertEquals(result.getDestination().getCurrency(), payment.getDestination().getCurrency());
        Assertions.assertEquals(result.getAmount(), payment.getAmount());

    }

    @Test
    void testEntityToDomain(){
        var payment = JpaPaymentEntity.builder().userId(100000L)
                .amount( BigDecimal.valueOf(1000)).feeAmount( BigDecimal.valueOf(100))
                .destination(JpaDestinationAccount.builder()
                        .holderName("Jon Doe").accountNumber( "212356456468").currency( "USD")
                        .routingNumber("9998888888").accountId(5L).build())
                .build();
        var result = paymentMapper.entityToDomain(payment);
        Assertions.assertEquals(result.getUserId(), payment.getUserId());
        Assertions.assertEquals(result.getDestination().getAccountNumber(), payment.getDestination().getAccountNumber());
        Assertions.assertEquals(result.getDestination().getRoutingNumber(), payment.getDestination().getRoutingNumber());
        Assertions.assertEquals(result.getDestination().getCurrency(), payment.getDestination().getCurrency());
        Assertions.assertEquals(result.getAmount(), payment.getAmount());

    }
}