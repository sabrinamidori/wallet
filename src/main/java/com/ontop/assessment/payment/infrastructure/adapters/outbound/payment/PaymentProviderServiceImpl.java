package com.ontop.assessment.payment.infrastructure.adapters.outbound.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.assessment.payment.domain.dto.PaymentResponseDTO;
import com.ontop.assessment.payment.domain.dto.SourceAccountDTO;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.domain.port.PaymentProviderPort;
import com.ontop.assessment.payment.infrastructure.config.exception.UnprocessableEntityException;
import com.ontop.assessment.payment.util.mapper.PaymentResponseMapper;
import com.ontop.assessment.payment.util.mapper.WithdrawMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Optional.ofNullable;


@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentProviderServiceImpl implements PaymentProviderPort {

    private final PaymentProviderClient paymentProviderClient;
    private final PaymentResponseMapper paymentResponseMapper;
    private final WithdrawMapper withdrawMapper;
    private final ObjectMapper objectMapper;

    @Override
    public PaymentResponseDTO sendMoney(SourceAccountDTO sourceAccount, Payment paymentInfo) {
        try {
            var request = withdrawMapper.toProviderRequest(sourceAccount, paymentInfo);
            var result = paymentProviderClient.sendMoney(request);

            var response = objectMapper.convertValue(result, ProviderResponse.class);
            return paymentResponseMapper.providerResponseToResponseDTO(response);
        } catch (FeignException e) {
            log.error(e.getMessage(), e);
            return convertError(e);
        }
    }

    private PaymentResponseDTO convertError(FeignException e) {
        try {
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            var message = e.contentUTF8();
            var errorMap = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
            ofNullable(errorMap.get("error")).ifPresent(ex -> {
                log.error((String) ex);
                throw new UnprocessableEntityException((String) ex);
            });
            var response =  objectMapper.readValue(message, ProviderResponse.class);
            return paymentResponseMapper.providerResponseToResponseDTO(response);

        } catch (JsonProcessingException ex) {
            throw new UnprocessableEntityException("Error during to process the payment.");
        }
    }
}
