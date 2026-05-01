package com.github.birulazena.lr5.client;

import com.github.birulazena.lr5.client.type.WeatherProviderType;

import java.math.BigDecimal;

public interface WeatherDataClient {
    BigDecimal getCurrentTemperature(BigDecimal lat, BigDecimal lon);

    WeatherProviderType getProviderType();
}
