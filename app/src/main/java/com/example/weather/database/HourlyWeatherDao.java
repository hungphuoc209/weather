package com.example.weather.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.weather.model.daysweather.HourlyWeatherRespond;
import com.example.weather.ultils.Constant;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface HourlyWeatherDao {
    @Insert(entity = HourlyWeatherRespond.class)
    Completable addHourly(HourlyWeatherRespond hourlyWeatherRespond);

    @Query("DELETE FROM " + Constant.HOURLY_TABLE_NAME)
    Completable deleteHourly();

    @Query("SELECT * FROM " + Constant.HOURLY_TABLE_NAME)
    Single<HourlyWeatherRespond> getHourly();
}
