package com.ontop.assessment.payment.infrastructure.adapters.outbound.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@FeignClient(name = "paymentProviderClient", url = "${payment.provider-feign-url}")
public interface PaymentProviderClient {

    @PostMapping(value = "/payments", produces = "application/json", consumes = "application/json")
    Map<String, Object> sendMoney(@RequestBody WithdrawProviderRequest withdrawProviderRequest);
}
