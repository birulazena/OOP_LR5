package com.github.birulazena.lr5.unit.controller;

import com.github.birulazena.lr5.client.type.WeatherProviderType;
import com.github.birulazena.lr5.controller.WeatherController;
import com.github.birulazena.lr5.dto.StatusResponse;
import com.github.birulazena.lr5.dto.SuccessResponse;
import com.github.birulazena.lr5.model.CurrentWeather;
import com.github.birulazena.lr5.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @Test
    void getCurrentWeather_ReturnsSuccessResponse() {
        BigDecimal lat = new BigDecimal("53.9006");
        BigDecimal lon = new BigDecimal("27.5590");
        CurrentWeather fakeWeather = new CurrentWeather();

        when(weatherService.getCurrentWeather(lat, lon, WeatherProviderType.OPEN_WEATHER)).thenReturn(fakeWeather);

        SuccessResponse<CurrentWeather> response = weatherController.getCurrentWeather(lat, lon, WeatherProviderType.OPEN_WEATHER);

        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(fakeWeather, response.getData());
    }

    @Test
    void handleArgumentTypeException_Returns400() {
        MethodArgumentTypeMismatchException mockException = mock(MethodArgumentTypeMismatchException.class);

        StatusResponse response = weatherController.handleArgumantTypeException(mockException);

        assertEquals(400, response.getCode());
        assertEquals("invalid coordinates", response.getMessage());
    }

    @Test
    void handleException_Returns500AndErrorMessage() {
        Exception exception = new RuntimeException("Database timeout");

        StatusResponse response = weatherController.handleException(exception);

        assertEquals(500, response.getCode());
        assertEquals("Database timeout", response.getMessage());
    }
}
