package com.example.weather.base;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BaseActivity extends AppCompatActivity {
    protected CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose();
    }
}
