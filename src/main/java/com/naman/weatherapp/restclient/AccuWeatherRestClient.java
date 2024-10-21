package com.naman.weatherapp.restclient;

import com.naman.weatherapp.exception.WeatherDetailsException;
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
		String url = "https://dataservice.accuweatherSS.com/locations/v1/search?q="+cityName+"&apikey=" + accuWeatherApiKey;
		System.out.println(" get Location key started");
		Mono<AccuLocationKeyResponse[]> response = webClient.get()
				.uri(url)
				.retrieve()
				.onStatus(
						status -> status.is4xxClientError() || status.is5xxServerError(),  // Handle client or server errors
						clientResponse -> clientResponse.bodyToMono(String.class)           // Extract error details
								.map(body -> {
											System.out.println("jiiii");
											return new WeatherDetailsException("Failed to fetch location key: " + body);
											}
										)
				)
				.bodyToMono(AccuLocationKeyResponse[].class);

		AccuLocationKeyResponse[] locationKeys = response.block();
		if (locationKeys != null && locationKeys.length > 0) {
			log.info(locationKeys[0].toString());
			return locationKeys[0];
		} else {
			System.out.println("hiiiiiii");
			throw new RuntimeException("Location key not found for city: " + cityName);
		}
	}
	
	public AccuWeatherInfoResponse getWeatherInfo(String locationKey) {
		String url = "https://dataservice.accuweather.com/currentconditions/v1/"+locationKey+"?apikey=" + accuWeatherApiKey;
		System.out.println(" getWeatherInfo started");

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
