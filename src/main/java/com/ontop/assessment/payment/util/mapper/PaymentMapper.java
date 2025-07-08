package com.ontop.assessment.payment.util.mapper;

import com.ontop.assessment.payment.infrastructure.adapters.outbound.entity.JpaPaymentEntity;
import com.ontop.assessment.payment.domain.payment.Payment;
import com.ontop.assessment.payment.infrastructure.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", config = MapstructConfig.class)
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    JpaPaymentEntity domainToEntity(Payment request);

    Payment entityToDomain(JpaPaymentEntity entity);

}
