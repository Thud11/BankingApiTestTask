package com.banking.psd2.services.helpers;

import com.banking.psd2.api.model.payment.Payment;
import com.banking.psd2.api.model.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentClient {

    private final WebClient webClient;

    public PaymentResponse sendPayment(String url, Payment request) {
        log.debug("Sending payment request to external API: {}", url);

        Optional<PaymentResponse> paymentResponse = Optional.ofNullable(webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.error("Remote server error: {}", response.statusCode());
                    return Mono.error(new RuntimeException("Remote server encountered an error"));
                })
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    log.error("Client error when sending payment: {}", response.statusCode());
                    return Mono.error(new RuntimeException("Payment request failed"));
                })
                .bodyToMono(PaymentResponse.class)
                .block());

        if (paymentResponse.isEmpty()) {
            log.error("Received null response from payment API");
        } else {
            log.debug("Payment response received successfully: ID={}", paymentResponse.get().getPaymentId());
        }
        return paymentResponse.orElseThrow(() -> new RuntimeException("Unable to process Payment request"));
    }
}
