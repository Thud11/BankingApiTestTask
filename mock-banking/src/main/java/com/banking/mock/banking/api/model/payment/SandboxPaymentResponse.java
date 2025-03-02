package com.banking.mock.banking.api.model.payment;

import com.banking.mock.banking.api.model.common.BaseAmount;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SandboxPaymentResponse {
    private UUID paymentId;
    private SandboxTransactionStatus status;
    private BaseAmount transactionFees;
}