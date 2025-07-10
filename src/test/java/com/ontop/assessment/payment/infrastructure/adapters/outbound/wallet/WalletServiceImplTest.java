package com.ontop.assessment.payment.infrastructure.adapters.outbound.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.assessment.payment.domain.dto.BalanceDTO;
import com.ontop.assessment.payment.domain.dto.TransactionDTO;
import com.ontop.assessment.payment.domain.dto.TransactionResponseDTO;
import com.ontop.assessment.payment.infrastructure.config.exception.ResourceNotFoundException;
import com.ontop.assessment.payment.util.mapper.TransactionMapper;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class WalletServiceImplTest {
    @Mock
    WalletServiceClient walletServiceClient;
    @Mock
    TransactionMapper transactionMapper;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    FeignException exception;
    @InjectMocks
    WalletServiceImpl walletServiceImpl;

    @Test
    void testGetBalance() {
        var response = new BalanceDTO(1000, "2500");
        when(walletServiceClient.getUserBalance(anyLong())).thenReturn(Map.of("balance", 2500, "user_id", 1000));
        when(objectMapper.convertValue(any(Object.class),  eq(BalanceDTO.class))).thenReturn(response);

        BalanceDTO result = walletServiceImpl.getBalance(1000L);
        Assertions.assertEquals(response.balance(), result.balance());
        Assertions.assertEquals(response.userId(), result.userId());
    }
    @Test
    void testGetBalanceError() {
        when(walletServiceClient.getUserBalance(anyLong())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->walletServiceImpl.getBalance(1000L));
    }

    @Test
    void testCreateTransaction() {
        var response = new TransactionResponseDTO(59974, 1000, BigDecimal.valueOf(-2300));
        when(walletServiceClient.createTransaction(any(TransactionRequest.class)))
                .thenReturn(Map.of("wallet_transaction_id", 59974, "amount", -2300, "user_id", 1000));
        when(transactionMapper.dtoToRequest(any(TransactionDTO.class))).thenReturn(new TransactionRequest("1000", new BigDecimal(-2300)));
        when(objectMapper.disable(any(DeserializationFeature.class))).thenReturn(objectMapper);
        when(objectMapper.convertValue(any(Map.class), (Class<TransactionResponseDTO>) any())).thenReturn(response);

        TransactionResponseDTO result = walletServiceImpl.createTransaction(new TransactionDTO(response.userId(), response.amount()));
        Assertions.assertEquals(response.walletTransactionId(), result.walletTransactionId());
        Assertions.assertEquals(response.amount(), result.amount());
    }

    @Test
    void testErrorCreatingTransaction() throws Exception {

        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("code", "INVALID_USER");
        requestMap.put("message", "user not found");
        when(exception.contentUTF8()).thenReturn(""" 
            {
                "code": "INVALID_USER",
                "message": "user not found"
            }
        """);
        when(exception.status()).thenReturn(HttpStatus.NOT_FOUND.value());
        var response = new TransactionResponseDTO(59974, 1000, BigDecimal.valueOf(-2300));
        TransactionDTO transactionDTO = new TransactionDTO(response.userId(), response.amount());

        when(walletServiceClient.createTransaction(any(TransactionRequest.class))).thenThrow(exception);
        when(transactionMapper.dtoToRequest(any(TransactionDTO.class))).thenReturn(new TransactionRequest("1000", new BigDecimal(-2300)));
        when(objectMapper.disable(any(DeserializationFeature.class))).thenReturn(objectMapper);
        when(objectMapper.readValue(anyString(), (TypeReference<Map<String, Object>>)any())).thenReturn(requestMap);

        assertThrows(ResourceNotFoundException.class,
                () -> {
                    walletServiceImpl.createTransaction(transactionDTO);
                });
    }
}
