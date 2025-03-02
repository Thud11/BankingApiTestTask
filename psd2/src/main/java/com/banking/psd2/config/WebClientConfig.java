package com.banking.psd2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final ExternalBankingApiConfig externalBankingApiConfig;
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(externalBankingApiConfig.getBaseUrl()).build();
    }
}