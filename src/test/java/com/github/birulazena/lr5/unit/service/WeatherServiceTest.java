package com.github.birulazena.lr5.unit.service;

import com.github.birulazena.lr5.client.OpenWeatherClient;
import com.github.birulazena.lr5.client.WeatherClientFactory;
import com.github.birulazena.lr5.client.WeatherDataClient;
import com.github.birulazena.lr5.client.type.WeatherProviderType;
import com.github.birulazena.lr5.model.CurrentWeather;
import com.github.birulazena.lr5.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private WeatherClientFactory weatherClientFactory;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void getCurrentWeatherWithLatLon_ReturnSuccessResponse() {
        BigDecimal lat = new BigDecimal("10.0");
        BigDecimal lon = new BigDecimal("52.52");
        WeatherProviderType type = WeatherProviderType.OPEN_WEATHER;
        BigDecimal expectedTemp = new BigDecimal("25.5");

        WeatherDataClient mockClient = Mockito.mock(WeatherDataClient.class);

        Mockito.when(weatherClientFactory.getClient(type))
                .thenReturn(mockClient);

        CurrentWeather result = weatherService.getCurrentWeather(lat, lon, type);

        assertNotNull(result);
        assertEquals(expectedTemp, result.getTemperature());

        Mockito.verify(weatherClientFactory).getClient(type);
        Mockito.verify(mockClient).getCurrentTemperature(lat, lon);
    }
}
