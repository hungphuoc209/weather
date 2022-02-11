package com.example.weather.model.daysweather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.weather.ultils.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

@Entity(tableName = Constant.HOURLY_TABLE_NAME)
public class HourlyWeatherRespond {
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private Double message;
    @SerializedName("cnt")
    @Expose
    @NonNull
    @PrimaryKey
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private List<HourlyWeather> list;
    @SerializedName("city")
    @Expose
    private City city;

    public HourlyWeatherRespond(String cod, Double message, @NotNull Integer cnt, List<HourlyWeather> list, City city) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
        this.city = city;
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

    public List<HourlyWeather> getList() {
        return list;
    }

    public void setList(List<HourlyWeather> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
