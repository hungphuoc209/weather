package com.example.weather.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.weather.model.currentweather.CurrentWeatherRespond;
import com.example.weather.ultils.Constant;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CurrentWeatherDao {
    @Insert
    Completable addCurrent(CurrentWeatherRespond currentWeatherRespond);

    @Query("DELETE FROM " + Constant.CURRENT_TABLE_NAME)
    Completable deleteCurrent();

    @Query("SELECT * FROM " + Constant.CURRENT_TABLE_NAME)
    Single<CurrentWeatherRespond> getCurrent();
}

