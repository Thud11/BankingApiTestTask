package com.banking.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TokenProvider {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String TOKEN_URI = "/oauth2/token";
    private static final String GRANT_TYPE_KEY = "grant_type";
    private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    private static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
    private static final String AUTHORIZATION_TYPE_BASIC = "Basic ";
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_FORM_URLENCODED;

    @Value("${client.client-id}")
    private String clientId;

    @Value("${client.client-secret}")
    private String clientSecret;

    private final WebClient webClient;

    public TokenProvider(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .exchangeStrategies(ExchangeStrategies.withDefaults())
                .baseUrl(BASE_URL)
                .build();
    }

    public TokenResponse getToken() {
        return webClient.post()
                .uri(TOKEN_URI)
                .header(AUTHORIZATION_HEADER, buildBasicAuthHeader(clientId, clientSecret))
                .contentType(CONTENT_TYPE)
                .bodyValue(GRANT_TYPE_KEY + "=" + GRANT_TYPE_CLIENT_CREDENTIALS)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    private String buildBasicAuthHeader(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        return AUTHORIZATION_TYPE_BASIC + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
