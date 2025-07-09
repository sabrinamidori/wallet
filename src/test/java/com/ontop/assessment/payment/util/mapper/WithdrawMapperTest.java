package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.domain.dto.DestinationAccountDTO;
import com.ontop.assessment.payment.domain.dto.SourceAccountDTO;
import com.ontop.assessment.payment.domain.payment.BankAccount;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.AccountInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class WithdrawMapperTest {

    @InjectMocks
    WithdrawMapperImpl withdrawMapper;
    @Mock
    BankAccountMapper bankAccountMapper;

    AccountInfo accountInfo;

    @BeforeEach
    void init(){
        accountInfo = AccountInfo.builder().currency("USD").accountNumber("545465465").routingNumber("48678451").build();
        when(bankAccountMapper.dtoToProviderRequest(any())).thenReturn(accountInfo);
    }

    @Test
    void testDtoToProviderDestination() {
        var account = new BankAccount(1L, "Jon Doe","545465465", "48678451", "USD");
        var result = withdrawMapper.dtoToProviderDestination(account);
        Assertions.assertEquals(result.getName(), account.getHolderName());
        Assertions.assertEquals(result.getAccount().getAccountNumber(), account.getAccountNumber());
        Assertions.assertEquals(result.getAccount().getRoutingNumber(), account.getRoutingNumber());
        Assertions.assertEquals(result.getAccount().getCurrency(), account.getCurrency());

    }

    @Test
    void testDtoToProviderSource() {
        var account = new SourceAccountDTO("Company", "Ontop",accountInfo.getAccountNumber(),
                accountInfo.getCurrency(), accountInfo.getRoutingNumber());
        var result = withdrawMapper.dtoToProviderSource(account);
        Assertions.assertEquals(result.getSourceInformation().getName(), account.name());
        Assertions.assertEquals(result.getAccount().getAccountNumber(), account.account().accountNumber());
        Assertions.assertEquals(result.getAccount().getRoutingNumber(), account.account().routingNumber());
        Assertions.assertEquals(result.getAccount().getCurrency(), account.account().currency());
    }

    @Test
    void testToProviderRequest() {
        var info = new Payment(100000L, BigDecimal.valueOf(1000), BigDecimal.valueOf(100),5L,
                new DestinationAccountDTO("Jon Doe", accountInfo.getAccountNumber(),
                        accountInfo.getCurrency(), accountInfo.getRoutingNumber()));
        var account = new SourceAccountDTO("Company", "Ontop",accountInfo.getAccountNumber(),
                accountInfo.getCurrency(), accountInfo.getRoutingNumber());
        var result = withdrawMapper.toProviderRequest(account,info);
        Assertions.assertEquals(result.getDestination().getName(), info.getDestination().getHolderName());
        Assertions.assertEquals(result.getDestination().getAccount().getCurrency(), info.getDestination().getCurrency());
        Assertions.assertEquals(result.getDestination().getAccount().getAccountNumber(), info.getDestination().getAccountNumber());
        Assertions.assertEquals(result.getDestination().getAccount().getRoutingNumber(), info.getDestination().getRoutingNumber());
        Assertions.assertEquals(result.getSource().getType(), account.type());
        Assertions.assertEquals(result.getSource().getSourceInformation().getName(), account.name());
        Assertions.assertEquals(result.getSource().getAccount().getAccountNumber(), account.account().accountNumber());
        Assertions.assertEquals(result.getSource().getAccount().getRoutingNumber(), account.account().routingNumber());
        Assertions.assertEquals(result.getSource().getAccount().getCurrency(), account.account().currency());
    }
}