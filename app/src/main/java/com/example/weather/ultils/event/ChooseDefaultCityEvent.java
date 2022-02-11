package com.example.weather.ultils.event;

public class ChooseDefaultCityEvent {
    String cityName;

    public ChooseDefaultCityEvent(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
