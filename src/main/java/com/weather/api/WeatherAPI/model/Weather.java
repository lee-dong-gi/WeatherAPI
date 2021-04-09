package com.weather.api.WeatherAPI.model;

import lombok.Data;
import java.util.List;
/*
    기상예보관련 데이터 객체들을 담기위한 MODEL
 */
@Data
public class Weather {

    private List<pop> POP;

    private List<pty> PTY;

    private List<reh> REH;

    private List<tmn> TMN;

    private List<tmx> TMX;

}
