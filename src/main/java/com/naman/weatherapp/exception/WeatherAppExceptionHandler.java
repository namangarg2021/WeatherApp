package com.naman.weatherapp.exception;

import com.naman.weatherapp.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class WeatherAppExceptionHandler {


    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientRequestException(WebClientResponseException ex) {
        System.out.println("in weather details " + ex.getMessage());
        System.out.println("hiiiii");
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    //    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
//        System.out.println("in runtime " + ex.getMessage());
//        System.out.println("hii");
//        return ResponseEntity.badRequest().body(ex.getMessage());
//    }
    @ExceptionHandler(WeatherDetailsException.class)
    public ResponseEntity<ErrorResponseDTO> handleWeatherDetailsException(WeatherDetailsException ex) {
        System.out.println("in weather details " + ex.getMessage());
        System.out.println("hello");
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setMessage("Invalid parameter value: " + ex.getMessage());
        errorResponseDTO.setCode(400);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        String timestamp = sdf.format(new Date());
        errorResponseDTO.setTimestamp(timestamp);
        return ResponseEntity.badRequest().body(errorResponseDTO);
    }


//    @ExceptionHandler(value = {Exception.class})
//    protected ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
//        System.out.println("hiiiii");
//        return ResponseEntity.badRequest().body("Invalid Request " + ex.getMessage());
//    }
}
