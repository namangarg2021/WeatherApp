package com.naman.weatherapp.service;

import com.naman.weatherapp.model.*;
import com.naman.weatherapp.restclient.AccuWeatherRestClient;
import com.naman.weatherapp.restclient.OpenWeatherRestClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class WeatherAppService {
	@Autowired
	AccuWeatherRestClient accuWeatherRestClient;
	
	@Autowired
	OpenWeatherRestClient openWeatherRestClient;
	
	private final ExecutorService executor = Executors.newFixedThreadPool(2);
	
	@SneakyThrows
	public WeatherResponse getWeatherDetails(String cityName, String zipCode) {
		System.out.println("getLocationKey started");
		CompletableFuture<AccuLocationKeyResponse> locationKeyFuture = CompletableFuture.supplyAsync(
				() -> accuWeatherRestClient.getLocationKey(cityName), executor);
		System.out.println("getGeocoderDetails started");
		CompletableFuture<OpenGeocoderResponse> geocoderFuture = CompletableFuture.supplyAsync(
				() -> openWeatherRestClient.getGeocoderDetails(zipCode), executor);
		System.out.println("getWeatherInfo started");
		CompletableFuture<AccuWeatherInfoResponse> weatherInfoFuture = locationKeyFuture.thenApply(
				accuLocationKey -> accuWeatherRestClient.getWeatherInfo(accuLocationKey.getAccuLocationKey()));
		System.out.println("getWeatherDetails started");
		CompletableFuture<OpenWeatherDetailsResponse> weatherDetailsFuture = geocoderFuture.thenApply(
				geocoderResponse -> openWeatherRestClient.getWeatherDetails(
						geocoderResponse.getLat(), geocoderResponse.getLon()));
		System.out.println("weatherResponseFuture started");
		CompletableFuture<WeatherResponse> weatherResponseFuture = weatherInfoFuture.thenCombine(
				weatherDetailsFuture, (accuWeatherInfo, openWeatherDetails) ->
						getWeatherResponse(accuWeatherInfo, openWeatherDetails));
		System.out.println("generating response started");
		return weatherResponseFuture.get();
	}
	
	public WeatherResponse getWeatherResponse(AccuWeatherInfoResponse accuWeatherInfo,
	                                          OpenWeatherDetailsResponse openWeatherDetailsInfo) {
		WeatherResponse response = new WeatherResponse();
		
		Supplier<WeatherResponse.Temperature> temperatureSupplier = () -> {
			WeatherResponse.Temperature temp = new WeatherResponse.Temperature();
			temp.setValue(accuWeatherInfo.getTemperature().getMetric().getValue());
			temp.setUnit(accuWeatherInfo.getTemperature().getMetric().getUnit());
			return temp;
		};
		
		Supplier<WeatherResponse.Temperature> feelsLikeTempSupplier = () -> {
			WeatherResponse.Temperature temp = new WeatherResponse.Temperature();
			temp.setValue(openWeatherDetailsInfo.getMain().getFeelsLike() - 273.15);
			temp.setUnit("C");
			return temp;
		};
		
		Consumer<WeatherResponse.Wind> setWind = wind -> {
			wind.setSpeed(openWeatherDetailsInfo.getWind().getSpeed());
			wind.setDeg(openWeatherDetailsInfo.getWind().getDeg());
			wind.setGust(openWeatherDetailsInfo.getWind().getGust());
		};
		
		response.setWeatherText(accuWeatherInfo.getWeatherText());
		response.setDayTime(accuWeatherInfo.isDayTime());
		response.setHasPrecipitation(accuWeatherInfo.isHasPrecipitation());
		response.setPrecipitationType(accuWeatherInfo.getPrecipitationType());
		
		response.setTemperature(temperatureSupplier.get());
		response.setFeelsLike(feelsLikeTempSupplier.get());
		
		response.setPressure(openWeatherDetailsInfo.getMain().getPressure());
		response.setHumidity(openWeatherDetailsInfo.getMain().getHumidity());
		response.setVisibility(openWeatherDetailsInfo.getVisibility());
		
		WeatherResponse.Wind wind = new WeatherResponse.Wind();
		setWind.accept(wind);
		response.setWind(wind);
		
		response.setSunrise(openWeatherDetailsInfo.getSys().getSunrise());
		response.setSunset(openWeatherDetailsInfo.getSys().getSunset());
		
		return response;
	}
}
