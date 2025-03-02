package com.banking.psd2.controller;


import com.banking.psd2.api.BankingApi;
import com.banking.psd2.api.model.account.BalanceAmount;
import com.banking.psd2.api.model.payment.Payment;
import com.banking.psd2.api.model.payment.PaymentResponse;
import com.banking.psd2.api.model.transaction.AccountTransaction;
import com.banking.psd2.services.AccountService;
import com.banking.psd2.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BankingController implements BankingApi {

    private final AccountService accountService;
    private final PaymentService paymentService;

    @Override
    public ResponseEntity<BalanceAmount> getAccountBalance(String accountId) {
        return ResponseEntity.ok(accountService.getAccountBalance(accountId));
    }

    @Override
    public ResponseEntity<List<AccountTransaction>> getLastTenTransactions(String accountId) {
        return ResponseEntity.ok(accountService.getLast10Transactions(accountId));
    }


    @Override
    public ResponseEntity<PaymentResponse> processPayment(Payment request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }
}
