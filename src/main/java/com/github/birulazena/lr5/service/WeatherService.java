package com.github.birulazena.lr5.service;

import com.github.birulazena.lr5.client.WeatherClientFactory;
import com.github.birulazena.lr5.client.WeatherDataClient;
import com.github.birulazena.lr5.client.type.WeatherProviderType;
import com.github.birulazena.lr5.exception.CoordinatesNotFoundException;
import com.github.birulazena.lr5.model.Coordinates;
import com.github.birulazena.lr5.model.CurrentWeather;
import com.github.birulazena.lr5.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClientFactory clientFactory;

    private final WeatherRepository weatherRepository;

    public CurrentWeather getCurrentWeather(BigDecimal lat, BigDecimal lon, WeatherProviderType type) {
        BigDecimal temperature = clientFactory.getClient(type)
                .getCurrentTemperature(lat, lon);
        return new CurrentWeather(temperature);
    }

    public CurrentWeather getCurrentWeather(String city, WeatherProviderType type) {
        Coordinates coordinates = weatherRepository.getCoordinatesByCity(city).orElseThrow(
                () -> new CoordinatesNotFoundException("Coordinates for " + city + " not found"));

        BigDecimal temperature = clientFactory.getClient(type)
                .getCurrentTemperature(coordinates.lat(), coordinates.lon());

        return new CurrentWeather(temperature);
    }
}
