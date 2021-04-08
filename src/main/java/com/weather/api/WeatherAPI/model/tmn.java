package com.weather.api.WeatherAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
/*
    아침최저기온
    단위 : ℃
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class tmn {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseDate;    //발표일자
    private String baseTime;    //발표시각
    private String fcstDate;    //예측일자
    private String fcstTime;    //예측시간
    private String fcstValue;   //예보 값
    private String nx;  // 예보지점 X 좌표
    private String ny;  // 예보지점 Y 좌표
}
