package com.weather.api.WeatherAPI.repository;

import com.weather.api.WeatherAPI.model.tmn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmnRepository extends JpaRepository<tmn, Long> {
}
