package com.weather.api.WeatherAPI.repository;

import com.weather.api.WeatherAPI.model.tmn;
import com.weather.api.WeatherAPI.model.tmx;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TmxRepository extends JpaRepository<tmx, Long> {

    List<tmx> findByBaseDateAndBaseTime(String baseDate, String baseTime);

}
