package com.example.weather.ui.weatherreport.nextweek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.base.BaseFragment;
import com.example.weather.model.dailyweather.DailyWeather;
import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.ultils.PrefDefault;
import com.example.weather.ultils.event.LoadNextWeekEvent;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NextWeekFragment extends BaseFragment {
    private RecyclerView mRvDaily;
    private NextWeekViewModel mNextWeekViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nextweek_frag, container, false);
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
        mRvDaily = view.findViewById(R.id.rvDailyWeather);
        mNextWeekViewModel = new ViewModelProvider(this).get(NextWeekViewModel.class);
    }

    private void setRecyclerView(List<DailyWeather> dailyWeathers) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRvDaily.setLayoutManager(llm);
        DailyAdapter dailyAdapter = new DailyAdapter(getContext(), dailyWeathers);
        mRvDaily.setAdapter(dailyAdapter);
        dailyAdapter.notifyDataSetChanged();
    }

    private void loadWeather() {
        mNextWeekViewModel.getDailyWeatherList(Prefs.getString(PrefDefault.CITY_NAME, PrefDefault.CITY_NAME), false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DailyWeatherRespond>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull DailyWeatherRespond dailyWeatherRespond) {
                        setRecyclerView(dailyWeatherRespond.getList());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadNextWeek(LoadNextWeekEvent event){
        if(event!=null) loadWeather();
    }

}
