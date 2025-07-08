package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.domain.dto.TransactionDTO;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.wallet.TransactionRequest;
import com.ontop.assessment.payment.infrastructure.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", config = MapstructConfig.class)
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionRequest dtoToRequest(TransactionDTO dto);

}
