package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.SourceAccount;
import com.ontop.assessment.payment.domain.dto.SourceAccountDTO;
import com.ontop.assessment.payment.infrastructure.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", config = MapstructConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
        uses = {BankAccountMapper.class})
public interface SourceAccountMapper {

    SourceAccountMapper INSTANCE = Mappers.getMapper(SourceAccountMapper.class);

    @Mapping(source = "sourceInformation.name", target = "name")
    SourceAccountDTO providerRequestToDto(SourceAccount request);

    @Mapping(target = "sourceInformation.name", source = "name")
    SourceAccount dtoToProviderRequest(SourceAccountDTO dto);

}
