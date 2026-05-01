package com.github.birulazena.lr5.service;

import com.github.birulazena.lr5.client.WeatherClientFactory;
import com.github.birulazena.lr5.client.WeatherDataClient;
import com.github.birulazena.lr5.client.type.WeatherProviderType;
import com.github.birulazena.lr5.model.CurrentWeather;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClientFactory clientFactory;

    public CurrentWeather getCurrentWeather(BigDecimal lat, BigDecimal lon, WeatherProviderType type) {
        BigDecimal temperature = clientFactory.getClient(type)
                .getCurrentTemperature(lat, lon);
        return new CurrentWeather(temperature);
    }
}
