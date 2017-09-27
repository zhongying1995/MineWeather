package com.zhongying.mineweather.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhongying.mineweather.MainApplication;

/**
 * @class: 使用单例模式封装 sharedPreferences
 * Created by Administrator on 2017/9/27.
 */

public class SharedPreferencesManager {

    private static SharedPreferencesManager mInstance;

    private static SharedPreferences sp;

    private static SharedPreferences.Editor editor;

    //shared 的名字
    private static final String SHARED_PRE_NAME = "weather.pre";

    private SharedPreferencesManager(){
        sp = MainApplication.getContext().
                getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
    }

    public static SharedPreferencesManager getInstance(){
        return Holder.sharedPreferenceManager;
    }

    private static class Holder{
        private static SharedPreferencesManager sharedPreferenceManager
                = new SharedPreferencesManager();
    }

    //对int进行读写
    public void putInt(String key,int value){
        editor.putInt(key, value);
        editor.commit();
    }
    public int getInt(String key){
        return getInt(key,0);
    }
    public int getInt(String key,int defaultValue){
        return sp.getInt(key, defaultValue);
    }

    //对String进行读写
    public void putString(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }
    public String getString(String key){
        return getString(key,null);
    }
    public String getString(String key,String defaultString){
        return sp.getString(key,defaultString);
    }


}
