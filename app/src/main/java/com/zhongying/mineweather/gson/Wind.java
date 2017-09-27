package com.zhongying.mineweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/25.
 */

public class Wind {

    @SerializedName("dir")
    public String direction;
    @SerializedName("sc")
    public String windForce;

}
