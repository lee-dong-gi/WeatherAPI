package com.weather.api.WeatherAPI.repository;

import com.weather.api.WeatherAPI.model.Weather;
import com.weather.api.WeatherAPI.model.pop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopRepository extends JpaRepository<pop, Long> {

    List<pop> findByBaseDateAndBaseTime(String baseDate, String baseTime);

}
