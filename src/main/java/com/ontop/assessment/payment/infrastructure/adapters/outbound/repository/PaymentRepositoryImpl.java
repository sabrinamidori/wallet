package com.ontop.assessment.payment.infrastructure.adapters.outbound.repository;

import com.ontop.assessment.payment.domain.payment.PaymentStatus;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.entity.JpaPaymentEntity;
import com.ontop.assessment.payment.domain.port.PaymentRepositoryPort;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.infrastructure.config.exception.ResourceNotFoundException;
import com.ontop.assessment.payment.util.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryPort {
    private final JpaPaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public Payment save(Payment payment) {
        JpaPaymentEntity paymentEntity = paymentMapper.domainToEntity(payment);
        return paymentMapper.entityToDomain(paymentRepository.save(paymentEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public Payment findById(long paymentId) {
        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment transaction not found"));
        return paymentMapper.entityToDomain(payment);
    }

    @Override
    @Transactional
    public Payment update(long paymentId, Payment payment) {
        var paymentEntity = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment transaction not found"));

        paymentEntity.setExternalId(payment.getExternalId());
        paymentEntity.setWithdrawId(payment.getWithdrawId());
        paymentEntity.setRefundId(payment.getRefundId());
        paymentEntity.setStatus(payment.getStatus());
        paymentEntity.setFailReason(payment.getFailReason());
        paymentEntity.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

        return paymentMapper.entityToDomain(paymentRepository.save(paymentEntity));
    }

    @Override
    @Transactional
    public void updateStatus(long paymentId, PaymentStatus status) {
        paymentRepository.updateStatusById(status.getCode(), paymentId);
    }
}
