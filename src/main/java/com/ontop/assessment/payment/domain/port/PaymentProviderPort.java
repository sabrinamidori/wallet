package com.ontop.assessment.payment.domain.port;

import com.ontop.assessment.payment.domain.dto.PaymentResponseDTO;
import com.ontop.assessment.payment.domain.dto.SourceAccountDTO;
import com.ontop.assessment.payment.domain.payment.Payment;

public interface PaymentProviderPort {
    PaymentResponseDTO sendMoney(SourceAccountDTO sourceAccount, Payment paymentInfo);
}
