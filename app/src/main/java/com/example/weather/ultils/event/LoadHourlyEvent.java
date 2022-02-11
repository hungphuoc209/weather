package com.example.weather.ultils.event;

public class LoadHourlyEvent {
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LoadHourlyEvent(String cityName) {
        this.cityName = cityName;
    }

    String cityName;
}
