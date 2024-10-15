package com.naman.weatherapp.service;

import com.naman.weatherapp.model.AccuLocationKeyResponse;
import com.naman.weatherapp.model.AccuWeatherInfoResponse;
import com.naman.weatherapp.model.OpenGeocoderResponse;
import com.naman.weatherapp.restclient.AccuWeatherRestClient;
import com.naman.weatherapp.restclient.OpenWeatherRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherAppService {
	@Autowired
	AccuWeatherRestClient accuWeatherRestClient;
	
	@Autowired
	OpenWeatherRestClient openWeatherRestClient;
	
	public OpenGeocoderResponse getWeatherDetails(String cityName, String zipCode) {
		AccuLocationKeyResponse accuLocationKeyResponse = accuWeatherRestClient.getLocationKey(cityName);
		AccuWeatherInfoResponse accuWeatherInfoResponse = accuWeatherRestClient.getWeatherInfo(accuLocationKeyResponse.getAccuLocationKey());
		
		OpenGeocoderResponse openGeocoderResponse = openWeatherRestClient.getGeocoderDetails(zipCode);
		
		return openGeocoderResponse;
	}
}
