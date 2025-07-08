package com.ontop.assessment.payment.domain.dto;

import com.ontop.assessment.payment.domain.payment.BankAccount;

public record BankAccountDTO (
     String accountNumber,
     String currency,
     String routingNumber)
{
    public BankAccountDTO(BankAccount bankAccount) {
        this(bankAccount.getAccountNumber(), bankAccount.getCurrency(), bankAccount.getRoutingNumber());
    }
}
