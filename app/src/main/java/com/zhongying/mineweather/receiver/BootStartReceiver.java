package com.zhongying.mineweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhongying.mineweather.activity.WeatherActivity;
import com.zhongying.mineweather.constant.Constant;
import com.zhongying.mineweather.service.AutoUpdateService;
import com.zhongying.mineweather.utily.SharedPreferencesManager;

/**
 * @class; 开机自启
 */
public class BootStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        //开启后台更新数据服务
        if(!SharedPreferencesManager.getInstance()
                .getBoolean(Constant.SHARED_KEY_IS_CLOSE_AUTO_UPDATE)){
            Intent it = new Intent(context, AutoUpdateService.class);
            context.startService(it);
        }

        //开机自启
        if(!SharedPreferencesManager.getInstance()
                .getBoolean(Constant.SHARED_KEY_IS_CLOSE_BOOT_START)){
            Intent it = new Intent(context, WeatherActivity.class);
            context.startActivity(it);
        }
    }
}
