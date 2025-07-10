package com.ontop.assessment.payment.infrastructure.adapters.outbound.payment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.assessment.payment.domain.dto.DestinationAccountDTO;
import com.ontop.assessment.payment.domain.dto.PaymentResponseDTO;
import com.ontop.assessment.payment.domain.dto.SourceAccountDTO;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.domain.payment.PaymentStatus;
import com.ontop.assessment.payment.infrastructure.config.exception.UnprocessableEntityException;
import com.ontop.assessment.payment.util.mapper.PaymentResponseMapper;
import com.ontop.assessment.payment.util.mapper.WithdrawMapper;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PaymentProviderServiceImplTest {
    @Mock
    PaymentProviderClient paymentProviderClient;
    @Mock
    PaymentResponseMapper paymentResponseMapper;
    @Mock
    WithdrawMapper withdrawMapper;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    FeignException exception;
    @InjectMocks
    PaymentProviderServiceImpl paymentProviderServiceImpl;

    @Test
    void testSendMoney() {

        Map<String,Object> requestMap = Map.of(
                "source", Map.of("type", "COMPANY",
                            "sourceInformation", Map.of("name", "ONTOP INC"),
                            "account", Map.of("accountNumber", "0245253419",
                                        "currency", "USD",
                                        "routingNumber","028444018")),
                "destination", Map.of("name", "TONY STARK",
                        "account", Map.of("accountNumber", "1885226711",
                                "currency", "USD",
                                "routingNumber","211927207")),
                "amount", "9000"
                );
        var paymentResponseDTO = new PaymentResponseDTO("10001", new BigDecimal(9000), PaymentStatus.PROCESSING, null);
        var destination = new DestinationAccount("holder", new AccountInfo("7777777", "USD", "888888"));
        var destinationDto = new DestinationAccountDTO(destination.getName(), destination.getAccount().getAccountNumber(), destination.getAccount().getCurrency(), destination.getAccount().getRoutingNumber());
        var source = new SourceAccount("type", new SourceInfo("company"), new AccountInfo("88888", "USD", "777777"));
        var sourceDto = new SourceAccountDTO(source.getType(), source.getSourceInformation().getName(), source.getAccount().getAccountNumber(), source.getAccount().getCurrency(), source.getAccount().getRoutingNumber());

        when(withdrawMapper.toProviderRequest(any(SourceAccountDTO.class), any(Payment.class))).thenReturn(new WithdrawProviderRequest(source, destination, "90000"));
        when(paymentProviderClient.sendMoney(any(WithdrawProviderRequest.class))).thenReturn(requestMap);
        when(paymentResponseMapper.providerResponseToResponseDTO(any(ProviderResponse.class))).thenReturn(paymentResponseDTO);
        when(objectMapper.convertValue(any(Map.class), (Class<ProviderResponse>) any()))
                .thenReturn( ProviderResponse.builder()
                        .requestInfo(new RequestInfo(PaymentProviderStatus.PROCESSING.getStatus(), null))
                        .paymentInfo(new PaymentInfo("fdhd-555555-77777-999999-10", "9000")).build());

        PaymentResponseDTO result = paymentProviderServiceImpl.sendMoney(sourceDto,
                new Payment(1000L, BigDecimal.valueOf(10000), BigDecimal.valueOf(1000), Long.valueOf(1), destinationDto));
        Assertions.assertEquals(paymentResponseDTO, result);
    }
    @Test
    void testFailedSendingMoney() throws Exception {

        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("requestInfo", Map.of("status", "Failed",
                "error", Map.of("name", "bank rejected payment")));
        requestMap.put("paymentInfo", Map.of("amount", 1000,
                "id", "7633f4c9-51e4-4b62-97b0-51156966f1d7"));
        requestMap.put("amount", "9000");
         when(exception.contentUTF8()).thenReturn(""" 
            {
            "requestInfo":{
                "status":"Failed",
                        "error":"bank rejected payment"
            },
            "paymentInfo":{
                "amount":1000,
                        "id":"7633f4c9-51e4-4b62-97b0-51156966f1d7"
            }
        }
        """);
         when(exception.status()).thenReturn(500);
        var paymentResponseDTO = new PaymentResponseDTO("10001", new BigDecimal(9000), PaymentStatus.FAILED, "bank rejected payment");
        var destination = new DestinationAccount("holder", new AccountInfo("7777777", "USD", "888888"));
        var destinationDto = new DestinationAccountDTO(destination.getName(), destination.getAccount().getAccountNumber(), destination.getAccount().getCurrency(), destination.getAccount().getRoutingNumber());
        var source = new SourceAccount("type", new SourceInfo("company"), new AccountInfo("88888", "USD", "777777"));
        var sourceDto = new SourceAccountDTO(source.getType(), source.getSourceInformation().getName(), source.getAccount().getAccountNumber(), source.getAccount().getCurrency(), source.getAccount().getRoutingNumber());


        when(withdrawMapper.toProviderRequest(any(SourceAccountDTO.class), any(Payment.class))).thenReturn(new WithdrawProviderRequest(source, destination, "90000"));
        when(paymentProviderClient.sendMoney(any(WithdrawProviderRequest.class))).thenThrow(exception);
        when(objectMapper.readValue(anyString(), (TypeReference<Map<String, Object>>) any())).thenReturn(requestMap);
        when(objectMapper.readValue(anyString(), eq(ProviderResponse.class))).thenReturn(
                ProviderResponse.builder()
                        .paymentInfo(new PaymentInfo(paymentResponseDTO.getPaymentId(), paymentResponseDTO.getPaymentAmount().toString()))
                        .requestInfo(new RequestInfo(paymentResponseDTO.getStatus().getStatus(), paymentResponseDTO.getError())).build());
        when(paymentResponseMapper.providerResponseToResponseDTO(any(ProviderResponse.class))).thenReturn(paymentResponseDTO);

        PaymentResponseDTO result = paymentProviderServiceImpl.sendMoney(sourceDto,
                new Payment(1000L, BigDecimal.valueOf(10000), BigDecimal.valueOf(1000), Long.valueOf(1), destinationDto));
        Assertions.assertEquals(paymentResponseDTO, result);
        Assertions.assertEquals("Failed", result.getStatus().getStatus());
    }

    @Test
    void testErrorSendingMoney() throws Exception {

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("error", "body is invalid, check postman collection example");
        when(exception.contentUTF8()).thenReturn(""" 
                    {
                         "error": "body is invalid, check postman collection example"
                    }
                """);
        when(exception.status()).thenReturn(400);
        var destination = new DestinationAccount("holder", new AccountInfo("7777777", "USD", "888888"));
        var destinationDto = new DestinationAccountDTO(destination.getName(), destination.getAccount().getAccountNumber(), destination.getAccount().getCurrency(), destination.getAccount().getRoutingNumber());
        var source = new SourceAccount("type", new SourceInfo("company"), new AccountInfo("88888", "USD", "777777"));
        var sourceDto = new SourceAccountDTO(source.getType(), source.getSourceInformation().getName(), source.getAccount().getAccountNumber(), source.getAccount().getCurrency(), source.getAccount().getRoutingNumber());
        var payment = new Payment(1000L, BigDecimal.valueOf(10000), BigDecimal.valueOf(1000), Long.valueOf(1), destinationDto);

        when(withdrawMapper.toProviderRequest(any(SourceAccountDTO.class), any(Payment.class))).thenReturn(new WithdrawProviderRequest(source, destination, "90000"));
        when(paymentProviderClient.sendMoney(any(WithdrawProviderRequest.class))).thenThrow(exception);
        when(objectMapper.readValue(anyString(), (TypeReference<Map<String, Object>>) any())).thenReturn(requestMap);

        assertThrows(UnprocessableEntityException.class, () -> paymentProviderServiceImpl.sendMoney(sourceDto, payment));
    }

}
