package com.example.weather.ultils.event;

public class ChooseCityEvent {
    private String cityName;

    public ChooseCityEvent(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
