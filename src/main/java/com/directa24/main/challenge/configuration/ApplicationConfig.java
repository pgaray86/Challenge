package com.directa24.main.challenge.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ApplicationConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(10))  // Espera para establecer conexión
                .setReadTimeout(Duration.ofSeconds(10))     // Espera para leer datos
                .build();
    }
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
