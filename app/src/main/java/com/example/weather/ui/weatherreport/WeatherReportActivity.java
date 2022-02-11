package com.example.weather.ui.weatherreport;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.weather.R;
import com.example.weather.ui.about.AboutFragment;
import com.example.weather.ui.splash.SplashActivity;
import com.example.weather.ultils.AlertDialogHelper;
import com.example.weather.ultils.AppUtils;
import com.example.weather.ultils.Pref;
import com.example.weather.ultils.PrefDefault;
import com.example.weather.ultils.event.ChooseCityEvent;
import com.example.weather.ultils.event.NameCityCallBackEvent;
import com.example.weather.ultils.event.RefreshEvent;
import com.google.android.material.tabs.TabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Objects;
import java.util.Spliterator;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class WeatherReportActivity extends AppCompatActivity {
    //const
    public static final String ABOUT_TAG = "about_tag";
    //
    private MaterialSearchView materialSearchView;
    private TabLayout mTlWeatherReport;
    private Toolbar mToolbar;
    private ViewPager mVpWeatherReport;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView mTvCurrentCity;
    private Button mBtnMenu;
    private LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_report_act);
        initPref();
        RxJavaPlugins.setErrorHandler(throwable -> {
        });
        if (!Prefs.getBoolean(Pref.IS_SPLASH_LOAD, PrefDefault.IS_SPLASH_LOAD)) {
            Prefs.putBoolean(Pref.IS_SPLASH_LOAD, true);
            startActivity(new Intent(this, SplashActivity.class));
        }
        initView();
        initListener();
    }

    private void initPref() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    private void initView() {
        materialSearchView = findViewById(R.id.svCity);
        mToolbar = findViewById(R.id.toolbar);
        setToolBar();
        mTlWeatherReport = findViewById(R.id.tlWeather);
        mVpWeatherReport = findViewById(R.id.vpWeatherReport);
        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        setViewPaper();
        mTvCurrentCity = findViewById(R.id.tvCurrentCity);
        mBtnMenu = findViewById(R.id.btnMenu);
        llContainer = findViewById(R.id.detailContainer);
    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    private void initListener() {
        if (Prefs.getString(PrefDefault.CITY_NAME, PrefDefault.CITY_NAME).equals("")) {
            setDefaultCity();
        }
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                EventBus.getDefault().post(new ChooseCityEvent(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (AppUtils.isLimited()) {
                Prefs.putLong(Pref.TIME_LAST_QUERY, Calendar.getInstance().getTimeInMillis());
                EventBus.getDefault().post(new RefreshEvent(true));
            } else {
                new AlertDialogHelper(WeatherReportActivity.this
                        , getString(R.string.load_error)
                        , getString(R.string.refresh_false_noti))
                        .createDialog().show();
            }
            swipeRefreshLayout.setRefreshing(false);
        });
        mBtnMenu.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            DialogFragment aboutFragment = new AboutFragment();
            aboutFragment.show(fm, ABOUT_TAG);
        });
    }

    private void setDefaultCity() {
        if (AppUtils.isNetworkConnected(getApplication())) {
            FragmentManager fm = getSupportFragmentManager();
            DialogFragment aboutFragment = new AboutFragment();
            aboutFragment.setCancelable(false);
            if (aboutFragment.getDialog() != null) {
                aboutFragment.getDialog().setCanceledOnTouchOutside(false);
            }
            aboutFragment.show(fm, ABOUT_TAG);
        }
    }

    private void setViewPaper() {
        ViewPaperAdapter mVpWeatherReportAdapter = new ViewPaperAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mVpWeatherReport.setAdapter(mVpWeatherReportAdapter);
        mTlWeatherReport.setupWithViewPager(mVpWeatherReport);
        mVpWeatherReport.setOffscreenPageLimit(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(menuItem);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NameCityCallBackEvent event) {
        if (event != null) {
            mTvCurrentCity.setText(event.getNameCity());
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        llContainer.setVisibility(View.INVISIBLE);
    }
}