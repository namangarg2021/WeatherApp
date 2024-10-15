package com.naman.weatherapp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenWeatherDetailsResponse {
	@JsonProperty("main")
	private Main main;
	
	@JsonProperty("visibility")
	private int visibility;
	
	@JsonProperty("wind")
	private Wind wind;
	
	@JsonProperty("sys")
	private Sys sys;
	
	@Data
	public static class Main {
		@JsonProperty("feels_like")
		private double feelsLike;
		
		@JsonProperty("pressure")
		private int pressure;
		
		@JsonProperty("humidity")
		private int humidity;
	}
	
	@Data
	public static class Wind {
		@JsonProperty("speed")
		private double speed;
		
		@JsonProperty("deg")
		private int deg;
		
		@JsonProperty("gust")
		private double gust;
	}
	
	@Data
	public static class Sys {
		@JsonProperty("sunrise")
		private long sunrise;
		
		@JsonProperty("sunset")
		private long sunset;
	}
}