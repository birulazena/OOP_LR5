package com.github.birulazena.lr5.client;

import java.math.BigDecimal;

public interface WeatherDataClient {
    BigDecimal getCurrentTemperature(BigDecimal lat, BigDecimal lon);
}
