package com.example.travelguide.weather.model;

public class WeatherData {

    private String cityName;
    private double temperature;
    private double feelsLike;
    private String description;
    private int humidity;
    private double windSpeed;
    private String iconCode;

    // 构造函数
    public WeatherData() {}

    public WeatherData(String cityName, double temperature, String description) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.description = description;
    }

    // Getters and Setters
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public String getIconCode() { return iconCode; }
    public void setIconCode(String iconCode) { this.iconCode = iconCode; }

    // 格式化温度显示
    public String getFormattedTemperature() {
        return Math.round(temperature) + "°C";
    }

    // 格式化体感温度
    public String getFormattedFeelsLike() {
        return "Somatosensory: " + Math.round(feelsLike) + "°C";
    }

    // 格式化湿度
    public String getFormattedHumidity() {
        return "Humidity: " + humidity + "%";
    }

    // 格式化风速
    public String getFormattedWindSpeed() {
        return "Wind speed: " + Math.round(windSpeed * 3.6) + "km/h";
    }

    // 根据天气代码获取简单的天气图标
    public String getWeatherEmoji() {
        if (iconCode == null) return "🌤️";

        switch (iconCode.substring(0, 2)) {
            case "01": return "☀️";  // clear sky
            case "02": return "⛅";  // few clouds
            case "03": return "☁️";  // scattered clouds
            case "04": return "☁️";  // broken clouds
            case "09": return "🌧️";  // shower rain
            case "10": return "🌦️";  // rain
            case "11": return "⛈️";  // thunderstorm
            case "13": return "❄️";  // snow
            case "50": return "🌫️";  // mist
            default: return "🌤️";
        }
    }
}