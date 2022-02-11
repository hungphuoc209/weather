package com.example.weather.ultils;

import androidx.room.TypeConverter;

import com.example.weather.model.common.Clouds;
import com.example.weather.model.common.Coord;
import com.example.weather.model.common.Main;
import com.example.weather.model.common.Rain;
import com.example.weather.model.common.Weather;
import com.example.weather.model.common.Wind;
import com.example.weather.model.currentweather.Sys;
import com.example.weather.model.dailyweather.DailyCity;
import com.example.weather.model.dailyweather.DailyWeather;
import com.example.weather.model.dailyweather.DailyWeatherRespond;
import com.example.weather.model.dailyweather.FeelLike;
import com.example.weather.model.dailyweather.Temp;
import com.example.weather.model.daysweather.City;
import com.example.weather.model.daysweather.HourlyWeather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ConvertJson {
    ////Object to string
    //commom
    @TypeConverter
    public String convertToString(Coord coord) {
        return new Gson().toJson(coord);
    }

    @TypeConverter
    public String convertToString(Clouds clouds) {
        return new Gson().toJson(clouds);
    }

    @TypeConverter
    public String convertToString(Main main) {
        return new Gson().toJson(main);
    }

    @TypeConverter
    public String convertToString(Rain rain) {
        return new Gson().toJson(rain);
    }

    @TypeConverter
    public String convertToString(List<Weather> weather) {
        return new Gson().toJson(weather);
    }

    @TypeConverter
    public String convertToString(Wind wind) {
        return new Gson().toJson(wind);
    }

    //current
    @TypeConverter
    public String convertString(Sys sys) {
        return new Gson().toJson(sys);
    }

    //fiveDay
    @TypeConverter
    public String convertString(City city) {
        return new Gson().toJson(city);
    }

    @TypeConverter
    public String convertStringHourly(List<HourlyWeather> hourlyWeathers) {
        return new Gson().toJson(hourlyWeathers);
    }


    @TypeConverter
    public String convertString(DailyCity dailyCity) {
        return new Gson().toJson(dailyCity);
    }

    //dailyWeather
    @TypeConverter
    public String convertStringDaily(List<DailyWeather> dailyWeathers) {
        return new Gson().toJson(dailyWeathers);

    }

    @TypeConverter
    public String convertString(DailyWeatherRespond dailyWeatherRespond) {
        return new Gson().toJson(dailyWeatherRespond);
    }

    @TypeConverter
    public String convertString(FeelLike feelLike) {
        return new Gson().toJson(feelLike);
    }

    @TypeConverter
    public String convertString(Temp temp) {
        return new Gson().toJson(temp);
    }

    ////String to Object
    //common
    @TypeConverter
    public Coord convertObjectCoord(String string) {
        return new Gson().fromJson(string, Coord.class);
    }

    @TypeConverter
    public Clouds convertObjectClouds(String string) {
        return new Gson().fromJson(string, Clouds.class);
    }

    @TypeConverter
    public Main convertObjectMain(String string) {
        return new Gson().fromJson(string, Main.class);
    }

    @TypeConverter
    public Rain convertObjectRain(String string) {
        return new Gson().fromJson(string, Rain.class);
    }

    @TypeConverter
    public List<Weather> convertObjectWeather(String string) {
        return new Gson().fromJson(string, new TypeToken<List<Weather>>() {
        }.getType());
    }

    @TypeConverter
    public Wind convertObjectWind(String string) {
        return new Gson().fromJson(string, Wind.class);
    }

    //current
    @TypeConverter
    public Sys convertObjectSys(String string) {
        return new Gson().fromJson(string, Sys.class);
    }

    //fiveDay
    @TypeConverter
    public City convertObjectSCity(String string) {
        return new Gson().fromJson(string, City.class);
    }

    @TypeConverter
    public List<HourlyWeather> convertObjectHourly(String string) {
        return new Gson().fromJson(string, new TypeToken<List<HourlyWeather>>() {
        }.getType());
    }

    //daily
    @TypeConverter
    public DailyCity convertObjectSDailyCity(String string) {
        return new Gson().fromJson(string, DailyCity.class);
    }

    @TypeConverter
    public List<DailyWeather> convertObjectDailyWeather(String string) {
        return new Gson().fromJson(string, new TypeToken<List<DailyWeather>>() {
        }.getType());
    }

    @TypeConverter
    public DailyWeatherRespond convertObjectDailyWeatherRespond(String string) {
        return new Gson().fromJson(string, DailyWeatherRespond.class);
    }

    @TypeConverter
    public FeelLike convertObjectFeelLike(String string) {
        return new Gson().fromJson(string, FeelLike.class);
    }

    @TypeConverter
    public Temp convertObjectTemp(String string) {
        return new Gson().fromJson(string, Temp.class);
    }
}
