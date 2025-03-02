package com.banking.mock.banking.services.helpers;

import com.banking.mock.banking.api.model.common.BaseAmount;
import com.banking.mock.banking.api.model.payment.SandboxPaymentRequest;
import com.banking.mock.banking.api.model.transaction.SandboxAccountTransaction;
import com.banking.mock.banking.api.model.transaction.SandboxTransactionType;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public class PaymentServiceHelper {

    private PaymentServiceHelper() {}

    public static SandboxAccountTransaction buildTransaction(SandboxPaymentRequest paymentRequest, SandboxTransactionType transactionType) {
        return SandboxAccountTransaction.builder()
                .transactionId(UUID.randomUUID())
                .senderIban(paymentRequest.getDebtorAccount().getIban())
                .recipientIban(paymentRequest.getCreditorAccount().getIban())
                .currency(paymentRequest.getInstructedAmount().getCurrency())
                .amount(paymentRequest.getInstructedAmount().getAmount())
                .type(transactionType)
                .build();

    }

    public static BaseAmount buildTransactionFees(BigDecimal amount, BigDecimal commissionRate) {
        return BaseAmount.builder()
                .amount(amount.multiply(commissionRate))
                .currency(Currency.getInstance("EUR"))
                .build();
    }

    public static UUID generatePaymentId() {
        return UUID.randomUUID();
    }
}
