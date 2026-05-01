package com.github.birulazena.lr5.unit.client;

import com.github.birulazena.lr5.client.OpenWeatherClient;
import com.github.birulazena.lr5.client.type.WeatherProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class OpenWeatherClientTest {

    private MockRestServiceServer mockServer;
    private OpenWeatherClient weatherClient;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder()
                .defaultUriVariables(Map.of("apiKey", "test-api-key"));

        mockServer = MockRestServiceServer.bindTo(builder).build();

        RestClient restClient = builder.build();

        weatherClient = new OpenWeatherClient(restClient);
    }

    @Test
    void getCurrentTemperature_Success_ReturnsTemperature() {
        BigDecimal lat = new BigDecimal("50.45");
        BigDecimal lon = new BigDecimal("30.52");
        String jsonResponse = "{ \"main\": { \"temp\": 22.5 } }";

        mockServer.expect(requestTo(containsString("lat=50.45")))
                .andExpect(requestTo(containsString("lon=30.52")))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        BigDecimal temperature = weatherClient.getCurrentTemperature(lat, lon);

        assertEquals(new BigDecimal("22.5"), temperature);
        mockServer.verify();
    }

    @Test
    void getCurrentTemperature_BadStatusCode_ThrowsRuntimeException() {
        BigDecimal lat = new BigDecimal("10.0");
        BigDecimal lon = new BigDecimal("20.0");

        mockServer.expect(requestTo(containsString("/weather")))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherClient.getCurrentTemperature(lat, lon);
        });

        assertEquals("openweather returned bad status: 401 UNAUTHORIZED", exception.getMessage());
        mockServer.verify();
    }

    @Test
    void getCurrentTemperature_MissingTemperatureInResponse_ThrowsRuntimeException() {
        BigDecimal lat = new BigDecimal("10.0");
        BigDecimal lon = new BigDecimal("20.0");

        String invalidJsonResponse = "{ \"main\": { \"humidity\": 80 } }";

        mockServer.expect(requestTo(containsString("/weather")))
                .andRespond(withSuccess(invalidJsonResponse, MediaType.APPLICATION_JSON));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weatherClient.getCurrentTemperature(lat, lon);
        });

        assertEquals("failed to decode response: missing temperature data", exception.getMessage());
        mockServer.verify();
    }

    @Test
    void shouldReturnOpenWeatherProviderType() {
        assertEquals(WeatherProviderType.OPEN_WEATHER, weatherClient.getProviderType());
    }

}
