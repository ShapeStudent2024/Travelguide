package com.example.travelguide.weather.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.example.travelguide.R;
import com.example.travelguide.weather.model.WeatherData;
import com.example.travelguide.weather.service.WeatherService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherCardView extends CardView {

    private TextView tvCityName;
    private TextView tvWeatherIcon;
    private TextView tvTemperature;
    private TextView tvWeatherDescription;
    private TextView tvFeelsLike;
    private TextView tvHumidity;
    private TextView tvWindSpeed;
    private TextView tvRefreshTime;
    private LinearLayout layoutLoading;
    private TextView tvErrorMessage;

    private WeatherService weatherService;
    private SimpleDateFormat timeFormat;

    public WeatherCardView(Context context) {
        super(context);
        init(context);
    }

    public WeatherCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeatherCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.weather_card, this, true);

        // 初始化视图
        initViews();

        // 初始化服务
        weatherService = new WeatherService(context);
        timeFormat = new SimpleDateFormat("HH:mm更新", Locale.getDefault());

        // 设置点击刷新
        setOnClickListener(v -> refreshWeather());

        // 自动加载天气数据
        refreshWeather();
    }

    private void initViews() {
        tvCityName = findViewById(R.id.tv_city_name);
        tvWeatherIcon = findViewById(R.id.tv_weather_icon);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvWeatherDescription = findViewById(R.id.tv_weather_description);
        tvFeelsLike = findViewById(R.id.tv_feels_like);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
        tvRefreshTime = findViewById(R.id.tv_refresh_time);
        layoutLoading = findViewById(R.id.layout_loading);
        tvErrorMessage = findViewById(R.id.tv_error_message);

        // 错误消息点击重试
        tvErrorMessage.setOnClickListener(v -> refreshWeather());
    }

    // 刷新天气数据
    public void refreshWeather() {
        showLoading();

        weatherService.getCurrentWeather(new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(WeatherData weather) {
                hideLoading();
                updateWeatherDisplay(weather);
            }

            @Override
            public void onError(String error) {
                hideLoading();
                showError(error);
            }
        });
    }

    // 更新天气显示
    private void updateWeatherDisplay(WeatherData weather) {
        tvCityName.setText(weather.getCityName());
        tvWeatherIcon.setText(weather.getWeatherEmoji());
        tvTemperature.setText(weather.getFormattedTemperature());
        tvWeatherDescription.setText(weather.getDescription());
        tvFeelsLike.setText(weather.getFormattedFeelsLike());
        tvHumidity.setText(weather.getFormattedHumidity());
        tvWindSpeed.setText(weather.getFormattedWindSpeed());
        tvRefreshTime.setText(timeFormat.format(new Date()));

        // 隐藏错误消息
        tvErrorMessage.setVisibility(View.GONE);

        // 显示天气信息
        showWeatherInfo();
    }

    // 显示加载状态
    private void showLoading() {
        layoutLoading.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.GONE);
        hideWeatherInfo();
    }

    // 隐藏加载状态
    private void hideLoading() {
        layoutLoading.setVisibility(View.GONE);
    }

    // 显示错误信息
    private void showError(String error) {
        tvErrorMessage.setText("Failed to obtain weather data, click to retry\n" + error);
        tvErrorMessage.setVisibility(View.VISIBLE);
        hideWeatherInfo();
    }

    // 显示天气信息
    private void showWeatherInfo() {
        tvCityName.setVisibility(View.VISIBLE);
        tvWeatherIcon.setVisibility(View.VISIBLE);
        tvTemperature.setVisibility(View.VISIBLE);
        tvWeatherDescription.setVisibility(View.VISIBLE);
        tvFeelsLike.setVisibility(View.VISIBLE);
        tvHumidity.setVisibility(View.VISIBLE);
        tvWindSpeed.setVisibility(View.VISIBLE);
        tvRefreshTime.setVisibility(View.VISIBLE);
    }

    // 隐藏天气信息
    private void hideWeatherInfo() {
        tvCityName.setVisibility(View.GONE);
        tvWeatherIcon.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvWeatherDescription.setVisibility(View.GONE);
        tvFeelsLike.setVisibility(View.GONE);
        tvHumidity.setVisibility(View.GONE);
        tvWindSpeed.setVisibility(View.GONE);
        tvRefreshTime.setVisibility(View.GONE);
    }
}