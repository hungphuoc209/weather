package com.example.weather.ui.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.base.BaseFragment;
import com.example.weather.database.Database;
import com.example.weather.model.dailyweather.DailyWeather;
import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.model.daysweather.HourlyWeather;
import com.example.weather.model.daysweather.HourlyWeatherRespond;
import com.example.weather.ui.weatherreport.today.TodayFragment;
import com.example.weather.ultils.Constant;
import com.example.weather.ultils.ConvertUnit;
import com.example.weather.ultils.Pref;
import com.example.weather.ultils.PrefDefault;
import com.example.weather.ultils.event.LoadHourlyEvent;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailFragment extends BaseFragment {
    //ui
    private DetailViewModel mDetailViewModel;
    private Button mBtnClose;
    private TextView mTvWeekDay, mTvMaxTemp, mTvTemp, mTvMinTemp;
    private ImageView mImgIcon;
    private RecyclerView mRvHourly;
    private LineChart mLineChart;
    //logic
    private int dayPosition;
    private String date;
    private final AtomicBoolean isLoad = new AtomicBoolean();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert container != null;
        container.setVisibility(View.VISIBLE);
        assert getArguments() != null;
        dayPosition = getArguments().getInt(TodayFragment.DAY_POSITION);
        Log.d("LogTest", "" + dayPosition);
        return inflater.inflate(R.layout.detail_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        isLoad.set(Prefs.getBoolean(Pref.IS_HOURLY_LOAD, PrefDefault.IS_HOURLY_LOAD));
        Log.d("LogTest", "Detail city : " + Prefs.getString(Pref.CITY_NAME, PrefDefault.CITY_NAME));
        loadWeather(dayPosition, Prefs.getString(Pref.CITY_NAME, PrefDefault.CITY_NAME), !isLoad.get());
        initListener(view);
    }

    private void initView(View view) {
        mBtnClose = view.findViewById(R.id.btnClose);
        mDetailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        mTvMinTemp = view.findViewById(R.id.tvMinTempDetail);
        mTvMaxTemp = view.findViewById(R.id.tvMaxTempDetail);
        mTvTemp = view.findViewById(R.id.tvTempDetail);
        mImgIcon = view.findViewById(R.id.imgWeatherIconDetail);
        mTvWeekDay = view.findViewById(R.id.tvWeekdayDetail);
        mRvHourly = view.findViewById(R.id.rvWeatherHour);
        mLineChart = view.findViewById(R.id.lcTemp);
    }

    private void initListener(View view) {
        mBtnClose.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            Fragment fragment = fm.findFragmentByTag(TodayFragment.DETAIL_TAG);
            if (fragment != null) {
                getParentFragmentManager().beginTransaction().remove(fragment).commit();
                ((ViewGroup) view.getParent()).setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setViewDailyWeather(DailyWeather dailyWeather) {
        date = ConvertUnit.getDateFromFt(dailyWeather.getDt(), "yyyy/MM/dd");
        mTvTemp.setText(String.format(getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(dailyWeather.getTemp().getDay())));
        mTvMaxTemp.setText(String.format(getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(dailyWeather.getTemp().getMax())));
        mTvMinTemp.setText(String.format(getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(dailyWeather.getTemp().getMin())));
        mTvWeekDay.setText(ConvertUnit.getDateFromFt(dailyWeather.getDt(), "EEEE"));
        Picasso.get().load(Constant.ICON_BASE_URL + dailyWeather.getWeather().get(0).getIcon() + Constant.ICON_SUB_PARAM).into(mImgIcon);
    }

    private void setRecyclerView(List<HourlyWeather> hourlyWeathers) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        DetailAdapter detailAdapter = new DetailAdapter(hourlyWeathers, getContext());
        mRvHourly.setLayoutManager(llm);
        mRvHourly.setAdapter(detailAdapter);
        detailAdapter.notifyDataSetChanged();
    }

    private void loadWeather(int dayPosition, String cityName, boolean apiOrDb) {
        mDetailViewModel.getDetailWeather(cityName, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DailyWeatherRespond>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull DailyWeatherRespond dailyWeatherRespond) {
                        setViewDailyWeather(dailyWeatherRespond.getList().get(dayPosition));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
        mDetailViewModel.getHourlyWeatherByCityName(cityName, apiOrDb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<HourlyWeatherRespond>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull HourlyWeatherRespond hourlyWeatherRespond) {
                        List<HourlyWeather> hourlyWeathers = new ArrayList<>();
                        for (HourlyWeather hourlyWeather : hourlyWeatherRespond.getList()) {
                            if (ConvertUnit.getDateFromFt(hourlyWeather.getDt(), "yyyy/MM/dd").equals(date)) {
                                hourlyWeathers.add(hourlyWeather);
                            }
                        }
                        setValueChart(hourlyWeathers);
                        setRecyclerView(hourlyWeathers);
                        storeHourly(hourlyWeatherRespond, apiOrDb);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }

    private void setValueChart(List<HourlyWeather> hourlyWeathers) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < hourlyWeathers.size(); i++) {
            entries.add(new Entry(i, (float) ConvertUnit.fahrenheitToCelsius(hourlyWeathers.get(i).getMain().getTemp())));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setLineWidth(4f);
        dataSet.setCircleRadius(7f);
        dataSet.setHighlightEnabled(false);
        dataSet.setColors(new int[]{R.color.holo_light_blue}, getContext());
        dataSet.setCircleColor(getResources().getColor(R.color.holo_orange));
        dataSet.setValueTextSize(12);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Objects.requireNonNull(getContext()).getString(R.string.temp_C_format), value);
            }
        });
        mLineChart.setVisibility(View.VISIBLE);
        LineData lineData = new LineData(dataSet);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.getAxisLeft().setDrawLabels(false);
        mLineChart.getAxisRight().setDrawLabels(false);
        mLineChart.getXAxis().setDrawLabels(false);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.getAxisLeft().setDrawAxisLine(false);
        mLineChart.getAxisRight().setDrawAxisLine(false);
        mLineChart.getXAxis().setDrawAxisLine(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.setData(lineData);
        mLineChart.animateY(1000);
    }

    private void storeHourly(HourlyWeatherRespond hourlyWeatherRespond, boolean apiOrDb) {
        if (apiOrDb) {
            Database database = Database.getInstance(getContext());
            database.clearHourly()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onComplete() {
                            Log.d("LogTest", "clear hourly");
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }
                    });
            database.addHourly(hourlyWeatherRespond)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onComplete() {
                            Log.d("LogTest", "add hourly");
                            isLoad.set(true);
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }
                    });
        }
    }
}
