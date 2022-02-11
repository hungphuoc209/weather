package com.example.weather.ui.weatherreport.today;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.base.BaseFragment;
import com.example.weather.database.Database;
import com.example.weather.model.currentweather.CurrentWeatherRespond;
import com.example.weather.model.dailyweather.DailyWeather;
import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.ui.detail.DetailFragment;
import com.example.weather.ultils.AlertDialogHelper;
import com.example.weather.ultils.AppUtils;
import com.example.weather.ultils.ConvertUnit;
import com.example.weather.ultils.Pref;
import com.example.weather.ultils.PrefDefault;
import com.example.weather.ultils.event.ChooseCityEvent;
import com.example.weather.ultils.event.ChooseDefaultCityEvent;
import com.example.weather.ultils.event.LoadNextWeekEvent;
import com.example.weather.ultils.event.LoadTomorrowEvent;
import com.example.weather.ultils.event.NameCityCallBackEvent;
import com.example.weather.ultils.event.RefreshEvent;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TodayFragment extends BaseFragment implements FiveDayAdapter.onItemClickListener {
    //const
    public static final String DETAIL_TAG = "DETAIL_FRAG";
    public static final String DAY_POSITION = "DAY_POSITION";
    //ui
    private CardView mCvTodayWeather;
    private TextView mTvTemp, mTvDescription, mTvHumidity, mTvWind;
    private ImageView mImgWeatherIcon;
    private RecyclerView mRvFiveDays;
    //non-ui
    private TodayViewModel mTodayViewModel;
    private Database database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        loadWeather(Prefs.getString(PrefDefault.CITY_NAME, PrefDefault.CITY_NAME), false);
        initListener();
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
        mTodayViewModel = new ViewModelProvider(this).get(TodayViewModel.class);
        mCvTodayWeather = view.findViewById(R.id.cvToday);
        mTvTemp = view.findViewById(R.id.tvTemp);
        mTvDescription = view.findViewById(R.id.tvDescription);
        mTvHumidity = view.findViewById(R.id.tvHumidityTitle);
        mTvWind = view.findViewById(R.id.tvWind);
        mImgWeatherIcon = view.findViewById(R.id.imgWeatherIcon);
        mRvFiveDays = view.findViewById(R.id.rvNext4days);
    }

    private void initListener() {
        AtomicBoolean isClicked = new AtomicBoolean(false);
        mCvTodayWeather.setOnClickListener(v -> {
            Log.d("LogTest", "click");
            resize(!isClicked.get());
            isClicked.set(!isClicked.get());
        });
    }

    private void setViewWeather(CurrentWeatherRespond currentWeatherRespond) {
        mTvTemp.setText(String.format(getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(currentWeatherRespond.getMain().getTemp())));
        mTvDescription.setText(currentWeatherRespond.getWeather().get(0).getDescription());
        mTvHumidity.setText(String.format(getString(R.string.humidity_format), currentWeatherRespond.getMain().getHumidity()));
        mTvWind.setText(String.format(getString(R.string.wind_ms_format), currentWeatherRespond.getWind().getSpeed()));
        Picasso.get().load(ConvertUnit.iconPath(currentWeatherRespond.getWeather().get(0).getIcon())).into(mImgWeatherIcon);
        EventBus.getDefault().postSticky(new NameCityCallBackEvent(currentWeatherRespond.getName() + "," + currentWeatherRespond.getSys().getCountry()));

    }

    private void setRecyclerView(List<DailyWeather> dailyWeathers) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mRvFiveDays.setLayoutManager(llm);
        FiveDayAdapter mFiveDayAdapter = new FiveDayAdapter(getContext(), this, dailyWeathers);
        mRvFiveDays.setAdapter(mFiveDayAdapter);
        mFiveDayAdapter.notifyDataSetChanged();
    }

    private void loadWeather(String cityName, boolean isDefaultCity) {
        database = Database.getInstance(getContext());
        boolean apiOrDb = AppUtils.isNetworkConnected(Objects.requireNonNull(getContext()));
        if (!apiOrDb) {
            new AlertDialogHelper(getContext(), getString(R.string.network_error), getString(R.string.internet_error)).createDialog().show();
        }
        mTodayViewModel.getCurrentWeatherByCityName(cityName, apiOrDb)
                .subscribe(new SingleObserver<CurrentWeatherRespond>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull CurrentWeatherRespond currentWeatherRespond) {
                        setViewWeather(currentWeatherRespond);
                        storeCurrent(currentWeatherRespond, apiOrDb);
                        if (isDefaultCity) {
                            Prefs.putString(PrefDefault.CITY_NAME, currentWeatherRespond.getName());
                        }
                        Prefs.putString(Pref.CITY_NAME, currentWeatherRespond.getName());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d("LogTest", "" + e.getLocalizedMessage());
                        if (Objects.equals(e.getLocalizedMessage(), getString(R.string._404_error_string))) {
                            new AlertDialogHelper(getContext()
                                    , getString(R.string.load_error)
                                    , getString(R.string.no_city_found)).createDialog().show();
                        }
                    }
                });
        mTodayViewModel.getDailyWeatherRespond(cityName, apiOrDb)
                .subscribe(new SingleObserver<DailyWeatherRespond>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull DailyWeatherRespond dailyWeatherRespond) {
                        setRecyclerView(dailyWeatherRespond.getList().subList(0, 5));
                        storeDaily(dailyWeatherRespond, apiOrDb);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChooseCityEvent(ChooseCityEvent event) {
        if (event != null) {
            loadWeather(event.getCityName(), false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshEvent event) {
        if (event != null) {
            loadWeather(Prefs.getString(Pref.CITY_NAME, PrefDefault.CITY_NAME), false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChooseDefaultCityEvent(ChooseDefaultCityEvent event) {
        loadWeather(event.getCityName(), true);
    }

    private void resize(boolean isClicked) {
        ConstraintLayout.LayoutParams cvParams = (ConstraintLayout.LayoutParams) mCvTodayWeather.getLayoutParams();
        if (isClicked) {
            Log.d("LogTest", "expand");
            cvParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            cvParams.bottomMargin = (int) getResources().getDimension(R.dimen.margin_20);
        } else {
            Log.d("LogTest", "not-expand");
            cvParams.height = (int) getResources().getDimension(R.dimen.dimen_314);
        }
        mCvTodayWeather.setLayoutParams(cvParams);
        ConstraintLayout.LayoutParams imgParams = (ConstraintLayout.LayoutParams) mImgWeatherIcon.getLayoutParams();
        if (isClicked) {
            imgParams.leftMargin = 0;
            imgParams.rightMargin = 0;
            imgParams.topMargin = (int) getResources().getDimension(R.dimen.margin_350);
        } else {
            imgParams.leftMargin = (int) getResources().getDimension(R.dimen.margin_250);
            imgParams.topMargin = (int) getResources().getDimension(R.dimen.margin_200);
            imgParams.rightMargin = 0;
        }
        mImgWeatherIcon.setLayoutParams(imgParams);
    }

    private void storeCurrent(CurrentWeatherRespond currentWeatherRespond, boolean ApiOrDb) {
        if (ApiOrDb) {
            database.clearCurrent()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onComplete() {
                            Log.d("LogTest", "clear current");
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }
                    });
            database.addCurrent(currentWeatherRespond)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onComplete() {
                            Log.d("LogTest", "add current");
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }
                    });
        }
    }

    private void storeDaily(DailyWeatherRespond dailyWeatherRespond, boolean ApiOrDb) {
        if (ApiOrDb) {
            database.clearDaily()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onComplete() {
                            Log.d("LogTest", "clear daily");
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }
                    });
            database.addDaily(dailyWeatherRespond)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onComplete() {
                            Log.d("LogTest", "add daily");
                            EventBus.getDefault().post(new LoadTomorrowEvent());
                            EventBus.getDefault().post(new LoadNextWeekEvent());
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.d("LogTest", "" + e.getLocalizedMessage());
                        }
                    });
        }
    }

    @Override
    public void onItemClick(int position) {
        FragmentManager fm = getParentFragmentManager();
        DetailFragment detailFragment = new DetailFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft
                .replace(R.id.detailContainer, detailFragment, DETAIL_TAG)
                .addToBackStack("detail")
                .commit();
        Bundle bundle = new Bundle();
        bundle.putInt(DAY_POSITION, position);
        detailFragment.setArguments(bundle);
    }

}
