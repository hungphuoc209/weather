package com.example.weather.ui.weatherreport.tomorrow;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.reporsitory.Repository;

import io.reactivex.rxjava3.core.Single;

public class TomorrowViewModel extends AndroidViewModel {

    private final Repository repository = new Repository(getApplication());

    public TomorrowViewModel(@NonNull Application application) {
        super(application);
    }

    public Single<DailyWeatherRespond> getTomorrowWeather(String cityName, boolean choose) {
        return repository.getDailyWeatherByCityName(cityName, choose);
    }
}
