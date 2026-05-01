package com.github.birulazena.lr5.unit.client;

import com.github.birulazena.lr5.client.WeatherClientFactory;
import com.github.birulazena.lr5.client.WeatherDataClient;
import com.github.birulazena.lr5.client.type.WeatherProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WeatherClientFactoryTest {

    @Mock
    private WeatherDataClient mockOpenWeather;

    private WeatherClientFactory weatherClientFactory;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(mockOpenWeather.getProviderType()).thenReturn(WeatherProviderType.OPEN_WEATHER);

        weatherClientFactory = new WeatherClientFactory(List.of(mockOpenWeather));
    }

    @Test
    void shouldReturnCorrectClientByProviderType() {
        WeatherDataClient result = weatherClientFactory.getClient(WeatherProviderType.OPEN_WEATHER);

        assertNotNull(result);
        assertEquals(mockOpenWeather, result);
    }

    @Test
    void shouldThrowExceptionWhenProviderNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            weatherClientFactory.getClient(WeatherProviderType.GOOGLE_WEATHER);
        });

        assertTrue(exception.getMessage().contains("Provider is not supported"));
    }
}
