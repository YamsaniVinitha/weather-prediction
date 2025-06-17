package com.ps.weather_prediction.model.enums;

import lombok.Getter;

@Getter
public enum WeatherInstruction {
    RAIN("Carry an umbrella."),
    THUNDERSTORM("Don’t step out! A Storm is brewing!"),
    SUNNY("Use sunscreen lotion."),
    WINDY("It’s too windy, watch out!");

    private final String description;

    WeatherInstruction(String description) {
        this.description = description;
    }

}
