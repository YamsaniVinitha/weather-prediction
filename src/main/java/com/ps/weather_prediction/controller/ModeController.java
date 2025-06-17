package com.ps.weather_prediction.controller;

import com.ps.weather_prediction.model.dto.ApplicationMode;
import com.ps.weather_prediction.model.enums.Mode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class ModeController {

    private final ApplicationMode applicationMode;

    @Operation(summary = "Toggle the application mode",
            description = "Sets the application mode to either ONLINE or OFFLINE." +
                    "ONLINE mode allows fetching live weather data, while OFFLINE mode uses cached data.")
    @PostMapping("/mode/{modeName}")
    public ResponseEntity<String> setMode(@Parameter(description = "Mode(Ex: OFFLINE or ONLINE)")
                                            @NonNull @PathVariable("modeName") String modeName) {
        Mode mode = Mode.valueOf(modeName.toUpperCase());
        applicationMode.setMode(mode);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get the current application mode",
            description = "Returns the current mode of the application, either ONLINE or OFFLINE.")
    @GetMapping("/mode")
    public ResponseEntity<String> getMode() {
        return ResponseEntity.ok(applicationMode.getMode().toString());
    }

}
