package com.ontop.assessment.payment.domain.port;

import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.domain.payment.PaymentStatus;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
    Payment findById(long paymentId);
    Payment update(long paymentId, Payment payment);
    void updateStatus(long paymentId, PaymentStatus status);

}
