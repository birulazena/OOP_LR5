package com.github.birulazena.lr5.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherRepositoryTest {

    private WeatherRepository weatherRepository;

    @BeforeEach
    void setUp() {
        weatherRepository = new WeatherRepositoryImpl();
    }

    @Test
    void findCoordinatesByCity_ShouldReturnCoordinates_WhenCityExists() {
        Optional<Coordinates> minksCoords = weatherRepository.getCoordinatesByCity("Minsk");

        assertTrue(minksCoords.isPresent());
        assertEquals(new BigDecimal("53.9006"), minksCoords.get().lat());
        assertEquals(new BigDecimal("27.5590"), minksCoords.get().lon());
    }

    @Test
    void findCoordinatesByCity_ShouldBeCaseInsensitive() {
        Optional<Coordinates> coords = weatherRepository.getCoordinatesByCity("mInSk");
        assertTrue(coords.isPresent());
    }

    @Test
    void findCoordinatesByCity_ShouldReturnEmpty_WhenCityDoesNotExist() {
        Optional<Coordinates> coords = weatherRepository.getCoordinatesByCity("Atlantis");
        assertTrue(coords.isEmpty());
    }
}
