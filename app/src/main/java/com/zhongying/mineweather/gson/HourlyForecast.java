package com.zhongying.mineweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/25.
 */

public class HourlyForecast {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("cond")
    public Condition cond;


}
