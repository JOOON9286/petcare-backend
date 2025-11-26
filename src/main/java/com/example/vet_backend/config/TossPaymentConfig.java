package com.example.vet_backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TossPaymentConfig {

    @Value("${toss.client.key}")
    private String testClientApiKey;

    @Value("${toss.secret.key}")
    private String testSecretKey;

    @Value("${toss.url}")
    private String successUrl;
}