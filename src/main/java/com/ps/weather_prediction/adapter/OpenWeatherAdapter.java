package com.ps.weather_prediction.adapter;

import com.ps.weather_prediction.configuration.OpenWeatherApiConfiguration;
import com.ps.weather_prediction.exception.CityNotFoundException;
import com.ps.weather_prediction.model.dto.WeatherApiResponse;
import com.ps.weather_prediction.service.WeatherCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherAdapter {

    private final RestTemplate restTemplate;
    private final OpenWeatherApiConfiguration apiConfig;
    private final WeatherCacheService weatherCacheService;

    public WeatherApiResponse getWeatherForecast(String cityName) {
        String url = String.format(apiConfig.getUrl(), cityName, apiConfig.getApiKey(), apiConfig.getCnt());
        try {
            log.info("Fetching weather data from OpenWeather API for city: {}", cityName);
            return restTemplate.getForObject(url, WeatherApiResponse.class);
        } catch (Exception e) {
            log.error("Error fetching weather data from OpenWeather API: {}", e.getMessage());
            if(e.getMessage().contains("404")) {
                throw new CityNotFoundException("City not found: " + cityName);
            }
            return weatherCacheService.getCachedWeatherPrediction(cityName);
        }
    }
}
