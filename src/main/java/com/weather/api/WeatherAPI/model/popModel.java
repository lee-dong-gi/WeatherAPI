package com.weather.api.WeatherAPI.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class popModel {
    private String baseDate;
    private String baseTime;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private String nx;
    private String ny;
}
