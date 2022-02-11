package com.example.weather.ui.about;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.weather.R;
import com.example.weather.ultils.Pref;
import com.example.weather.ultils.PrefDefault;
import com.example.weather.ultils.event.ChooseCityEvent;
import com.example.weather.ultils.event.ChooseDefaultCityEvent;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class AboutFragment extends DialogFragment {
    private EditText mEdtDefaultCity;
    private Button mBtnSet;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.about_frag, null);
        initView(view);
        initListener();
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void initView(View view) {
        mEdtDefaultCity = view.findViewById(R.id.edtDefaultCity);
        mBtnSet = view.findViewById(R.id.btnSet);
    }

    private void initListener() {
        mBtnSet.setOnClickListener(v -> {
            EventBus.getDefault().post(new ChooseDefaultCityEvent(mEdtDefaultCity.getText().toString().trim()));
            dismiss();
        });
    }
}
