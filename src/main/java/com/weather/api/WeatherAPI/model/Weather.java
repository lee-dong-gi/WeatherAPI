package com.weather.api.WeatherAPI.model;

import lombok.Data;

import java.util.List;

@Data
public class Weather {

    private List<pop> POP;

    private List<pty> PTY;

    private List<reh> REH;

    private List<tmn> TMN;

    private List<tmx> TMX;

}
