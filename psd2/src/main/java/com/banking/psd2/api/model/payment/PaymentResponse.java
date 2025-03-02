package com.banking.psd2.api.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private UUID paymentId;
    private PaymentStatus status;
    private TransactionFees transactionFees;
}