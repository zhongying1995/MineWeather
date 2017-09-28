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
    public Temperature temp;

    @SerializedName("wind")
    public Wind wind;

    public class Condition {
        @SerializedName("code_d")
        public String code;
        @SerializedName("txt_d")
        public String info;
    }

    public class Astronomy{
        @SerializedName("sr")
        public String sunrise;
        @SerializedName("ss")
        public String sundown;
    }

    public class Temperature {
        @SerializedName("max")
        public String maxTemp;
        @SerializedName("min")
        public String minTemp;
    }

}
