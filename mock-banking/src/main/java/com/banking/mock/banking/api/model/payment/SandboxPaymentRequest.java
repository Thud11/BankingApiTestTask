package com.banking.mock.banking.api.model.payment;

import com.banking.validation.IBAN;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;


@Data
@Builder
public class SandboxPaymentRequest {

    @NotNull(message = "Creditor name cannot be null")
    @NotBlank(message = "Creditor name cannot be empty")
    private String creditorName;

    private AccountReference debtorAccount;
    private AccountReference creditorAccount;
    private InstructedAmount instructedAmount;

    @Data
    @Builder
    public static class AccountReference {
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


