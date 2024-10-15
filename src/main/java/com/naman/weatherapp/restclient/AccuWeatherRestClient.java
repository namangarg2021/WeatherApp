package com.naman.weatherapp.restclient;

import com.naman.weatherapp.model.AccuLocationKeyResponse;
import com.naman.weatherapp.model.AccuWeatherInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AccuWeatherRestClient {
	private WebClient webClient;
	
	@Autowired
	public AccuWeatherRestClient(@Qualifier("accuWeatherWebClient") WebClient webClient) {
		this.webClient = webClient;
	}
	
	@Value("${accu.weather.apikey}")
	String accuWeatherApiKey;
	
	public AccuLocationKeyResponse getLocationKey(String cityName) {
		String url = "https://dataservice.accuweather.com/locations/v1/search?q="+cityName+"&apikey=" + accuWeatherApiKey;
		
		Mono<AccuLocationKeyResponse[]> response = webClient.get()
				.uri(url)
				.retrieve()
				.bodyToMono(AccuLocationKeyResponse[].class);
		
		AccuLocationKeyResponse[] locationKeys = response.block();
		if (locationKeys != null && locationKeys.length > 0) {
			log.info(locationKeys[0].toString());
			return locationKeys[0];
		} else {
			throw new RuntimeException("Location key not found for city: " + cityName);
		}
	}
	
	public AccuWeatherInfoResponse getWeatherInfo(String locationKey) {
		String url = "https://dataservice.accuweather.com/currentconditions/v1/"+locationKey+"?apikey=" + accuWeatherApiKey;
		
		Mono<AccuWeatherInfoResponse[]> response = webClient.get()
				.uri(url)
				.retrieve()
				.bodyToMono(AccuWeatherInfoResponse[].class);
		
		AccuWeatherInfoResponse[] weatherInfoResponses = response.block();
		if (weatherInfoResponses != null && weatherInfoResponses.length > 0) {
			log.info(weatherInfoResponses[0].toString());
			return weatherInfoResponses[0];
		} else {
			throw new RuntimeException("Weather information not found for location: " + locationKey);
		}
	}
}
