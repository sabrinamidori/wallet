package com.ontop.assessment.payment.domain.dto;

import java.math.BigDecimal;

public record TransactionResponseDTO (
    long walletTransactionId,
    long userId,
    BigDecimal amount)
{}
