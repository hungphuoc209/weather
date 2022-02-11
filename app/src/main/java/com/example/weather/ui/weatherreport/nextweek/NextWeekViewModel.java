package com.example.weather.ui.weatherreport.nextweek;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.reporsitory.Repository;

import io.reactivex.rxjava3.core.Single;

public class NextWeekViewModel extends AndroidViewModel {

    private final Repository repository = new Repository(getApplication());

    public NextWeekViewModel(@NonNull Application application) {
        super(application);
    }

    public Single<DailyWeatherRespond> getDailyWeatherList(String cityName,boolean choose) {
        return repository.getDailyWeatherByCityName(cityName,choose);
    }
}
