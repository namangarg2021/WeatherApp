package com.naman.weatherapp.exception;


public class WeatherDetailsException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public WeatherDetailsException(String message) {
        super(message);
    }
}
