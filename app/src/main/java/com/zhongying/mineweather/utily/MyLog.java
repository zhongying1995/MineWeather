package com.zhongying.mineweather.utily;

import android.util.Log;

/**
 * Created by Administrator on 2017/10/1.
 */

public class MyLog {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    public static int level = VERBOSE;

    public static void v(String tag,String info){
        if(level<=VERBOSE){
            Log.v(tag,info);
        }
    }

    public static void d(String tag,String info){
        if(level<=DEBUG){
            Log.d(tag,info);
        }
    }

    public static void i(String tag,String info){
        if(level<=INFO){
            Log.i(tag,info);
        }
    }

    public static void w(String tag,String info){
        if(level<=WARN){
            Log.w(tag,info);
        }
    }

    public static void e(String tag,String info){
        if(level<=ERROR){
            Log.e(tag,info);
        }
    }


}
