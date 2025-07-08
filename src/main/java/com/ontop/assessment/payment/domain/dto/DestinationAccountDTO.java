package com.ontop.assessment.payment.domain.dto;

public record DestinationAccountDTO (
    String name,
    BankAccountDTO account)
{
    public DestinationAccountDTO(String name, String accountNumber,
     String currency, String routingNumber) {
        this(name, new BankAccountDTO(accountNumber, currency, routingNumber));
    }
}
