package com.banking.psd2.api.model.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Data
@Builder
public class AccountTransaction {


    private UUID transactionId;

    private String senderIban;

    private String recipientIban;

    private BigDecimal amount;

    private TransactionType type;

    private String description;

    private Currency currency;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
