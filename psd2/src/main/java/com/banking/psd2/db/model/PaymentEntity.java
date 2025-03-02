package com.banking.psd2.db.model;

import com.banking.psd2.api.model.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Table(name = "payments")
@Entity
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private UUID paymentId;

    private String currency;

    @Column(nullable = false)
    private String senderIban;

    @Column(nullable = false)
    private String recipientIban;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
