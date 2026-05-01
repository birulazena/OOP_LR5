package com.github.birulazena.lr5.config;

import com.github.birulazena.lr5.properties.GoogleWeatherProperties;
import com.github.birulazena.lr5.properties.OpenWeatherProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class ClientsConfig {

    @Bean
    RestClient openWeatherRestClient(OpenWeatherProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultUriVariables(Map.of("apiKey", properties.getApiKey()))
                .build();
    }

    @Bean
    RestClient googleWeatherRestClient(GoogleWeatherProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultUriVariables(Map.of("apiKey", properties.getApiKey()))
                .build();
    }
}
