package com.ontop.assessment.payment.application.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.assessment.payment.application.usecase.SendMoneyUseCase;
import com.ontop.assessment.payment.domain.dto.WithdrawRequestDTO;
import com.ontop.assessment.payment.domain.payment.PaymentStatus;
import com.ontop.assessment.payment.domain.port.PaymentProviderPort;
import com.ontop.assessment.payment.domain.port.PaymentRepositoryPort;
import com.ontop.assessment.payment.domain.port.PaymentServicePort;
import com.ontop.assessment.payment.domain.port.WalletServicePort;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.wallet.WalletError;
import com.ontop.assessment.payment.infrastructure.config.BeanConfig;
import com.ontop.assessment.payment.infrastructure.config.PaymentsConfig;
import com.ontop.assessment.payment.infrastructure.config.exception.ErrorsCode;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(BeanConfig.class)
class PaymentControllerTest {
    private static final String PAYMENT_PATH = "/payment";
    @Autowired
    SendMoneyUseCase sendMoneyUseCase;
    @Autowired
    PaymentServicePort paymentServicePort;
    @Autowired PaymentRepositoryPort paymentRepository;
    @Autowired PaymentProviderPort paymentProviderPort;
    @Autowired WalletServicePort walletServicePort;
    @Autowired PaymentsConfig paymentsConfig;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMoney() throws Exception {
        var resource = WithdrawRequestDTO.builder()
                        .accountId(1L).accountNumber("1885226711").routingNumber("211927207")
                        .userId(1000L).currency("USD").holderName("TONY STARK")
                        .amount(BigDecimal.valueOf(1000)).build();
        this.mockMvc.perform(post(PAYMENT_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(1)))
                .andExpect(jsonPath("$.user_id", Is.is(1000)))
                .andExpect(jsonPath("$.withdraw_id", IsNull.notNullValue()))
                .andExpect(jsonPath("$.external_id", IsNull.notNullValue()))
                .andExpect(jsonPath("$.fee_amount", Is.is(100.00)))
                .andExpect(jsonPath("$.status", Is.is("PROCESSING")));
    }

    @Test
    void testSendMoneyValidationError() throws Exception {
        var resource = WithdrawRequestDTO.builder()
                .accountId(1L).routingNumber("211927207")
                .userId(1000L).currency("USD").holderName("TONY STARK")
                .amount(BigDecimal.valueOf(1000)).build();
        this.mockMvc.perform(post(PAYMENT_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Is.is(ErrorsCode.INVALID_BODY.getCode())))
                .andExpect(jsonPath("$.message", StringEndsWith.endsWith("Destination bank account number can't be null.")));
    }

    @Test
    void testSendMoneyProviderError() throws Exception {
        var resource = WithdrawRequestDTO.builder()
                .accountId(1L).accountNumber("1885226711").routingNumber("211927207")
                .userId(1000L).currency("USD").holderName("JAMES FAILED")
                .amount(BigDecimal.valueOf(1000)).build();
        this.mockMvc.perform(post(PAYMENT_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Is.is(PaymentStatus.FAILED.getCode())))
                .andExpect(jsonPath("$.message", StringContains.containsString("bank rejected payment")));
    }

    @Test
    void testSendMoneyWalletError() throws Exception {
        var resource = WithdrawRequestDTO.builder()
                .accountId(1L).accountNumber("1885226711").routingNumber("211927207")
                .userId(404L).currency("USD").holderName("JAMES FAILED")
                .amount(BigDecimal.valueOf(2000)).build();
        this.mockMvc.perform(post(PAYMENT_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Is.is(WalletError.INVALID_USER.getCode())))
                .andExpect(jsonPath("$.message", StringContains.containsString("user not found")));
    }

    @Test
    void testSendMoneyNotEnoughBalance() throws Exception {
        var resource = WithdrawRequestDTO.builder()
                .accountId(1L).accountNumber("1885226711").routingNumber("211927207")
                .userId(404L).currency("USD").holderName("TONY STARK")
                .amount(BigDecimal.valueOf(3000)).build();
        this.mockMvc.perform(post(PAYMENT_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Is.is(ErrorsCode.NOT_ENOUGH_BALANCE.getCode())))
                .andExpect(jsonPath("$.message", StringContains.containsString("Insufficient balance.")));
    }
}
