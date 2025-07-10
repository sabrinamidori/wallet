package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.domain.dto.SourceAccountDTO;
import com.ontop.assessment.payment.domain.payment.BankAccount;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.DestinationAccount;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.SourceAccount;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.WithdrawProviderRequest;
import com.ontop.assessment.payment.infrastructure.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", config = MapstructConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
        uses = {BankAccountMapper.class})
public interface WithdrawMapper {

    WithdrawMapper INSTANCE = Mappers.getMapper(WithdrawMapper.class);

    @Mapping(target = "name", source = "holderName")
    @Mapping(target = "account.accountNumber", source = "accountNumber")
    @Mapping(target = "account.currency", source = "currency")
    @Mapping(target = "account.routingNumber", source = "routingNumber")
    DestinationAccount dtoToProviderDestination(BankAccount dto);

    @Mapping(target = "sourceInformation.name", source = "name")
    SourceAccount dtoToProviderSource(SourceAccountDTO dto);

    default WithdrawProviderRequest toProviderRequest(SourceAccountDTO sourceAccount, Payment paymentInfo) {
        return WithdrawProviderRequest.builder()
                .destination(this.dtoToProviderDestination(paymentInfo.getDestination()))
                .amount(paymentInfo.getTotal())
                .source(this.dtoToProviderSource(sourceAccount)).build();
    }

}
