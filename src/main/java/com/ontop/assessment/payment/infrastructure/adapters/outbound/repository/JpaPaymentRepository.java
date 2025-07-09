package com.ontop.assessment.payment.infrastructure.adapters.outbound.repository;

import com.ontop.assessment.payment.infrastructure.adapters.outbound.entity.JpaPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<JpaPaymentEntity, Long> {

}
