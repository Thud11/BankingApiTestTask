package com.banking.mock.banking.services;

import com.banking.mock.banking.api.model.payment.SandboxPaymentRequest;
import com.banking.mock.banking.api.model.payment.SandboxPaymentResponse;
import com.banking.mock.banking.api.model.payment.SandboxTransactionStatus;
import com.banking.mock.banking.api.model.transaction.SandboxAccountTransaction;
import com.banking.mock.banking.api.model.transaction.SandboxTransactionType;
import com.banking.mock.banking.services.helpers.PaymentServiceHelper;
import com.banking.mock.banking.services.validators.PaymentRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SandboxPaymentService {

    private final SandboxAccountService accountService;
    private final SandboxTransactionService transactionService;

    private final PaymentRequestValidator paymentRequestValidator;

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.01");

    public SandboxPaymentResponse processPayment(SandboxPaymentRequest request) {
        return paymentRequestValidator.hasInvalidConditions(request)
                ? rejectedResponse()
                : acceptedResponse(request);
    }

    private SandboxPaymentResponse rejectedResponse() {
        return SandboxPaymentResponse.builder()
                .paymentId(PaymentServiceHelper.generatePaymentId())
                .status(SandboxTransactionStatus.RJCT)
                .build();
    }

    private SandboxPaymentResponse acceptedResponse(SandboxPaymentRequest request) {
        proceedPayment(request);
        return SandboxPaymentResponse.builder()
                .paymentId(PaymentServiceHelper.generatePaymentId())
                .status(SandboxTransactionStatus.ACSC)
                .transactionFees(PaymentServiceHelper.buildTransactionFees(request.getInstructedAmount().getAmount(), COMMISSION_RATE))
                .build();
    }


    private void proceedPayment(SandboxPaymentRequest request) {
        proceedTransaction(request);
        updateBalances(request);
    }

    private void proceedTransaction(SandboxPaymentRequest request) {
        SandboxAccountTransaction senderTransaction = PaymentServiceHelper.buildTransaction(request, SandboxTransactionType.WITHDRAWAL);
        SandboxAccountTransaction recipientTransaction = PaymentServiceHelper.buildTransaction(request, SandboxTransactionType.DEPOSIT);
        transactionService.saveTransactions(List.of(senderTransaction, recipientTransaction));
    }

    private void updateBalances(SandboxPaymentRequest request) {
        BigDecimal withdrawalAmount = calculateWithdrawalAmount(request);
        accountService.changeBalance(request.getDebtorAccount().getIban(), withdrawalAmount);
        accountService.changeBalance(request.getCreditorAccount().getIban(), request.getInstructedAmount().getAmount());
    }

    private BigDecimal calculateWithdrawalAmount(SandboxPaymentRequest request) {
        BigDecimal commission = request.getInstructedAmount().getAmount().multiply(COMMISSION_RATE);
        return request.getInstructedAmount().getAmount().negate().subtract(commission);
    }

}
