package com.banking.psd2.services;

import com.banking.exception.AccountNotFoundException;
import com.banking.psd2.api.model.account.BalanceAmount;
import com.banking.psd2.api.model.transaction.AccountTransaction;
import com.banking.psd2.config.ExternalBankingApiConfig;
import com.banking.psd2.services.helpers.UrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final WebClient webClient;
    private final ExternalBankingApiConfig externalBankingApiConfig;

    private static final int TRANSACTION_SIZE_LIMIT = 10;

    public BalanceAmount getAccountBalance(String accountId) {
        String url = UrlBuilder.buildUrl(externalBankingApiConfig.getBaseUrl(), externalBankingApiConfig.getAccountBalanceUrl(), accountId, null);
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new AccountNotFoundException("Account not found")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server error occurred")))
                .bodyToMono(BalanceAmount.class)
                .block();
    }

    public boolean ibanExists(String iban) {
        String url = UrlBuilder.buildUrl(externalBankingApiConfig.getBaseUrl(), externalBankingApiConfig.getIbanExistsUrl(), iban, null);
        return Boolean.TRUE.equals(webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn(true)
                .onErrorReturn(false)
                .block());
    }


    public List<AccountTransaction> getLast10Transactions(String accountId) {
        String url = UrlBuilder.buildUrl(externalBankingApiConfig.getBaseUrl(), externalBankingApiConfig.getTransactionsUrl(), accountId, TRANSACTION_SIZE_LIMIT);
        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server error occurred")))
                .bodyToFlux(AccountTransaction.class)
                .collectList()
                .block();
    }
}
