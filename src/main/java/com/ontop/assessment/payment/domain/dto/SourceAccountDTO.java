package com.ontop.assessment.payment.domain.dto;

public record SourceAccountDTO (
     String type,
     String name,
     BankAccountDTO account)
{
    public SourceAccountDTO(String type, String name, String accountNumber,
                                 String currency, String routingNumber) {
        this(type, name, new BankAccountDTO(accountNumber, currency, routingNumber));
    }
}
