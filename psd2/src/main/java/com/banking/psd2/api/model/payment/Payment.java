package com.banking.psd2.api.model.payment;

import com.banking.validation.IBAN;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;


@Data
@Builder
public class Payment {

    @NotNull(message = "Creditor name cannot be null")
    @NotBlank(message = "Creditor name cannot be empty")
    private String creditorName;

    @NotNull(message = "Debtor account cannot be null")
    private ParticipantAccount debtorAccount;

    @NotNull(message = "Creditor account cannot be null")
    private ParticipantAccount creditorAccount;

    @NotNull(message = "Instructed amount cannot be null")
    private InstructedAmount instructedAmount;

    private PaymentStatus status;

    private UUID paymentId;

    @Data
    @Builder
    public static class ParticipantAccount {
        @IBAN
        @NotNull(message = "Debtor IBAN cannot be null")
        private String iban;

        @NotNull(message = "Currency cannot be null")
        private Currency currency;
    }

    @Data
    @Builder
    public static class InstructedAmount {

        @NotNull(message = "Currency cannot be null")
        private Currency currency;

        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        private BigDecimal amount;
    }

}


