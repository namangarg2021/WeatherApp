package com.naman.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccuWeatherInfoResponse {
	@JsonProperty("WeatherText")
	private String weatherText;
	
	@JsonProperty("IsDayTime")
	private boolean isDayTime;
	
	@JsonProperty("HasPrecipitation")
	private boolean hasPrecipitation;
	
	@JsonProperty("PrecipitationType")
	private String precipitationType;
	
	@JsonProperty("Temperature")
	private Temperature temperature;
	
	@Data
	public static class Temperature {
		@JsonProperty("Metric")
		private UnitValue metric;
		
		@Data
		public static class UnitValue {
			@JsonProperty("Value")
			private double value;
			
			@JsonProperty("Unit")
			private String unit;
		}
	}
}
