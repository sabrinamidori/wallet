package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.DestinationAccount;
import com.ontop.assessment.payment.domain.dto.DestinationAccountDTO;
import com.ontop.assessment.payment.infrastructure.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", config = MapstructConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
        uses = {BankAccountMapper.class})
public interface DestinationAccountMapper {

    DestinationAccountMapper INSTANCE = Mappers.getMapper(DestinationAccountMapper.class);

    DestinationAccountDTO providerRequestToDto(DestinationAccount request);

    DestinationAccount dtoToProviderRequest(DestinationAccountDTO dto);

}
