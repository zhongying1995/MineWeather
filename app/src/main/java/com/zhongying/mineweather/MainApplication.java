package com.zhongying.mineweather;

import android.app.Application;
import android.content.Context;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2017/9/14.
 */

public class MainApplication extends Application {

    private static Context context;

    /*
        ShareSDK
     */
    {
        PlatformConfig.setQQZone("1106385495", "RR1BorUWETd1ivhu");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //LitePal初始化
        LitePal.initialize(context);
        //ShareSDK初始化
        UMShareAPI.get(this);
    }

    public static Context getContext(){
        return context;
    }
}
