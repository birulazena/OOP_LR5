package com.github.birulazena.lr5.client;

import com.github.birulazena.lr5.client.type.WeatherProviderType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WeatherClientFactory {

    private final Map<WeatherProviderType, WeatherDataClient> clients;

    public WeatherClientFactory(List<WeatherDataClient> clients) {
        this.clients = clients.stream()
                .collect(Collectors.toMap(
                        WeatherDataClient::getProviderType,
                        client -> client
                ));
    }

    public WeatherDataClient getClient(WeatherProviderType type) {
        return Optional.ofNullable(clients.get(type)).orElseThrow(() ->
                new IllegalArgumentException("Provider is not supported"));
    }

}
