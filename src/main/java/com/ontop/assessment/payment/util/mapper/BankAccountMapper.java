package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.AccountInfo;
import com.ontop.assessment.payment.domain.dto.BankAccountDTO;
import com.ontop.assessment.payment.infrastructure.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", config = MapstructConfig.class)
public interface BankAccountMapper {

    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    BankAccountDTO providerRequestToDto(AccountInfo request);

    AccountInfo dtoToProviderRequest(BankAccountDTO dto);

}
