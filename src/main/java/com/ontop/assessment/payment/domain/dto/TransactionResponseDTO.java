package com.ontop.assessment.payment.domain.dto;

public record TransactionResponseDTO (
    long walletTransactionId,
    long userId,
    String amount)
{}
