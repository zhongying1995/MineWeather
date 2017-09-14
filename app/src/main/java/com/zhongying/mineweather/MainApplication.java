package com.zhongying.mineweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2017/9/14.
 */

public class MainApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
