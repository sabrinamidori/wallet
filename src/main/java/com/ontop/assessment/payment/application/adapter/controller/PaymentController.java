package com.ontop.assessment.payment.application.adapter.controller;

import com.ontop.assessment.payment.application.usecase.SendMoneyUseCase;
import com.ontop.assessment.payment.domain.dto.PaymentDTO;
import com.ontop.assessment.payment.domain.dto.WithdrawRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Validated
public class PaymentController {
    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping
    public ResponseEntity<PaymentDTO> sendMoney(@Valid @RequestBody WithdrawRequestDTO withdraw) {
        return new ResponseEntity<>(sendMoneyUseCase.sendMoney(withdraw),  HttpStatus.OK);
    }
}
