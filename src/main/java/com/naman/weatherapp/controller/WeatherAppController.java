package com.naman.weatherapp.controller;

import com.naman.weatherapp.model.OpenGeocoderResponse;
import com.naman.weatherapp.service.WeatherAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherAppController {
	
	private final WeatherAppService weatherAppService;
	
	@Autowired
	public WeatherAppController(WeatherAppService weatherAppService) {
		this.weatherAppService = weatherAppService;
	}
	
	@GetMapping
	@ResponseBody
	public OpenGeocoderResponse getWeatherDetails(@RequestParam(required = false) String cityName,
	                                              @RequestParam(required = false) String zipCode) {
		return weatherAppService.getWeatherDetails(cityName, zipCode);
	}
}
