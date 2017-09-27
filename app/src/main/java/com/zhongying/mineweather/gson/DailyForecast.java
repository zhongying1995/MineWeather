package com.zhongying.mineweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/25.
 */

public class DailyForecast {

    @SerializedName("astro")
    public Astronomy astronomy;

    @SerializedName("cond")
    public Condition condition;

    @SerializedName("tmp")
    public Temperation temp;

    @SerializedName("wind")
    public Wind wind;

    public class Astronomy{
        @SerializedName("sr")
        public String sunrise;
        @SerializedName("ss")
        public String sundown;
    }

    public class Temperation{
        @SerializedName("max")
        public String maxTemp;
        @SerializedName("min")
        public String minTemp;
    }

}
