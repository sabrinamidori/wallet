package com.ontop.assessment.payment.infrastructure.adapters.outbound.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class JpaDestinationAccount implements Serializable {
    @NotNull
    @Column(name = "account_id", columnDefinition = "bigint")
    private Long accountId;

    @NotNull
    @Column(name = "account_holder_name", nullable = false)
    private String holderName;

    @NotNull
    @Column(name = "routing_number", nullable = false)
    private String routingNumber;

    @NotNull
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @NotNull
    @Column(name = "account_currency", nullable = false)
    private String currency;
}
