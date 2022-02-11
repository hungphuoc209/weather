package com.example.weather.ultils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.pixplicity.easyprefs.library.Prefs;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public class AppUtils {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static boolean isLimited() {
        return (Calendar.getInstance().getTimeInMillis() - Prefs.getLong(Pref.TIME_LAST_QUERY, 0)) > Constant.TIME_LIMIT_QUERY;
    }
}
