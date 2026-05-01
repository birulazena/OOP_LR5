package com.github.birulazena.lr5.client;

import com.github.birulazena.lr5.client.type.WeatherProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Component
public class OpenWeatherClient implements WeatherDataClient {

    private final RestClient restClient;

    public OpenWeatherClient(@Qualifier("openWeatherRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public BigDecimal getCurrentTemperature(BigDecimal lat, BigDecimal lon) {
        var response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/weather")
                        .queryParam("appid", "{apiKey}")
                        .queryParam("lat", lat.toString())
                        .queryParam("lon", lon.toString())
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new RuntimeException("openweather returned bad status: " + res.getStatusCode());
                })
                .toEntity(String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode mainNode = rootNode.get("main");

            if (mainNode == null || !mainNode.has("temp")) {
                throw new RuntimeException("failed to decode response: missing temperature data");
            }

            return new BigDecimal(mainNode.get("temp").asText());
        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WeatherProviderType getProviderType() {
        return WeatherProviderType.OPEN_WEATHER;
    }
}
