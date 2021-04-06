package com.weather.api.WeatherAPI.repository;

import com.weather.api.WeatherAPI.model.tmx;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmxRepository extends JpaRepository<tmx, Long> {
}
