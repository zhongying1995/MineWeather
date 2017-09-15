package com.zhongying.mineweather.okhttp;


import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/9/15.
 */

public class HttpUtil {

    public static void requestOkHttpUrl(String address,HttpCallback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
