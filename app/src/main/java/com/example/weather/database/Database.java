package com.example.weather.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.weather.model.currentweather.CurrentWeatherRespond;
import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.model.daysweather.HourlyWeatherRespond;
import com.example.weather.ultils.ConvertJson;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@androidx.room.Database(entities = {CurrentWeatherRespond.class, HourlyWeatherRespond.class, DailyWeatherRespond.class}, version = 1)
@TypeConverters({ConvertJson.class})
public abstract class Database extends RoomDatabase {
    public abstract CurrentWeatherDao currentWeatherDao();

    public abstract DailyWeatherDao dailyWeatherDao();

    public abstract HourlyWeatherDao hourlyWeatherDao();

    private static Database instance;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, Database.class, "weather")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public Completable addCurrent(CurrentWeatherRespond currentWeatherRespond) {
        return currentWeatherDao().addCurrent(currentWeatherRespond);

    }

    public Single<CurrentWeatherRespond> getCurrent() {
        return currentWeatherDao().getCurrent();
    }

    public Completable clearCurrent() {
        return currentWeatherDao().deleteCurrent();
    }

    //fiveDay
    public Completable addHourly(HourlyWeatherRespond hourlyWeatherRespond) {
        return hourlyWeatherDao().addHourly(hourlyWeatherRespond);
    }

    public Completable clearHourly() {
        return hourlyWeatherDao().deleteHourly();
    }

    public Single<HourlyWeatherRespond> getHourly() {
        return hourlyWeatherDao().getHourly();
    }

    //daily
    public Completable addDaily(DailyWeatherRespond dailyWeatherRespond) {
        return dailyWeatherDao().addDaily(dailyWeatherRespond);
    }

    public Single<DailyWeatherRespond> getDaily() {
        return dailyWeatherDao().getDaily();
    }

    public Completable clearDaily() {
        return dailyWeatherDao().deleteDaily();
    }
}
