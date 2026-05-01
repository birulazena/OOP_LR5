package com.github.birulazena.lr5.repository;

import com.github.birulazena.lr5.model.Coordinates;

import java.util.Optional;

public interface WeatherRepository {
    Optional<Coordinates> getCoordinatesByCity(String city);
}
