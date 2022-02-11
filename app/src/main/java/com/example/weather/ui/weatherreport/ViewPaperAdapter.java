package com.example.weather.ui.weatherreport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.weather.ui.weatherreport.nextweek.NextWeekFragment;
import com.example.weather.ui.weatherreport.today.TodayFragment;
import com.example.weather.ui.weatherreport.tomorrow.TomorrowFragment;

public class    ViewPaperAdapter extends FragmentStatePagerAdapter {
    public ViewPaperAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new TomorrowFragment();
            case 2:
                return new NextWeekFragment();
            default:
                return new TodayFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Today";
                break;
            case 1:
                title = "Tomorrow";
                break;
            case 2:
                title = "Next Week";
                break;
        }
        return title;
    }
}
