package com.naman.weatherapp.restclient;

import com.naman.weatherapp.model.OpenGeocoderResponse;
import com.naman.weatherapp.model.OpenWeatherDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OpenWeatherRestClient {
	
	private WebClient webClient;
	
	@Autowired
	public OpenWeatherRestClient(@Qualifier("openWeatherWebClient") WebClient webClient) {
		this.webClient = webClient;
	}
	
	@Value("${open.weather.apikey}")
	String openWeatherApiKey;
	
	public OpenGeocoderResponse getGeocoderDetails(String zipCode) {
		String url = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zipCode+"&appid="+openWeatherApiKey;
		System.out.println("getGeocoderDetails started");
		
		Mono<OpenGeocoderResponse> response = webClient.get()
				.uri(url)
				.retrieve()
				.bodyToMono(OpenGeocoderResponse.class);

		return response.block();
	}
	
	public OpenWeatherDetailsResponse getWeatherDetails(String lat, String lon) {
		String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat+"&lon="+lon+"&appid"+openWeatherApiKey+"&appid="+openWeatherApiKey;

		System.out.println("getWeatherDetails started");

		Mono<OpenWeatherDetailsResponse> response = webClient.get()
				.uri(url)
				.retrieve()
				.bodyToMono(OpenWeatherDetailsResponse.class);
		
		return response.block();
	}
}
