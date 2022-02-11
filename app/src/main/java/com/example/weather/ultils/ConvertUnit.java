package com.example.weather.ultils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConvertUnit {
    public static double fahrenheitToCelsius(double fahrenheit) {
        return Math.round(fahrenheit - 273.15);
    }

    public static String convertDayFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        String dayOfWeek = "";
        try {
            Date dateParsed = dateFormat.parse(date);
            assert dateParsed != null;
            dayOfWeek = weekDayFormat.format(dateParsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayOfWeek;
    }

    public static String iconPath(String subPath) {
        return Constant.ICON_BASE_URL + subPath + Constant.ICON_SUB_PARAM;
    }

    public static String getDateFromFt(long dt, String format) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(dt * 1000);
        Date date = cal.getTime();
        SimpleDateFormat weekDayFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return weekDayFormat.format(date);
    }
}
