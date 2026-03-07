package com.suratgan.backend.payment.infrastructure.api;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TossApiHelper {

    private final RestClient restClient;

    @Getter
    private final String authorizationHeaderValue;

    public TossApiHelper(
            RestClient.Builder restClientBuilder,
            @Value("${toss.payments.base-url:https://api.tosspayments.com}") String baseUrl,
            @Value("${toss.payments.secret-key}") String secretKey
    ) {
        this.authorizationHeaderValue = basicAuth(secretKey);

        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, this.authorizationHeaderValue)
                .build();
    }

    public RestClient client() {
        return restClient;
    }

    private String basicAuth(String secretKey) {
        String raw = secretKey + ":";
        String encoded = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encoded;
    }
}