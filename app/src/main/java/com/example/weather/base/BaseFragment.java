package com.example.weather.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BaseFragment extends Fragment {
    protected CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose();
    }
}
