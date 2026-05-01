package com.github.birulazena.lr5.unit.service;

import com.github.birulazena.lr5.client.OpenWeatherClient;
import com.github.birulazena.lr5.client.WeatherClientFactory;
import com.github.birulazena.lr5.client.WeatherDataClient;
import com.github.birulazena.lr5.client.type.WeatherProviderType;
import com.github.birulazena.lr5.model.Coordinates;
import com.github.birulazena.lr5.model.CurrentWeather;
import com.github.birulazena.lr5.repository.WeatherRepository;
import com.github.birulazena.lr5.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private WeatherClientFactory weatherClientFactory;

    @Mock
    private WeatherRepository weatherRepository;

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

        Mockito.when(mockClient.getCurrentTemperature(lat, lon))
                .thenReturn(expectedTemp);

        CurrentWeather result = weatherService.getCurrentWeather(lat, lon, type);

        assertNotNull(result);
        assertEquals(expectedTemp, result.getTemperature());

        Mockito.verify(weatherClientFactory).getClient(type);
        Mockito.verify(mockClient).getCurrentTemperature(lat, lon);
    }

    @Test
    void getCurrentWeatherWithCity_ReturnsSuccessResponse() {
        String city = "Minsk";
        WeatherProviderType type = WeatherProviderType.GOOGLE_WEATHER;
        Coordinates coordinates = new Coordinates(new BigDecimal("10.0"), new BigDecimal("20.0"));
        BigDecimal expectedTemp = new BigDecimal("25.5");

        WeatherDataClient mockClient = Mockito.mock(WeatherDataClient.class);

        Mockito.when(weatherRepository.getCoordinatesByCity(city))
                        .thenReturn(Optional.of(coordinates));

        Mockito.when(weatherClientFactory.getClient(type))
                .thenReturn(mockClient);

        Mockito.when(mockClient.getCurrentTemperature(coordinates.lat(), coordinates.lon()))
                .thenReturn(expectedTemp);

        CurrentWeather result = weatherService.getCurrentWeather(city, type);

        assertNotNull(result);
        assertEquals(result, expectedTemp);
    }

    @Test
    void getCurrentWeatherWithCity_ReturnsCoordinatesErrorResponse() {
        String city = "Mogilev";
        WeatherProviderType type = WeatherProviderType.GOOGLE_WEATHER;

        Mockito.when(weatherRepository.getCoordinatesByCity(city))
                .thenReturn(Optional.empty());

        CoordinatesNotFoundException exception = assertThrows(CoordinatesNotFoundException.class, () -> {
            weatherService.getCurrentWeather(city, type);
        });

        Mockito.verifyNoInteractions(weatherClientFactory);
    }
}
