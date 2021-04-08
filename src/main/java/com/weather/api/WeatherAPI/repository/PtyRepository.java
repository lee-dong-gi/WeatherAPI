package com.weather.api.WeatherAPI.repository;

import com.weather.api.WeatherAPI.model.Weather;
import com.weather.api.WeatherAPI.model.pty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PtyRepository extends JpaRepository<pty, Long> {

    List<pty> findByBaseDateAndBaseTime(String baseDate, String baseTime);
}
