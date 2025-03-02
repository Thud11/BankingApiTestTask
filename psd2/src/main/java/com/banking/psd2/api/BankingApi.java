package com.banking.psd2.api;


import com.banking.psd2.api.model.account.BalanceAmount;
import com.banking.psd2.api.model.payment.Payment;
import com.banking.psd2.api.model.payment.PaymentResponse;
import com.banking.psd2.api.model.transaction.AccountTransaction;
import com.banking.validation.IBAN;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequestMapping("/api")
public interface BankingApi {

    @GetMapping(value = "/accounts/{accountId}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BalanceAmount> getAccountBalance(@PathVariable @IBAN String accountId);

    @GetMapping(value = "/accounts/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AccountTransaction>> getLastTenTransactions(@PathVariable @IBAN String accountId);

    @PostMapping(value = "/payments/initiate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PaymentResponse> processPayment(@RequestBody @Valid Payment payment);
}
