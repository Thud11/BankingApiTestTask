package com.banking.psd2.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ExternalBankingApiConfig {

    @Value("${externalBanking.config.baseUrl}")
    private String baseUrl;

    @Value("${externalBanking.config.accountBalanceUrl}")
    private String accountBalanceUrl;

    @Value("${externalBanking.config.ibanExistsUrl}")
    private String ibanExistsUrl;

    @Value("${externalBanking.config.transactionsUrl}")
    private String transactionsUrl;

    @Value("${externalBanking.config.paymentsUrl}")
    private String paymentsUrl;
}
