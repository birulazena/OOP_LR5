package com.github.birulazena.lr5.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.googleweather")
public class GoogleWeatherProperties {
    private String baseUrl;
    private String apiKey;
}
