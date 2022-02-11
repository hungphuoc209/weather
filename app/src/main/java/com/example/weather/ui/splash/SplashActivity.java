package com.example.weather.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.R;
import com.example.weather.ui.weatherreport.WeatherReportActivity;

public class SplashActivity extends AppCompatActivity {
    Button mBtnExplore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_act);
        initView();
        initListener();
    }


    private void initView() {
        mBtnExplore = findViewById(R.id.btnExplore);
    }

    private void initListener() {
        mBtnExplore.setOnClickListener(v -> {
            Intent weatherReportIntent = new Intent(this, WeatherReportActivity.class);
            startActivity(weatherReportIntent);
            finish();
        });
    }

}