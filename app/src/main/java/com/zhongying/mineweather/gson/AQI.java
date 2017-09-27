package com.zhongying.mineweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/25.
 */

public class AQI {

    @SerializedName("city")
    public City city;

    public class City{
        @SerializedName("qlty")
        public String quality;
    }

}
