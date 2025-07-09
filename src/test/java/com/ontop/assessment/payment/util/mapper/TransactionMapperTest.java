package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.domain.dto.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
class TransactionMapperTest {

    @InjectMocks
    TransactionMapperImpl transactionMapper;

    @Test
    void dtoToRequest() {
        var dto = new TransactionDTO(1001L, BigDecimal.valueOf(1000));
        var result = transactionMapper.dtoToRequest(dto);
        Assertions.assertEquals(result.getUserId(), dto.userId().toString());
        Assertions.assertEquals(result.getAmount().doubleValue(), dto.amount().doubleValue());
    }
}