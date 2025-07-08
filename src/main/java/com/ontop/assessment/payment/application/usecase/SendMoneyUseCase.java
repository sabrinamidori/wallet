package com.ontop.assessment.payment.application.usecase;

import com.ontop.assessment.payment.domain.dto.PaymentDTO;
import com.ontop.assessment.payment.domain.dto.WithdrawRequestDTO;

public interface SendMoneyUseCase {
    PaymentDTO sendMoney(WithdrawRequestDTO withdrawRequestDTO);
}
