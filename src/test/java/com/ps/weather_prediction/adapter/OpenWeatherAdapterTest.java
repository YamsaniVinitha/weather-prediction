package com.ps.weather_prediction.adapter;

import com.ps.weather_prediction.configuration.OpenWeatherApiConfiguration;
import com.ps.weather_prediction.exception.CityNotFoundException;
import com.ps.weather_prediction.model.dto.WeatherApiResponse;
import com.ps.weather_prediction.service.WeatherCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenWeatherAdapterTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private OpenWeatherApiConfiguration apiConfig;
    @Mock
    private WeatherCacheService weatherCacheService;
    @InjectMocks
    private OpenWeatherAdapter adapter;

    private String expectedUrl;

    @BeforeEach
    void setUp() {
        when(apiConfig.getUrl()).thenReturn("https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&cnt=%s");
        when(apiConfig.getApiKey()).thenReturn("dummyKey");
        when(apiConfig.getCnt()).thenReturn("3");

        expectedUrl = String.format(apiConfig.getUrl(), "Paris", apiConfig.getApiKey(), apiConfig.getCnt());
    }

    @Test
    void testGetWeatherForecast_Success() {
        var mockWeatherApiResponse = WeatherApiResponse.builder().build();
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class))).thenReturn(mockWeatherApiResponse);

        var result = adapter.getWeatherForecast("Paris");

        assertNotNull(result);
        assertEquals(result, mockWeatherApiResponse);
        verify(restTemplate, times(1)).getForObject(expectedUrl, WeatherApiResponse.class);
        verify(weatherCacheService, never()).getCachedWeatherPrediction(anyString());
    }

    @Test
    void testGetWeatherForecast_CityNotFound() {
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class)))
                .thenThrow(new RuntimeException("404 City Not Found"));

        var exception = assertThrows(CityNotFoundException.class, () -> adapter.getWeatherForecast("UnknownCity"));
        assertEquals("City not found: UnknownCity", exception.getMessage());

        verify(restTemplate, times(1)).getForObject(expectedUrl.replace("Paris", "UnknownCity"), WeatherApiResponse.class);
        verify(weatherCacheService, never()).getCachedWeatherPrediction(anyString());
    }

    @Test
    void testGetWeatherForecast_FallbackToCache() {
        var cachedResponse = WeatherApiResponse.builder().build();
        when(restTemplate.getForObject(anyString(), eq(WeatherApiResponse.class)))
                .thenThrow(new RuntimeException("500 Internal Server Error"));
        when(weatherCacheService.getCachedWeatherPrediction("Berlin")).thenReturn(cachedResponse);

        var result = adapter.getWeatherForecast("Berlin");

        assertEquals(result, cachedResponse);
        verify(restTemplate, times(1)).getForObject(expectedUrl.replace("Paris", "Berlin"), WeatherApiResponse.class);
        verify(weatherCacheService, times(1)).getCachedWeatherPrediction("Berlin");
    }
}