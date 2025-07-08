package com.ontop.assessment.payment.domain.payment;

import com.ontop.assessment.payment.domain.Auditable;
import com.ontop.assessment.payment.domain.dto.DestinationAccountDTO;

import java.math.BigDecimal;
import java.util.Optional;

public class Payment extends Auditable {
    private Long id;
    private Long userId;
    private BankAccount destination;

    private Long withdrawId;
    private BigDecimal amount;
    private BigDecimal feeAmount;
    private PaymentStatus status;
    private Long refundId;
    private String externalId;
    private String failReason;

    public Payment() {
    }

    public Payment(Long userId, BigDecimal amount, BigDecimal fee,
                   Long accountId, DestinationAccountDTO destination){
        this.userId = userId;
        this.amount = amount;
        this.feeAmount = fee;
        this.status = PaymentStatus.PROCESSING;
        this.destination = new BankAccount(accountId, destination.name(), destination.account().accountNumber(),
                        destination.account().routingNumber(),destination.account().currency());
    }

    public Payment(Long id, Long userId, BankAccount destination, BigDecimal amount,
                   BigDecimal feeAmount) {
        this.id = id;
        this.userId = userId;
        this.destination = destination;
        this.amount = amount;
        this.feeAmount = feeAmount;
        this.status = PaymentStatus.PROCESSING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BankAccount getDestination() {
        return destination;
    }

    public void setDestination(BankAccount destination) {
        this.destination = destination;
    }

    public Long getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(Long withdrawId) {
        this.withdrawId = withdrawId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public BigDecimal getTotal() {
        return Optional.ofNullable(amount).orElse(BigDecimal.ZERO).subtract(Optional.ofNullable(feeAmount).orElse(BigDecimal.ZERO));
    }
}
