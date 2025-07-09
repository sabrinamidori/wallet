package com.ontop.assessment.payment.domain.port;

import com.ontop.assessment.payment.domain.payment.Payment;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
    Payment findById(long paymentId);
    Payment update(long paymentId, Payment payment);

}
