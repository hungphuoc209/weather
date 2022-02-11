package com.example.weather.service;

import com.example.weather.model.currentweather.CurrentWeatherRespond;
import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.model.daysweather.HourlyWeatherRespond;
import com.example.weather.ultils.Constant;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET(Constant.CURRENT_WEATHER)
    Single<CurrentWeatherRespond> doGetCurrentWeatherByCityName(
            @Query(Constant.QUERY_CITY) String city,
            @Query(Constant.QUERY_KEY) String appId
    );

    @GET(Constant.FORECAST_WEATHER)
    Single<HourlyWeatherRespond> doGetHourlyWeatherByCityName(
            @Query(Constant.QUERY_CITY) String city,
            @Query(Constant.QUERY_KEY) String appId
    );

    @GET(Constant.DAILY_WEATHER)
    Single<DailyWeatherRespond> doGetDailyWeatherByCityName(
            @Query(Constant.QUERY_CITY) String city,
            @Query(Constant.QUERY_KEY) String appId,
            @Query(Constant.QUERY_CNT) int cnt
    );
}
