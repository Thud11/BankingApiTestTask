package com.banking.mock.banking.api;

import com.banking.mock.banking.api.model.common.BaseAmount;
import com.banking.mock.banking.api.model.payment.SandboxPaymentRequest;
import com.banking.mock.banking.api.model.payment.SandboxPaymentResponse;
import com.banking.mock.banking.api.model.transaction.SandboxAccountTransaction;
import com.banking.validation.IBAN;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequestMapping("/mock")
public interface MockBankingApi {

    @GetMapping(value = "/accounts/{accountId}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BaseAmount> getAccountBalance(@PathVariable @IBAN String accountId);

    @GetMapping(value = "/accounts/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<SandboxAccountTransaction>> getTransactions(
            @PathVariable @IBAN String accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "10") int limit

    );

    @PostMapping(value = "/payments/initiate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SandboxPaymentResponse> processPayment(@RequestBody @Valid SandboxPaymentRequest paymentRequest);
}
