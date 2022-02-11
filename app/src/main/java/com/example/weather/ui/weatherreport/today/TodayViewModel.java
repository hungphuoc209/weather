package com.example.weather.ui.weatherreport.today;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.weather.model.currentweather.CurrentWeatherRespond;
import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.reporsitory.Repository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TodayViewModel extends AndroidViewModel {

    Repository repository = new Repository(getApplication());

    public TodayViewModel(@NonNull Application application) {
        super(application);
    }

    public Single<CurrentWeatherRespond> getCurrentWeatherByCityName(String cityName,boolean choose){
        return repository.getCurrentWeatherByCityName(cityName,choose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    public Single<DailyWeatherRespond> getDailyWeatherRespond(String cityName,boolean choose) {
        return repository.getDailyWeatherByCityName(cityName,choose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
