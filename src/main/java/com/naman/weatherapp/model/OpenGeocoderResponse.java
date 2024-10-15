package com.naman.weatherapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenGeocoderResponse {
	private String zip;
	private String name;
	private String lat;
	private String lon;
	private String country;
}
