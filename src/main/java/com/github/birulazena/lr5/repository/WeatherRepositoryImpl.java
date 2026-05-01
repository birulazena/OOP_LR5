package com.github.birulazena.lr5.repository;

import com.github.birulazena.lr5.model.Coordinates;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Component
public class WeatherRepositoryImpl implements WeatherRepository {

    private static final Map<String, Coordinates> CITY_DATABASE = Map.of(
            "MINSK", new Coordinates(new BigDecimal("53.9006"), new BigDecimal("27.5590")),
            "LONDON", new Coordinates(new BigDecimal("51.5072"), new BigDecimal("-0.1276")),
            "TOKIO", new Coordinates(new BigDecimal("35.6764"), new BigDecimal("139.6500")),
            "SHANGHAI", new Coordinates(new BigDecimal("31.2304"), new BigDecimal("121.4737")),
            "WARSAW", new Coordinates(new BigDecimal("52.2297"), new BigDecimal("21.0122"))
    );

    @Override
    public Optional<Coordinates> getCoordinatesByCity(String city) {
        if (city == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(CITY_DATABASE.get(city.toUpperCase().trim()));
    }
}
