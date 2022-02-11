package com.example.weather.ui.weatherreport.tomorrow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.weather.R;
import com.example.weather.base.BaseFragment;
import com.example.weather.model.dailyweather.DailyWeather;
import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.ultils.ConvertUnit;
import com.example.weather.ultils.PrefDefault;
import com.example.weather.ultils.event.LoadTomorrowEvent;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TomorrowFragment extends BaseFragment {
    private TextView mTvTemp, mTvDescription, mTvHumidity, mTvWind;
    private ImageView mWeatherIcon;
    private TomorrowViewModel mTomorrowViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tomorrow_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        loadWeather();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View view) {
        mTomorrowViewModel = new ViewModelProvider(this).get(TomorrowViewModel.class);
        mTvTemp = view.findViewById(R.id.tvTemp);
        mTvDescription = view.findViewById(R.id.tvDescription);
        mTvHumidity = view.findViewById(R.id.tvHumidityTitle);
        mTvWind = view.findViewById(R.id.tvWind);
        mWeatherIcon = view.findViewById(R.id.imgWeatherIcon);
    }

    private void loadWeather() {
        mTomorrowViewModel.getTomorrowWeather(Prefs.getString(PrefDefault.CITY_NAME, PrefDefault.CITY_NAME), false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DailyWeatherRespond>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull DailyWeatherRespond dailyWeatherRespond) {
                        setViewWeather(dailyWeatherRespond.getList().get(1));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }

    private void setViewWeather(DailyWeather dailyWeather) {
        mTvTemp.setText(String.format(getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(dailyWeather.getTemp().getDay())));
        mTvDescription.setText(dailyWeather.getWeather().get(0).getDescription());
        mTvHumidity.setText(String.format(getString(R.string.humidity_format), dailyWeather.getHumidity()));
        mTvWind.setText(String.format(getString(R.string.wind_ms_format), dailyWeather.getGust()));
        Picasso.get().load(ConvertUnit.iconPath(dailyWeather.getWeather().get(0).getIcon())).into(mWeatherIcon);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadTomorrowEvent(LoadTomorrowEvent event) {
        Log.d("LogTest", "load tomorrow");
        loadWeather();
    }
}
