package com.ps.weather_prediction.model.dto;

import com.ps.weather_prediction.model.enums.Mode;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ApplicationMode {
    private Mode mode = Mode.ONLINE;
}
