package com.ps.weather_prediction.service;

import com.ps.weather_prediction.adapter.OpenWeatherAdapter;
import com.ps.weather_prediction.model.dto.WeatherApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LiveWeatherService {

    private final OpenWeatherAdapter openWeatherAdapter;

    @CachePut(key = "#cityName", value = "WEATHERS")
    public WeatherApiResponse getAndCacheWeatherPrediction(String cityName) {
        return openWeatherAdapter.getWeatherForecast(cityName);
    }
}
