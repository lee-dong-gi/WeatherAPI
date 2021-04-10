package com.weather.api.WeatherAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
/*
    강수형태
    단위 : 코드값
    - 강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
        여기서 비/눈은 비와 눈이 섞여 오는 것을 의미 (진눈개비)
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class pty {
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
