package com.example.weather.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.model.daysweather.HourlyWeatherRespond;
import com.example.weather.reporsitory.Repository;

import io.reactivex.rxjava3.core.Single;


public class DetailViewModel extends AndroidViewModel {

    private final Repository repository = new Repository(getApplication());

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public Single<DailyWeatherRespond> getDetailWeather(String cityName,boolean apiOrDb) {
        return repository.getDailyWeatherByCityName(cityName,apiOrDb);
    }

    public Single<HourlyWeatherRespond> getHourlyWeatherByCityName(String cityName,boolean apiOrDb) {
        return repository.getHourlyWeatherByCityName(cityName,apiOrDb);
    }
}
