package com.naman.weatherapp.restclient;

import com.naman.weatherapp.model.AccuLocationKeyResponse;
import com.naman.weatherapp.model.AccuWeatherInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
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
		
		Mono<AccuLocationKeyResponse> response = webClient.get()
				.uri(url)
				.retrieve()
				.bodyToMono(AccuLocationKeyResponse.class);
		
		return response.block();
	}
	
	public AccuWeatherInfoResponse getWeatherInfo(String locationKey) {
		String url = "https://dataservice.accuweather.com/currentconditions/v1/"+locationKey+"?apikey=" + accuWeatherApiKey;
		
		Mono<AccuWeatherInfoResponse> response = webClient.get()
				.uri(url)
				.retrieve()
				.bodyToMono(AccuWeatherInfoResponse.class);
		
		return response.block();
	}
}
