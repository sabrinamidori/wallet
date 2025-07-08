package com.ontop.assessment.payment.infrastructure.adapters.outbound.entity;

import com.ontop.assessment.payment.infrastructure.adapters.outbound.JpaAuditableEntity;
import com.ontop.assessment.payment.domain.payment.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Table(name = "payment")
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JpaPaymentEntity extends JpaAuditableEntity {
    @Id
    @SequenceGenerator(
            name = "payment_seq",
            sequenceName = "payment_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "payment_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;
    private Long userId;

    @NotNull
    @Embedded
    private JpaDestinationAccount destination;

    @NotNull
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "fee_amount", nullable = false)
    private BigDecimal feeAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PROCESSING;

    @Column(name = "withdraw_id")
    private Long withdrawId;

    @Column(name = "refund_id")
    private Long refundId;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "fail_reason")
    private String failReason;

}
