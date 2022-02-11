package com.example.weather.ultils.event;

public class NameCityCallBackEvent {
    private String nameCity;

    public NameCityCallBackEvent(String nameCity) {
        this.nameCity = nameCity;

    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }
}
