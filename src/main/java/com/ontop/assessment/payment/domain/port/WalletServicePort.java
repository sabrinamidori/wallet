package com.ontop.assessment.payment.domain.port;

import com.ontop.assessment.payment.domain.dto.BalanceDTO;
import com.ontop.assessment.payment.domain.dto.TransactionDTO;
import com.ontop.assessment.payment.domain.dto.TransactionResponseDTO;

public interface WalletServicePort {
    BalanceDTO getBalance(Long userId);
    TransactionResponseDTO createTransaction(TransactionDTO transactionDTO);
}
