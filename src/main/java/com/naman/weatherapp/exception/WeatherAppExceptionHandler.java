package com.naman.weatherapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class WeatherAppExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest().body("Invalid Request " + ex.getMessage());
    }

    @ExceptionHandler(WeatherDetailsException.class)
    public ResponseEntity<String> handleWeatherDetailsException(WeatherDetailsException ex) {
        System.out.println("in weather details " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
