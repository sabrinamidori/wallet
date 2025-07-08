package com.ontop.assessment.payment.domain.port;

import com.ontop.assessment.payment.domain.dto.PaymentDTO;
import com.ontop.assessment.payment.domain.dto.WithdrawRequestDTO;

public interface PaymentServicePort {
    PaymentDTO sendMoney(WithdrawRequestDTO withdrawRequestDTO);
}
