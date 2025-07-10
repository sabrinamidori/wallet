package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.domain.dto.PaymentResponseDTO;
import com.ontop.assessment.payment.domain.payment.PaymentStatus;
import com.ontop.assessment.payment.infrastructure.adapters.outbound.payment.ProviderResponse;
import com.ontop.assessment.payment.infrastructure.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", config = MapstructConfig.class)
public interface PaymentResponseMapper {

    PaymentResponseMapper INSTANCE = Mappers.getMapper(PaymentResponseMapper.class);

    @Mapping(source = "paymentInfo.id", target = "paymentId")
    @Mapping(source = "paymentInfo.amount", target = "paymentAmount")
    @Mapping(source = "requestInfo.status", target = "status",
            qualifiedByName = "statusStringToEnum")
    @Mapping(source = "requestInfo.error", target = "error")
    PaymentResponseDTO providerResponseToResponseDTO(ProviderResponse providerResponse);

    @Named("statusStringToEnum")
    static PaymentStatus statusStringToEnum(String status) {
        if (status == null) return null;
        return PaymentStatus.fromString(status);
    }

}
