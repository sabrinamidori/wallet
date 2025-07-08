package com.ontop.assessment.payment.infrastructure.adapters.outbound.repository;

import com.ontop.assessment.payment.infrastructure.adapters.outbound.entity.JpaPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaPaymentRepository extends JpaRepository<JpaPaymentEntity, Long> {

    @Modifying
    @Query(value = "update coach_profiles_sports " +
            "set status =?1, updated_at = now() " +
            "where id = ?2", nativeQuery = true)
    void updateStatusById(String status, Long paymentId);
}
