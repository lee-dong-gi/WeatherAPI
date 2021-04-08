package com.weather.api.WeatherAPI.repository;

import com.weather.api.WeatherAPI.model.reh;
import com.weather.api.WeatherAPI.model.tmn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TmnRepository extends JpaRepository<tmn, Long> {

    List<tmn> findByBaseDateAndBaseTime(String baseDate, String baseTime);

}
