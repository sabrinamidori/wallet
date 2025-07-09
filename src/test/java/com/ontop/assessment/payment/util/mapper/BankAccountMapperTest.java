package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.domain.dto.BankAccountDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class BankAccountMapperTest {
    @InjectMocks
    BankAccountMapperImpl bankAccountMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDtoToProviderRequest(){
        var dto = new BankAccountDTO("2315468484", "USD", "88888888");
        var result = bankAccountMapper.dtoToProviderRequest(dto);
        Assertions.assertEquals(result.getAccountNumber(), dto.accountNumber());
        Assertions.assertEquals(result.getCurrency(), dto.currency());
        Assertions.assertEquals(result.getRoutingNumber(), dto.routingNumber());

    }
}
