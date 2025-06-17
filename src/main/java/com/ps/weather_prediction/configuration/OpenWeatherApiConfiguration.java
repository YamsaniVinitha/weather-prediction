package com.ps.weather_prediction.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "open-weather-api")
@Configuration
@Data
public class OpenWeatherApiConfiguration {
    private String url;
    private String apiKey;
    private String cnt;
}
