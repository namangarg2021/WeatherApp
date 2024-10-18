package com.naman.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherResponse {
	@JsonProperty("WeatherText")
	private String weatherText;
	
	@JsonProperty("HasPrecipitation")
	private boolean hasPrecipitation;
	
	@JsonProperty("PrecipitationType")
	private String precipitationType;
	
	@JsonProperty("IsDayTime")
	private boolean isDayTime;
	
	@JsonProperty("Temperature")
	private Temperature temperature;
	
	@JsonProperty("feels_like")
	private Temperature feelsLike;
	
	@JsonProperty("pressure")
	private int pressure;
	
	@JsonProperty("humidity")
	private int humidity;
	
	@JsonProperty("visibility")
	private int visibility;
	
	@JsonProperty("wind")
	private Wind wind;
	
	@JsonProperty("sunrise")
	private long sunrise;
	
	@JsonProperty("sunset")
	private long sunset;
	
	@Data
	public static class Temperature {
		@JsonProperty("Value")
		private double value;
		
		@JsonProperty("Unit")
		private String unit;
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
}

