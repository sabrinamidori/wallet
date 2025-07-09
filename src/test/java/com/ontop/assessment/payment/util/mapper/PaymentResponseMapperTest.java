package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.PaymentInfo;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.ProviderResponse;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.RequestInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PaymentResponseMapperTest {

    @InjectMocks
    PaymentResponseMapperImpl paymentResponseMapper;
    @Test
    void testProviderResponseToResponseDTO() {

        var response = ProviderResponse.builder().paymentInfo(PaymentInfo.builder().id("100000").amount("1000").build())
                .requestInfo(RequestInfo.builder().status("Processing").build()).build();
        var result = paymentResponseMapper.providerResponseToResponseDTO(response);
        Assertions.assertEquals(result.getStatus().getStatus(), response.getRequestInfo().getStatus());
        Assertions.assertEquals(result.getPaymentId(), response.getPaymentInfo().getId());
        Assertions.assertEquals(result.getPaymentAmount().doubleValue(),Double.parseDouble(response.getPaymentInfo().getAmount()));
    }

}