package com.zhongying.mineweather.areadata.base;

import android.util.Log;

import com.zhongying.mineweather.okhttp.HttpCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/16.
 */

public abstract class Callback extends HttpCallback{

    private String TAG = "ChooseAreaFragment";

    private String  mResponseText;

    @Override
    public void onFailure(Call call, IOException e) {
        onFailure(call);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response == null){
            onFailure(call);
        }
        this.mResponseText = response.body().string();
        Log.i(TAG,mResponseText);
        onSucceed( call, response);
    }

    public abstract void onFailure(Call call);

    public String getResponseText(){
        return mResponseText;
    }

    public void setResponseTextNull(){
        this.mResponseText = null;
    }

    public abstract void onSucceed(Call call, Response response)throws IOException;

    public abstract void onPreResponse();

    public abstract void onPostResponse();

}
