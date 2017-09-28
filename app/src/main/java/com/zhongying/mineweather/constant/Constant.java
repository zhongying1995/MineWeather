package com.zhongying.mineweather.constant;

/**
 * @Class: 记录整个程序中的常量
 * Created by Administrator on 2017/9/14.
 */

public class Constant {

    //个人Id
    public static String URLKEY_FOR_HEWEATHER = "2832c11c7a8f4dea92295cd180afb0b8";

    //请求Url样式
    /*
        https://free-api.heweather.com/v5/weather?city=yourcity&key=yourkey
    */

    //请求每日必应一图的 url
    public static final String BACKGROUND_PICTURE_URL = "http://guolin.tech/api/bing_pic";

    //pre使用的保存 背景图 的 key
    public static final String SHARED_KEY_BACKGROUND = "background";

    //当前的weatherId
    public static final String SHARED_KEY_WEATHERID = "weatherId";

    //当前的weather，保存的天气jsonString
    public static final String SHARED_KEY_WEATHER_JSON = "weather";

    //当前的自动更新间隔
    public static final String  SHARED_KEY_AUTO_UPDATE_TIME = "aotuUpdateTime";

}
