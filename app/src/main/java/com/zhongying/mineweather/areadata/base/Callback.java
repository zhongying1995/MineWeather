package com.zhongying.mineweather.areadata.base;

/**
 * Created by Administrator on 2017/9/16.
 */

public interface ICallback {

    public  void preCallback();

    public  void postCallback();

    public  void onFailure();

    public  void onSucceed();

}
