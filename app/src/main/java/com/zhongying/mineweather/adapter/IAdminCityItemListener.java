package com.zhongying.mineweather.adapter;

/**
 * Created by Administrator on 2017/10/1.
 */

public interface IAdminCityItemListener {

    public void deleteCity(final String weatherId);

    public void exchangeCityWeather(final String weatherId);

    public void addCity();
}
