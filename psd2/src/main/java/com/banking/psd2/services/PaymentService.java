package com.banking.psd2.services;

import com.banking.exception.InsufficientFundsException;
import com.banking.psd2.api.model.account.BalanceAmount;
import com.banking.psd2.api.model.payment.Payment;
import com.banking.psd2.api.model.payment.PaymentResponse;
import com.banking.psd2.api.model.payment.PaymentStatus;
import com.banking.psd2.config.ExternalBankingApiConfig;
import com.banking.psd2.db.services.PaymentRepoService;
import com.banking.psd2.services.helpers.PaymentClient;
import com.banking.psd2.services.helpers.UrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for processing payment operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final AccountService accountService;
    private final PaymentRepoService paymentRepoService;

    private final PaymentClient paymentClient;

    private final ExternalBankingApiConfig externalBankingApiConfig;



    public PaymentResponse processPayment(Payment request) {
        log.debug("Processing payment from account {} to account {}, amount: {}",
                request.getDebtorAccount().getIban(),
                request.getCreditorAccount().getIban(),
                request.getInstructedAmount().getAmount());

        String url = UrlBuilder.buildUrl(externalBankingApiConfig.getBaseUrl(), externalBankingApiConfig.getPaymentsUrl(), null, null);
        log.debug("Payment endpoint URL: {}", url);

        if (!checkSufficientFunds(request)) {
            log.error("Insufficient funds for payment from account {}", request.getDebtorAccount().getIban());
            throw new InsufficientFundsException("Insufficient funds for payment");
        }

        Long id = registerPayment(request);
        log.debug("Payment registered with internal ID: {}", id);

        PaymentResponse paymentResponse = paymentClient.sendPayment(url, request);
        log.debug("Payment request sent successfully, external payment ID: {}, status: {}",
                paymentResponse.getPaymentId(), paymentResponse.getStatus());

        return updatePaymentStatus(paymentResponse, id);
    }

    private boolean checkSufficientFunds(Payment request) {
        String debtorIban = request.getDebtorAccount().getIban();
        BigDecimal amount = request.getInstructedAmount().getAmount();
        BalanceAmount balance = accountService.getAccountBalance(debtorIban);

        boolean isSufficient = balance.getAmount().compareTo(amount) >= 0;
        log.debug("Funds check for account {}: balance = {}, required = {}, sufficient = {}",
                debtorIban, balance.getAmount(), amount, isSufficient);

        return isSufficient;
    }

    private Long registerPayment(Payment request) {
        request.setStatus(PaymentStatus.PDNG);
        Long id = paymentRepoService.savePayment(request);
        log.debug("Payment registered with status PENDING, internal ID: {}", id);
        return id;
    }



    private PaymentResponse updatePaymentStatus(PaymentResponse paymentResponse, Long id) {
        log.debug("Updating payment status for ID: {}, external ID: {}, status: {}",
                id, paymentResponse.getPaymentId(), paymentResponse.getStatus());

        paymentRepoService.updatePaymentStatus(id, paymentResponse.getPaymentId(), paymentResponse.getStatus());
        log.debug("Payment status updated successfully");
        return paymentResponse;
    }
}