package com.example.weather.reporsitory;

import android.app.Application;
import android.util.Log;

import com.example.weather.model.currentweather.CurrentWeatherRespond;
import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.model.daysweather.HourlyWeatherRespond;
import com.example.weather.database.Database;
import com.example.weather.service.ApiClient;
import com.example.weather.ultils.Constant;

import io.reactivex.rxjava3.core.Single;

public class Repository {
    private final Database database;
    private final ApiClient apiClient = ApiClient.getInstance();

    public Repository(Application application) {
        database = Database.getInstance(application);
    }

    public Single<CurrentWeatherRespond> getCurrentWeatherByCityName(String cityName, boolean apiOrDb) {
        if (apiOrDb) {
            Log.d("LogTest", "current api");
            return getCurrentWeatherApi(cityName);
        }
        Log.d("LogTest", "current local");
        return getCurrentWeatherDb();
    }

    private Single<CurrentWeatherRespond> getCurrentWeatherApi(String cityName) {
        return apiClient.getApiService().doGetCurrentWeatherByCityName(cityName, Constant.API_KEY);
    }

    private Single<CurrentWeatherRespond> getCurrentWeatherDb() {
        return database.getCurrent();
    }

    public Single<HourlyWeatherRespond> getHourlyWeatherByCityName(String cityName, boolean choose) {
        if (choose) {
            Log.d("LogTest", "hourly api");
            return getHourlyWeatherApi(cityName);
        }
        Log.d("LogTest", "hourly local");
        return getHourlyWeatherDb();
    }

    private Single<HourlyWeatherRespond> getHourlyWeatherApi(String cityName) {
        return apiClient.getApiService().doGetHourlyWeatherByCityName(cityName, Constant.API_KEY);
    }

    private Single<HourlyWeatherRespond> getHourlyWeatherDb() {
        return database.getHourly();
    }

    public Single<DailyWeatherRespond> getDailyWeatherByCityName(String cityName, boolean choose) {
        if (choose) {
            Log.d("LogTest", "daily api");
            return getDailyWeatherApi(cityName);
        }
        Log.d("LogTest", "daily local");
        return getDailyWeatherDb();
    }

    private Single<DailyWeatherRespond> getDailyWeatherApi(String cityName) {
        return apiClient.getApiService().doGetDailyWeatherByCityName(cityName, Constant.API_KEY, 16);
    }

    private Single<DailyWeatherRespond> getDailyWeatherDb() {
        return database.getDaily();
    }
}
