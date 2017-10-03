package com.zhongying.mineweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;

import com.zhongying.mineweather.constant.Constant;
import com.zhongying.mineweather.utily.SharedPreferencesManager;
import com.zhongying.mineweather.gson.HeWeather;
import com.zhongying.mineweather.okhttp.HttpCallback;
import com.zhongying.mineweather.okhttp.HttpUtil;
import com.zhongying.mineweather.utily.Utilies;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = SharedPreferencesManager.getInstance()
                .getInt(Constant.SHARED_KEY_AUTO_UPDATE_TIME);
        if(hour<1){
            hour =1;
        }
        int intervalTime = hour * 60 *60 *1000;
        long triggerTime = SystemClock.elapsedRealtime() + intervalTime;

        Intent it = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,it,0);

        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /*
        自动更新天气信息
     */
    private void updateWeather(){
        String weatherId = SharedPreferencesManager.getInstance()
                .getString(Constant.SHARED_KEY_WEATHER_ID);
        if(weatherId == null || TextUtils.isEmpty(weatherId)){
            return;
        }
        String url = Utilies.getWeatherUrl(weatherId);
        HttpUtil.requestOkHttpUrl(url, new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String weatherText = response.body().string();
                HeWeather weather = Utilies.handleWeatherResponse(weatherText);
                if(Utilies.isWeatherResponseAvailable(weather)){
                    SharedPreferencesManager.getInstance()
                            .putString(Constant.SHARED_KEY_WEATHER_JSON,weatherText);
                }

            }
        });

    }

}
