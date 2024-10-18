package com.naman.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenGeocoderResponse {
	@JsonProperty("lat")
	private String lat;
	@JsonProperty("lon")
	private String lon;
}
