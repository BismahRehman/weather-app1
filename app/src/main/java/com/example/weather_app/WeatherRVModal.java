package com.example.weather_app;

public class WeatherRVModal {

    private String time;
    private String temperature;
    private String windspeed;
    private String icon;

    public WeatherRVModal(String time, String temperature, String windspeed, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.windspeed = windspeed;
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImageUrl() {
        // Construct the full image URL using the icon path
        return "http://example.com/images/" + icon; // Change this to your actual image base URL
    }
}
