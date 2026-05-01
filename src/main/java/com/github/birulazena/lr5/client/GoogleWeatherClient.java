package com.github.birulazena.lr5.client;

import com.github.birulazena.lr5.client.type.WeatherProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class GoogleWeatherClient implements WeatherDataClient{

    @Qualifier("googleWeatherRestClient")
    private final RestClient restClient;

    @Override
    public BigDecimal getCurrentTemperature(BigDecimal lat, BigDecimal lon) {
        var response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", "{apiKey}")
                        .queryParam("location.latitude", lat.toString())
                        .queryParam("location.longitude", lon.toString())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new RuntimeException("googleweather returned bad status: " + res.getStatusCode());
                })
                .toEntity(String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode mainNode = rootNode.get("temperature");

            if (mainNode == null || !mainNode.has("degrees")) {
                throw new RuntimeException("failed to decode response: missing temperature data");
            }

            return new BigDecimal(mainNode.get("degrees").asText());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WeatherProviderType getProviderType() {
        return WeatherProviderType.GOOGLE_WEATHER;
    }
}
