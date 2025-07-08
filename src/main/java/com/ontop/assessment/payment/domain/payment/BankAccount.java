package com.ontop.assessment.payment.domain.payment;

import java.io.Serializable;

public class BankAccount implements Serializable {
    private Long accountId;
    private String holderName;
    private String accountNumber;
    private String routingNumber;
    private String currency;

    public BankAccount() {}

    public BankAccount(Long accountId, String holderName, String accountNumber, String routingNumber, String currency) {
        this.accountId = accountId;
        this.holderName = holderName;
        this.routingNumber = routingNumber;
        this.accountNumber = accountNumber;
        this.currency = currency;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
