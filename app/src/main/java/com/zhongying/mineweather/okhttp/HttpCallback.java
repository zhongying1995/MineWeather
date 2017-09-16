package com.zhongying.mineweather.okhttp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @class: 重构okhttp自带的Callback
 * Created by Administrator on 2017/9/15.
 */

public abstract  class HttpCallback implements Callback {
    @Override
    public abstract void onFailure(Call call, IOException e) ;

    @Override
    public abstract void onResponse(Call call, Response response) throws IOException ;
}
