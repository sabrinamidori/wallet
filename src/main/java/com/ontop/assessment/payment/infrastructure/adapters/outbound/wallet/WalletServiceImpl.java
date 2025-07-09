package com.ontop.assessment.payment.infrastructure.adapters.outbound.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.assessment.payment.domain.dto.BalanceDTO;
import com.ontop.assessment.payment.domain.dto.TransactionDTO;
import com.ontop.assessment.payment.domain.dto.TransactionResponseDTO;
import com.ontop.assessment.payment.domain.port.WalletServicePort;
import com.ontop.assessment.payment.infrastructure.config.exception.InvalidOperationException;
import com.ontop.assessment.payment.infrastructure.config.exception.ResourceNotFoundException;
import com.ontop.assessment.payment.infrastructure.config.exception.UnprocessableEntityException;
import com.ontop.assessment.payment.util.mapper.TransactionMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Optional.ofNullable;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletServicePort {
    private final WalletServiceClient walletServiceClient;
    private final TransactionMapper transactionMapper;
    private final ObjectMapper objectMapper;

    @Override
    public BalanceDTO getBalance(Long userId) {
        var result =  walletServiceClient.getUserBalance(userId);

        return ofNullable(result).map(r -> objectMapper.convertValue(r, BalanceDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("No balance found for the user"));
    }

    @Override
    public TransactionResponseDTO createTransaction(TransactionDTO transactionDTO) {
        try {
            var request = transactionMapper.dtoToRequest(transactionDTO);
            var response = walletServiceClient.createTransaction(request);
            return ofNullable(response).map(r -> objectMapper.convertValue(r, TransactionResponseDTO.class))
                    .orElseThrow(() -> new ResourceNotFoundException( "Error creating wallet transaction"));
        } catch (FeignException e) {
            log.error(e.getMessage(), e);
            convertError(e);
        }
        return null;
    }

    private void convertError(FeignException e) {
        try {
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            var message = e.contentUTF8();
            var errorMap = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
            ofNullable(errorMap.get("code")).ifPresentOrElse(ex -> {
                log.error((String) ex);
                if(e.status() == HttpStatus.NOT_FOUND.value()) {
                    throw new ResourceNotFoundException(WalletError.valueOf((String) ex), (String) errorMap.get("message"));
                }
                throw new InvalidOperationException(WalletError.valueOf((String) ex), (String) errorMap.get("message"));
            }, () -> {
                throw new UnprocessableEntityException("Error updating the balance.");
            });

        } catch (JsonProcessingException ex) {
            throw new UnprocessableEntityException("Error from wallet service.");
        }
    }
}
