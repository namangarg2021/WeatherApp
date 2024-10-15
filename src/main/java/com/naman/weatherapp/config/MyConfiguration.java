package com.naman.weatherapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MyConfiguration {
	@Bean("openWeatherWebClient")
	public WebClient openWeatherWebClient(WebClient.Builder builder) {
		return builder.build();
	}
	
	@Bean("accuWeatherWebClient")
	public WebClient accuWeatherWebClient(WebClient.Builder builder) {
		return builder.build();
	}
}
