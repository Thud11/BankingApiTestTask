package com.banking.mock.banking.controller;

import com.banking.mock.banking.api.MockBankingApi;
import com.banking.mock.banking.api.model.common.BaseAmount;
import com.banking.mock.banking.api.model.payment.SandboxPaymentRequest;
import com.banking.mock.banking.api.model.payment.SandboxPaymentResponse;
import com.banking.mock.banking.api.model.transaction.SandboxAccountTransaction;
import com.banking.mock.banking.services.SandboxAccountService;
import com.banking.mock.banking.services.SandboxPaymentService;
import com.banking.mock.banking.services.SandboxTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MockBankingController implements MockBankingApi {

    private final SandboxAccountService accountService;
    private final SandboxPaymentService paymentService;
    private final SandboxTransactionService transactionService;

    @Override
    public ResponseEntity<BaseAmount> getAccountBalance(String accountId) {
        return ResponseEntity.ok(accountService.getAccountBalance(accountId));
    }

    @Override
    public ResponseEntity<List<SandboxAccountTransaction>> getTransactions(String accountId, LocalDateTime from, LocalDateTime to, int limit) {
        return ResponseEntity.ok(transactionService.getTransactions(accountId, from, to, limit));
    }

    @Override
    public ResponseEntity<SandboxPaymentResponse> processPayment(SandboxPaymentRequest request) {
        return ResponseEntity.ok().body(paymentService.processPayment(request));
    }

}
