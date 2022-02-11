package com.example.weather.model.dailyweather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.weather.ultils.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

@Entity(tableName = Constant.DAILY_TABLE_NAME)
public class DailyWeatherRespond {
    @SerializedName("city")
    @Expose
    private DailyCity dailyCity;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private Double message;
    @SerializedName("cnt")
    @Expose
    @PrimaryKey
    @NonNull
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private List<DailyWeather> list;

    public DailyWeatherRespond(DailyCity dailyCity, String cod, Double message, @NotNull Integer cnt, List<DailyWeather> list) {
        this.dailyCity = dailyCity;
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
    }

    public DailyCity getDailyCity() {
        return dailyCity;
    }

    public void setDailyCity(DailyCity dailyCity) {
        this.dailyCity = dailyCity;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    @NotNull
    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(@NotNull Integer cnt) {
        this.cnt = cnt;
    }

    public List<DailyWeather> getList() {
        return list;
    }

    public void setList(List<DailyWeather> list) {
        this.list = list;
    }
}
