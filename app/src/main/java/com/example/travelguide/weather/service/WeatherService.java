package com.example.travelguide.weather.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.example.travelguide.weather.model.WeatherData;
import okhttp3.*;
import java.io.IOException;

public class WeatherService {

    private static final String API_KEY = "38d629ec151e6cec155a8c50d0b4dca9";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private Context context;
    private LocationManager locationManager;
    private OkHttpClient client;
    private Gson gson;

    public interface WeatherCallback {
        void onSuccess(WeatherData weather);
        void onError(String error);
    }

    public WeatherService(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    // 获取当前位置的天气
    public void getCurrentWeather(WeatherCallback callback) {
        // 检查权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，使用默认城市（香港）
            getWeatherByLocation(22.3193, 114.1694, callback);
            return;
        }

        // 先尝试获取上次已知位置
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastLocation != null) {
            getWeatherByLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), callback);
            return;
        }

        // 如果没有缓存位置，请求新的位置
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationManager.removeUpdates(this);
                getWeatherByLocation(location.getLatitude(), location.getLongitude(), callback);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        try {
            // 请求位置更新（网络定位，更快）
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);

            // 5秒后如果还没有结果，使用默认位置
            new android.os.Handler().postDelayed(() -> {
                locationManager.removeUpdates(locationListener);
                getWeatherByLocation(22.3193, 114.1694, callback); // 香港
            }, 5000);

        } catch (SecurityException e) {
            // 权限异常，使用默认位置
            getWeatherByLocation(22.3193, 114.1694, callback);
        }
    }

    // 根据经纬度获取天气
    private void getWeatherByLocation(double lat, double lon, WeatherCallback callback) {
        String url = BASE_URL + "?lat=" + lat + "&lon=" + lon +
                "&appid=" + API_KEY + "&units=metric&lang=zh_cn";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 回到主线程
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                        callback.onError("Network request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                            callback.onError("Request failed, status code: " + response.code()));
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    WeatherData weather = parseWeatherData(responseBody);

                    // 回到主线程
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                            callback.onSuccess(weather));

                } catch (Exception e) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                            callback.onError("Data parsing failed: " + e.getMessage()));
                }
            }
        });
    }

    // 解析天气数据
    private WeatherData parseWeatherData(String jsonString) {
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

        WeatherData weather = new WeatherData();

        // 城市名
        weather.setCityName(json.get("name").getAsString());

        // 温度信息
        JsonObject main = json.getAsJsonObject("main");
        weather.setTemperature(main.get("temp").getAsDouble());
        weather.setFeelsLike(main.get("feels_like").getAsDouble());
        weather.setHumidity(main.get("humidity").getAsInt());

        // 天气描述
        JsonObject weatherInfo = json.getAsJsonArray("weather").get(0).getAsJsonObject();
        weather.setDescription(weatherInfo.get("description").getAsString());
        weather.setIconCode(weatherInfo.get("icon").getAsString());

        // 风速
        if (json.has("wind")) {
            JsonObject wind = json.getAsJsonObject("wind");
            weather.setWindSpeed(wind.get("speed").getAsDouble());
        }

        return weather;
    }
}