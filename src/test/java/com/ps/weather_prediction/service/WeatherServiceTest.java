package com.ps.weather_prediction.service;

import com.ps.weather_prediction.exception.CityNotFoundException;
import com.ps.weather_prediction.exception.CityWeatherDataUnavailableException;
import com.ps.weather_prediction.model.dto.ApplicationMode;
import com.ps.weather_prediction.model.dto.WeatherApiResponse;
import com.ps.weather_prediction.model.enums.Mode;
import com.ps.weather_prediction.model.response.WeatherDetails;
import com.ps.weather_prediction.model.response.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @InjectMocks
    WeatherService weatherService;

    @Mock
    LiveWeatherService liveWeatherService;

    @Mock
    WeatherCacheService weatherCacheService;

    @Mock
    ApplicationMode applicationMode;

    String cityName = "Paris";

    @BeforeEach
    void setUp() throws Exception {
        Field daysField = WeatherService.class.getDeclaredField("days");
        daysField.setAccessible(true);
        daysField.set(weatherService, 1);

        when(applicationMode.getMode()).thenReturn(Mode.ONLINE);
    }

    @Test
    void getWeatherPrediction_shouldReturnWeatherResponse_whenCityNameIsValid() {
        // Given
        when(liveWeatherService.getAndCacheWeatherPrediction(cityName)).thenReturn(aWeatherApiResponse(cityName));

        // When
        var actual = weatherService.getWeatherPrediction(cityName);
        var expected = aWeatherResponse(cityName);

        // Then
        assertEquals(expected, actual);
        verify(liveWeatherService, times(1)).getAndCacheWeatherPrediction(cityName);
        verify(weatherCacheService, never()).getCachedWeatherPrediction(cityName);
    }

    @Test
    void getWeatherPrediction_shouldReturnCachedWeatherResponse_whenModeIsNotLive() throws IllegalAccessException {
        // Given
        when(applicationMode.getMode()).thenReturn(Mode.OFFLINE);
        when(weatherCacheService.getCachedWeatherPrediction(cityName)).thenReturn(aWeatherApiResponse(cityName));

        // When
        var actual = weatherService.getWeatherPrediction(cityName);
        var expected = aWeatherResponse(cityName);

        // Then
        assertEquals(expected, actual);
        verify(weatherCacheService, times(1)).getCachedWeatherPrediction(cityName);
        verify(liveWeatherService, never()).getAndCacheWeatherPrediction(cityName);
    }

    @Test
    void getWeatherPrediction_shouldThrowException_whenCityWeatherDataIsUnavailable() throws IllegalAccessException {
        // Given
        when(applicationMode.getMode()).thenReturn(Mode.OFFLINE);
        when(weatherCacheService.getCachedWeatherPrediction(cityName)).thenReturn(null);

        // When & Then
        var exception = assertThrows(CityWeatherDataUnavailableException.class, () -> weatherService.getWeatherPrediction(cityName));
        assertEquals("Weather data unavailable for city: Paris. Please try again later.", exception.getMessage());
        verify(weatherCacheService, times(1)).getCachedWeatherPrediction(cityName);
        verify(liveWeatherService, never()).getAndCacheWeatherPrediction(cityName);
    }

    @Test
    void getWeatherPrediction_shouldThrowException_whenCityNameIsInvalid() {
        // Given
        String invalidCityName = "UnknownCity";
        when(liveWeatherService.getAndCacheWeatherPrediction(invalidCityName)).thenThrow(new CityNotFoundException("City not found: " + invalidCityName));

        // When & Then
        var exception = assertThrows(CityNotFoundException.class, () -> weatherService.getWeatherPrediction(invalidCityName));
        assertEquals("City not found: UnknownCity", exception.getMessage());
        verify(liveWeatherService, times(1)).getAndCacheWeatherPrediction(invalidCityName);
        verify(weatherCacheService, never()).getCachedWeatherPrediction(invalidCityName);
    }

    private WeatherResponse aWeatherResponse(String cityName) {

        return WeatherResponse.builder()
                .city(cityName)
                .weatherDetails(List.of(
                        WeatherDetails.builder()
                                .date("2023-10-01")
                                .temperature(WeatherDetails.Temperature.builder()
                                        .max(300)
                                        .min(290)
                                        .build())
                                .instruction("Carry umbrella")
                                .build()
                ))
                .build();
    }

    private WeatherApiResponse aWeatherApiResponse(String cityName) {
        var weatherData1 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(294.8).build())
                .rain(WeatherApiResponse.WeatherData.Rain.builder().threeHourRain(0.6).build())
                .build();
        var weatherData2 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(295.0).build())
                .build();
        var weatherData3 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(300).build())
                .build();
        var weatherData4 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(290).build())
                .build();
        var weatherData5 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(291).build())
                .build();
        var weatherData6 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(292).build())
                .build();
        var weatherData7 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(293).build())
                .build();
        var weatherData8 = WeatherApiResponse.WeatherData.builder()
                .dtTxt("2023-10-01")
                .main(WeatherApiResponse.WeatherData.Main.builder().temp(296).build())
                .build();
        var weatherDataList = List.of(weatherData1, weatherData2, weatherData3, weatherData4, weatherData5, weatherData6, weatherData7, weatherData8);

        return WeatherApiResponse.builder()
                .city(WeatherApiResponse.City.builder().name(cityName).build())
                .list(weatherDataList)
                .build();
    }
}