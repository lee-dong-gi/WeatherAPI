package com.weather.api.WeatherAPI.repository;

import com.weather.api.WeatherAPI.model.pty;
import com.weather.api.WeatherAPI.model.reh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RehRepository extends JpaRepository<reh, Long> {

    List<reh> findByBaseDateAndBaseTime(String baseDate, String baseTime);

}
