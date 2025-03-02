package com.banking.mock.banking.api.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SandboxAccountTransaction {

    private UUID transactionId;

    private String senderIban;

    private String recipientIban;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal amount;

    private SandboxTransactionType type;

    private String description;

    private Currency currency;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
