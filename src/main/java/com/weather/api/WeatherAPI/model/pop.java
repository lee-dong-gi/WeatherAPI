package com.weather.api.WeatherAPI.model;


import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class pop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseDate;
    private String baseTime;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private String nx;
    private String ny;
}
