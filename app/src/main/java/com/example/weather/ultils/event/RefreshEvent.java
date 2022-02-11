package com.example.weather.ultils.event;

public class RefreshEvent {
    boolean isRefresh;

    public RefreshEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
