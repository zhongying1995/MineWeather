package com.zhongying.mineweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/9/25.
 */

public class Condition {

    @SerializedName("code")
    public String code;
    @SerializedName("txt")
    public String info;

}
