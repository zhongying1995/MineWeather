package com.zhongying.mineweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class HeWeather {

    @SerializedName("status")
    public String status;

    @SerializedName("aqi")
    public AQI aqi;

    @SerializedName("basic")
    public Basic basic;

    @SerializedName("daily_forecast")
    public List<DailyForecast> dailyForecastList;

    @SerializedName("hourly_forecast")
    public List<HourlyForecast> hourlyForecastList;

    @SerializedName("now")
    public Now now;

    @SerializedName("suggestion")
    public Suggestion suggestion;

}
