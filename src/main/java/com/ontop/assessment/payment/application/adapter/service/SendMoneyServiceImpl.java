package com.ontop.assessment.payment.application.adapter.service;

import com.ontop.assessment.payment.application.usecase.SendMoneyUseCase;
import com.ontop.assessment.payment.domain.dto.PaymentDTO;
import com.ontop.assessment.payment.domain.dto.WithdrawRequestDTO;
import com.ontop.assessment.payment.domain.port.PaymentServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendMoneyServiceImpl implements SendMoneyUseCase {
    private final PaymentServicePort paymentServicePort;
    @Override
    public PaymentDTO sendMoney(WithdrawRequestDTO withdrawRequestDTO) {
        log.info("Sending money to {}", withdrawRequestDTO.getUserId());
        return paymentServicePort.sendMoney(withdrawRequestDTO);
    }
}
