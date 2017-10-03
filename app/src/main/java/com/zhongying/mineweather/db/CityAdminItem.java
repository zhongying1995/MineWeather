package com.zhongying.mineweather.db;

import org.litepal.crud.DataSupport;

/**
 * @class: 被收藏的每一个城市对象细节；维护在页面上将会展示的内容
 * Created by Administrator on 2017/9/30.
 */

public class CityAdminItem extends DataSupport {

    private int id;

    private String cityName;

    private String  cityWeatherId;

    private String iconCode;

    private String temperature;

    private String minTemperature;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityWeatherId() {
        return cityWeatherId;
    }

    public void setCityWeatherId(String cityWeatherId) {
        this.cityWeatherId = cityWeatherId;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }


    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindForce() {
        return windForce;
    }

    public void setWindForce(String windForce) {
        this.windForce = windForce;
    }

    private String maxTemperature;

    private String airQuality;

    private String windDir;

    private String windForce;


}
