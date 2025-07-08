package com.ontop.assessment.payment.domain.dto;

import java.math.BigDecimal;

public record TransactionDTO (
        Long userId,
        BigDecimal amount
){ }
