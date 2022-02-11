package com.example.weather.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.ultils.Constant;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DailyWeatherDao {
    @Insert
    Completable addDaily(DailyWeatherRespond dailyWeatherRespond);

    @Query("DELETE FROM " + Constant.DAILY_TABLE_NAME)
    Completable deleteDaily();

    @Query("SELECT * FROM " + Constant.DAILY_TABLE_NAME)
    Single<DailyWeatherRespond> getDaily();
}
