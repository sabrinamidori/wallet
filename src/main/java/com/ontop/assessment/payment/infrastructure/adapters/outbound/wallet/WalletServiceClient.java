package com.ontop.assessment.payment.infrastructure.adapters.outbound.wallet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "walletServiceClient", url = "${wallet.service_url}")
public interface WalletServiceClient {

    @PostMapping(value = "/transactions", produces = "application/json", consumes = "application/json")
    Map<String, Object> createTransaction(@NotNull @RequestBody TransactionRequest transactionRequest);

    @GetMapping(value = "/balance", produces = "application/json")
    Map<String, Object> getUserBalance(@NotBlank @RequestParam(value = "user_id") final Long userId);
}
