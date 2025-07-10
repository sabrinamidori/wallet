package com.ontop.assessment.payment.domain.adapter.service;

import com.ontop.assessment.payment.domain.dto.BalanceDTO;
import com.ontop.assessment.payment.domain.dto.DestinationAccountDTO;
import com.ontop.assessment.payment.domain.dto.PaymentDTO;
import com.ontop.assessment.payment.domain.dto.PaymentResponseDTO;
import com.ontop.assessment.payment.domain.dto.SourceAccountDTO;
import com.ontop.assessment.payment.domain.dto.TransactionDTO;
import com.ontop.assessment.payment.domain.dto.TransactionResponseDTO;
import com.ontop.assessment.payment.domain.dto.WithdrawRequestDTO;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.domain.payment.PaymentStatus;
import com.ontop.assessment.payment.domain.port.PaymentProviderPort;
import com.ontop.assessment.payment.domain.port.PaymentRepositoryPort;
import com.ontop.assessment.payment.domain.port.WalletServicePort;
import com.ontop.assessment.payment.infrastructure.config.PaymentsConfig;
import com.ontop.assessment.payment.infrastructure.config.exception.InvalidOperationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PaymentServiceImplTest {
    @Mock
    PaymentRepositoryPort paymentRepository;
    @Mock
    PaymentProviderPort paymentProviderPort;
    @Mock
    WalletServicePort walletServicePort;
    @Mock
    PaymentsConfig paymentsConfig;
    @InjectMocks
    PaymentServiceImpl paymentServiceImpl;

    @BeforeEach
    void setUp() {
        when(paymentsConfig.getWithdrawFee()).thenReturn("0.1");
        when(paymentsConfig.getMinAmount()).thenReturn(100);
        when(paymentsConfig.getSource()).thenReturn(
                new PaymentsConfig.PaymentSourceConfig("COMPANY", "Ontop Inc",
                        "1323254564", "USD", "87864353"));
    }

    @Test
    void testSendMoney() {
        var destination = new DestinationAccountDTO("Jon", "011895", "USD", "777888");
        Payment payment = new Payment(Long.valueOf(1000), new BigDecimal(1000), new BigDecimal(100), 888L, destination);
        payment.setId(1L);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentRepository.update(anyLong(), any(Payment.class))).thenReturn(payment);
        var paymentResponseDTO = new PaymentResponseDTO("547897", new BigDecimal(1000), PaymentStatus.PROCESSING, null);
        when(paymentProviderPort.sendMoney(any(SourceAccountDTO.class), any(Payment.class))).thenReturn(paymentResponseDTO);
        when(walletServicePort.getBalance(anyLong())).thenReturn(new BalanceDTO(1000L, "2000"));
        var transaction = new TransactionResponseDTO(1000L, 1000L, payment.getAmount().toString());
        when(walletServicePort.createTransaction(any(TransactionDTO.class))).thenReturn(transaction);

        var withdrawRequestDTO = new WithdrawRequestDTO(payment.getUserId(), 1L,
                destination.account().currency(), destination.name(), destination.account().accountNumber(),
                destination.account().routingNumber(), payment.getAmount());
        PaymentDTO result = paymentServiceImpl.sendMoney(withdrawRequestDTO);
        Assertions.assertEquals(paymentResponseDTO.getStatus().getCode(), result.getStatus().getCode());
        Assertions.assertEquals(withdrawRequestDTO.getAmount(), result.getAmount());
        Assertions.assertEquals(destination.account().accountNumber(), result.getDestination().accountNumber());
    }

    @Test
    void testSendMoneyNoBalance() {
        var destination = new DestinationAccountDTO("Jon", "011895", "USD", "777888");
        Payment payment = new Payment(Long.valueOf(1000), new BigDecimal(1000), new BigDecimal(100), 888L, destination);
        payment.setId(1L);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentRepository.update(anyLong(), any(Payment.class))).thenReturn(payment);
        when(walletServicePort.getBalance(anyLong())).thenReturn(new BalanceDTO(1000L, "200"));

        var withdrawRequestDTO = new WithdrawRequestDTO(payment.getUserId(), 1L,
                destination.account().currency(), destination.name(), destination.account().accountNumber(),
                destination.account().routingNumber(), payment.getAmount());
        Assertions.assertThrows(InvalidOperationException.class, () ->paymentServiceImpl.sendMoney(withdrawRequestDTO));
        verify(paymentRepository,never()).save(any(Payment.class));
        verify(paymentRepository,never()).save(paymentRepository.update(anyLong(), any(Payment.class)));
    }
}
