package com.banking.psd2.integrations.helpers;


import com.banking.psd2.api.model.account.BalanceAmount;
import com.banking.psd2.api.model.payment.Payment;

import java.math.BigDecimal;
import java.util.Currency;

public class TestDataHelper {

    private static final String CURRENCY = "EUR";


    public static Payment createPaymentRequest(String debtorIban, String creditorIban, BigDecimal amount) {
        return Payment.builder()
                .creditorName("creditorName")
                .creditorAccount(Payment.ParticipantAccount.builder()
                        .iban(creditorIban)
                        .currency(Currency.getInstance(CURRENCY))
                        .build())
                .debtorAccount(Payment.ParticipantAccount.builder()
                        .iban(debtorIban)
                        .currency(Currency.getInstance(CURRENCY))
                        .build())
                .instructedAmount(Payment.InstructedAmount.builder()
                        .amount(amount)
                        .currency(Currency.getInstance(CURRENCY))
                        .build())
                .build();
    }

    public static BalanceAmount createBalanceAmount(BigDecimal amount) {
        return BalanceAmount.builder()
                .amount(amount)
                .currency(Currency.getInstance(CURRENCY))
                .build();
    }

}
