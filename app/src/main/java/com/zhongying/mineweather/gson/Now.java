package com.zhongying.mineweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/25.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public Condition condition;


}
