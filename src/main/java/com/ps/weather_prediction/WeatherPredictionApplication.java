package com.ps.weather_prediction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WeatherPredictionApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherPredictionApplication.class, args);
	}

}
