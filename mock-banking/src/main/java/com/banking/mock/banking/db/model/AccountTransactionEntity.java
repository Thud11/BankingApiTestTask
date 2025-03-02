package com.banking.mock.banking.db.model;


import com.banking.mock.banking.api.model.transaction.SandboxTransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "account_transactions")
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID transactionId;

    @Column(nullable = false)
    private String senderIban;

    @Column(nullable = false)
    private String recipientIban;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SandboxTransactionType type;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
