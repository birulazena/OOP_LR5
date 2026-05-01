package com.github.birulazena.lr5.controller;

import com.github.birulazena.lr5.dto.StatusResponse;
import com.github.birulazena.lr5.dto.SuccessResponse;
import com.github.birulazena.lr5.model.CurrentWeather;
import com.github.birulazena.lr5.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "weather", description = "Weather API")
public class WeatherController {
    private final WeatherService service;

    @GetMapping("/weather")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Current Weather", description = "Returns current weather for given coordinates")
    public SuccessResponse<CurrentWeather> getCurrentWeather(
            @Parameter(description = "Latitude", required = true, example = "53.9006") @RequestParam BigDecimal lat,
            @Parameter(description = "Longitude", required = true, example = "27.5590") @RequestParam BigDecimal lon) {
        CurrentWeather result = service.getCurrentWeather(lat, lon);
        return new SuccessResponse<>(200, "Success", result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public StatusResponse handleArgumantTypeException(Exception exception) {
        return new StatusResponse(400, "invalid coordinates");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public StatusResponse handleException(Exception exception) {
        return new StatusResponse(500, exception.getMessage());
    }
}
