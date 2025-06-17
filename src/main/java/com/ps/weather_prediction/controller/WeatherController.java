package com.ps.weather_prediction.controller;

import com.ps.weather_prediction.model.response.WeatherResponse;
import com.ps.weather_prediction.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(summary = "Get 3-day forecast with weather advisories",
            description = "Returns high/low temperatures and advice like 'Carry umbrella', 'Use sunscreen', etc.")
    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getWeatherPrediction(
            @Parameter(description = "City name (e.g., London)")
            @NonNull @RequestParam String city) {
        log.info("Fetching weather prediction for city: {}", city);
        return ResponseEntity.ok(weatherService.getWeatherPrediction(city));
    }
}
