package com.ontop.assessment.payment.domain.adapter.service;

import com.ontop.assessment.payment.domain.dto.DestinationAccountDTO;
import com.ontop.assessment.payment.domain.dto.PaymentDTO;
import com.ontop.assessment.payment.domain.dto.SourceAccountDTO;
import com.ontop.assessment.payment.domain.dto.TransactionDTO;
import com.ontop.assessment.payment.domain.dto.TransactionResponseDTO;
import com.ontop.assessment.payment.domain.dto.WithdrawRequestDTO;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.domain.payment.PaymentStatus;
import com.ontop.assessment.payment.domain.port.PaymentRepositoryPort;
import com.ontop.assessment.payment.domain.port.PaymentProviderPort;
import com.ontop.assessment.payment.domain.port.PaymentServicePort;
import com.ontop.assessment.payment.domain.port.WalletServicePort;
import com.ontop.assessment.payment.infrastructure.config.PaymentsConfig;
import com.ontop.assessment.payment.infrastructure.config.exception.ErrorsCode;
import com.ontop.assessment.payment.infrastructure.config.exception.InvalidOperationException;
import com.ontop.assessment.payment.infrastructure.config.exception.UnprocessableEntityException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.valueOf;
import static java.util.Optional.ofNullable;

public class PaymentServiceImpl implements PaymentServicePort {
    private final PaymentRepositoryPort paymentRepository;
    private final PaymentProviderPort paymentProviderPort;
    private final WalletServicePort walletServicePort;
    private final PaymentsConfig paymentsConfig;

    public PaymentServiceImpl(PaymentRepositoryPort paymentRepository,
                              PaymentProviderPort paymentProviderPort,
                              WalletServicePort walletServicePort,
                              PaymentsConfig paymentsConfig) {
        this.paymentRepository = paymentRepository;
        this.paymentProviderPort = paymentProviderPort;
        this.walletServicePort = walletServicePort;
        this.paymentsConfig = paymentsConfig;
    }

    @Override
    public PaymentDTO sendMoney(WithdrawRequestDTO dto) {
        validateAmount(dto.getUserId(), dto.getAmount());
        //register the payment intent
        var payment = createPayment(dto.getUserId(), dto.getAccountId(),
                dto.getAmount(), calculateFee(dto.getAmount()), collectDestinationInfo(dto));

        //withdraw from user wallet
        withdrawWallet(payment);
        try {

            var response = paymentProviderPort.sendMoney(getSourceAccountInfo(), payment);

            payment.setExternalId(response.getPaymentId());

            if (response.getStatus() == PaymentStatus.FAILED) {
                throw new InvalidOperationException(response.getStatus(), response.getError());
            }

        } catch (UnprocessableEntityException | InvalidOperationException e) {
            refundWallet(payment,e.getMessage());

            paymentRepository.update(payment.getId(), payment);
            throw e;
        }
        paymentRepository.update(payment.getId(), payment);
        return new PaymentDTO(payment);
    }

    private TransactionResponseDTO updateBalance(Long userId, BigDecimal amount) {
        return walletServicePort.createTransaction(new TransactionDTO(userId, amount));
    }

    private void refundWallet(Payment payment, String reason) {
        var transaction = updateBalance(payment.getUserId(), payment.getAmount());
        payment.setRefundId(transaction.walletTransactionId());
        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailReason(reason);
    }

    private void withdrawWallet(Payment payment) {
        try {
            var transaction = updateBalance(payment.getUserId(), payment.getAmount().negate());
            payment.setWithdrawId(transaction.walletTransactionId());
        } catch (UnprocessableEntityException | InvalidOperationException e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailReason(e.getMessage());
            paymentRepository.update(payment.getId(), payment);
            throw e;
        }
    }

    private Payment createPayment(Long userId, Long accountId, BigDecimal amount,
                                  BigDecimal fee, DestinationAccountDTO destinationAccount) {
        var payment = new Payment(userId, amount, fee, accountId, destinationAccount);
        return paymentRepository.save(payment);
    }

    private DestinationAccountDTO collectDestinationInfo(WithdrawRequestDTO dto) {
        return new DestinationAccountDTO(dto.getHolderName(), dto.getAccountNumber(),
                dto.getCurrency(), dto.getRoutingNumber());
    }

    private void validateAmount(Long userId, BigDecimal amount) {
        if (amount.doubleValue() < paymentsConfig.getMinAmount())
            throw new InvalidOperationException(ErrorsCode.INVALID_AMOUNT,
                    String.format("The withdraw amount must be greater than $%d", paymentsConfig.getMinAmount()));

        var balance = walletServicePort.getBalance(userId);
        if (Double.parseDouble(balance.balance()) < amount.doubleValue())
            throw new InvalidOperationException(ErrorsCode.NOT_ENOUGH_BALANCE, "Insufficient balance.");
    }

    private BigDecimal calculateFee(BigDecimal amount) {
        var fee = valueOf(Double.parseDouble(ofNullable(paymentsConfig.getWithdrawFee()).orElse("0")));
        return amount.multiply(fee).setScale(2, RoundingMode.CEILING);
    }

    private SourceAccountDTO getSourceAccountInfo() {
        return new SourceAccountDTO(paymentsConfig.getSource().getType(),
                paymentsConfig.getSource().getSourceInformationName(),
                paymentsConfig.getSource().getAccountNumber(),
                paymentsConfig.getSource().getAccountCurrency(),
                paymentsConfig.getSource().getAccountRoutingNumber());
    }
}
