package com.zhongying.mineweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;

import com.zhongying.mineweather.constant.Constant;
import com.zhongying.mineweather.db.CityAdminItem;
import com.zhongying.mineweather.db.CityAdminItemManager;
import com.zhongying.mineweather.gson.HeWeather;
import com.zhongying.mineweather.okhttp.HttpCallback;
import com.zhongying.mineweather.okhttp.HttpUtil;
import com.zhongying.mineweather.utily.SharedPreferencesManager;
import com.zhongying.mineweather.utily.Utilies;

import java.io.IOException;
import java.util.List;

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
        updateCurrentWeather();
        updateColletcedCityWeather();
        timingAwaken();

        return super.onStartCommand(intent, flags, startId);
    }

    /*
        自动更新天气主界面上的天气信息
     */
    private void updateCurrentWeather(){
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

    /**
     * @function： 自动更新收藏的城市的天气
     */
    private void updateColletcedCityWeather(){
        List<CityAdminItem> list = CityAdminItemManager.getInstance().findAll();
        if(list.size()==0){
            return;
        }
        for(CityAdminItem city:list){
            String url = Utilies.getWeatherUrl(city.getCityWeatherId());
            HttpUtil.requestOkHttpUrl(url, new HttpCallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String text = response.body().string();
                    HeWeather weather = Utilies.handleWeatherResponse(text);
                    if(Utilies.isWeatherResponseAvailable(weather)){
                        CityAdminItem city = new CityAdminItem();
                        city.setCityName(weather.basic.cityName);
                        city.setAirQuality(weather.aqi.city.quality);
                        city.setCityWeatherId(weather.basic.weatherId);
                        city.setIconCode(weather.now.condition.code);
                        city.setTemperature(weather.now.temperature);
                        city.setMinTemperature(weather.dailyForecastList.get(0).temp.minTemp);
                        city.setMaxTemperature(weather.dailyForecastList.get(0).temp.maxTemp);
                        city.setWindDir(weather.dailyForecastList.get(0).wind.direction);
                        city.setWindForce(weather.dailyForecastList.get(0).wind.windForce);

                        CityAdminItemManager.getInstance().update(city);
                    }
                }
            });
        }
    }


    private void timingAwaken(){
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = SharedPreferencesManager.getInstance()
                .getInt(Constant.SHARED_KEY_AUTO_UPDATE_TIME);
        int intervalTime;
        switch (hour){
            case 0:
                intervalTime = 60 *60 *1000 /2;
                break;
            case 1:
                intervalTime =  60 *60 *1000 ;
                break;
            case 2:
                intervalTime =  60 *60 *1000 *2;
                break;
            case 3:
                intervalTime =  60 *60 *1000 *4;
                break;
            default:
                intervalTime = 60 *60 *1000 /2;
                break;
        }

        long triggerTime = SystemClock.elapsedRealtime() + intervalTime;

        Intent it = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,it,0);

        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pi);
    }

}
