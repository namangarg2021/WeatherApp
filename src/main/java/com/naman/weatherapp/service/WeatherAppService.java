package com.naman.weatherapp.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naman.weatherapp.exception.WeatherDetailsException;
import com.naman.weatherapp.model.AccuLocationKeyResponse;
import com.naman.weatherapp.model.AccuWeatherInfoResponse;
import com.naman.weatherapp.model.OpenGeocoderResponse;
import com.naman.weatherapp.model.OpenWeatherDetailsResponse;
import com.naman.weatherapp.model.WeatherResponse;
import com.naman.weatherapp.restclient.AccuWeatherRestClient;
import com.naman.weatherapp.restclient.OpenWeatherRestClient;

import lombok.SneakyThrows;

@Service
public class WeatherAppService {
	@Autowired
	AccuWeatherRestClient accuWeatherRestClient;

	@Autowired
	OpenWeatherRestClient openWeatherRestClient;

	private final ExecutorService executor = Executors.newFixedThreadPool(2);


	public CompletableFuture<WeatherResponse> getWeatherDetails(String cityName, String zipCode) {

		System.out.println("getLocationKey started");
		CompletableFuture<AccuLocationKeyResponse> locationKeyFuture = CompletableFuture
				.supplyAsync(() -> accuWeatherRestClient.getLocationKey(cityName), executor)
				.exceptionally(ex -> {
					System.out.println("in service "+ex.getMessage());
					throw new WeatherDetailsException("Failed to fetch location key"+ ex);

				});

		System.out.println("getGeocoderDetails started");
		CompletableFuture<OpenGeocoderResponse> geocoderFuture = CompletableFuture
				.supplyAsync(() -> openWeatherRestClient.getGeocoderDetails(zipCode), executor)
				.exceptionally(ex -> {
					throw new WeatherDetailsException("Failed to fetch geocoder details"+ ex);
				});

		System.out.println("getWeatherInfo started");
		CompletableFuture<AccuWeatherInfoResponse> weatherInfoFuture = locationKeyFuture
				.thenApply(accuLocationKey -> accuWeatherRestClient.getWeatherInfo(accuLocationKey.getAccuLocationKey()))
				.exceptionally(ex -> {
					throw new WeatherDetailsException("Failed to fetch weather info"+ex);
				});

		System.out.println("getWeatherDetails started");
		CompletableFuture<OpenWeatherDetailsResponse> weatherDetailsFuture = geocoderFuture
				.thenApply(geocoderResponse -> openWeatherRestClient.getWeatherDetails(
						geocoderResponse.getLat(), geocoderResponse.getLon()))
				.exceptionally(ex -> {
					throw new WeatherDetailsException("Failed to fetch weather details"+ ex);
				});

		System.out.println("Generating weather response...");
		return weatherInfoFuture.thenCombine(weatherDetailsFuture,
				(accuWeatherInfo, openWeatherDetails) ->
						getWeatherResponse(accuWeatherInfo, openWeatherDetails)
		);
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
