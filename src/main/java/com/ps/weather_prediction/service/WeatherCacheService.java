package com.ps.weather_prediction.service;

import com.ps.weather_prediction.adapter.OpenWeatherAdapter;
import com.ps.weather_prediction.model.dto.WeatherApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherCacheService {

    private final OpenWeatherAdapter openWeatherAdapter;

    @Cacheable(key = "#cityName", value = "WEATHERS")
    public WeatherApiResponse getCachedWeatherPrediction(String cityName) {
        return null;
    }

}
