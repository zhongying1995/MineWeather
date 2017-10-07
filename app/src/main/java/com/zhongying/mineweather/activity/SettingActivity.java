package com.zhongying.mineweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.constant.Constant;
import com.zhongying.mineweather.utily.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/6.
 */

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener,AdapterView.OnItemSelectedListener{
    private String TAG = "SettingActivity";

    private TextView mGoBack_tv,mAboutApp_tv;

    private CheckBox mBootStart_cb,mAutoUpdate_cb;

    private Spinner mSpinner;
    private List<String> mList;
    private ArrayAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
        Log.i(TAG,"onCreate()>>>>>>>>>>>");
        initView();
        initSpinner();
    }

    public static void startSettingActivity(Context context){
        Intent it = new Intent(context,SettingActivity.class);
        context.startActivity(it);
    }

    private void initView(){
        mGoBack_tv = (TextView) findViewById(R.id.go_back_tv);
        mGoBack_tv.setOnClickListener(this);
        mAboutApp_tv = (TextView) findViewById(R.id.about_app_tv);
        mAboutApp_tv.setOnClickListener(this);
        mBootStart_cb = (CheckBox) findViewById(R.id.boot_start_cb);
        mBootStart_cb.setOnCheckedChangeListener(this);
        mAutoUpdate_cb = (CheckBox) findViewById(R.id.auto_update_cb);
        mAutoUpdate_cb.setOnCheckedChangeListener(this);
        mSpinner = (Spinner) findViewById(R.id.update_time_spinner);

        if(SharedPreferencesManager.getInstance()
                .getBoolean(Constant.SHARED_KEY_IS_CLOSE_AUTO_UPDATE)){
            mAutoUpdate_cb.setChecked(false);
            mSpinner.setVisibility(View.INVISIBLE);
        }
        if(SharedPreferencesManager.getInstance().getBoolean(Constant.SHARED_KEY_IS_CLOSE_BOOT_START)){
            mBootStart_cb.setChecked(false);
        }

    }

    private void initSpinner(){
        mList = new ArrayList<>();
        mList.add("30分钟");
        mList.add("1小时");
        mList.add("2小时");
        mList.add("4小时");
        mAdapter = new ArrayAdapter<String>
                (SettingActivity.this,android.R.layout.simple_list_item_1,mList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.boot_start_cb:
                if(isChecked){
                    SharedPreferencesManager.getInstance()
                            .putBoolean(Constant.SHARED_KEY_IS_CLOSE_BOOT_START,false);
                }else {
                    SharedPreferencesManager.getInstance()
                            .putBoolean(Constant.SHARED_KEY_IS_CLOSE_BOOT_START,true);
                }
                break;
            case R.id.auto_update_cb:
                if(isChecked){
                    SharedPreferencesManager.getInstance()
                            .putBoolean(Constant.SHARED_KEY_IS_CLOSE_AUTO_UPDATE,false);
                    mSpinner.setVisibility(View.VISIBLE);
                }else {
                    SharedPreferencesManager.getInstance()
                            .putBoolean(Constant.SHARED_KEY_IS_CLOSE_AUTO_UPDATE,true);
                    mSpinner.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_back_tv:
                finish();
                break;
            case R.id.about_app_tv:
                showAboutAppDialog();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.update_time_spinner:
                SharedPreferencesManager.getInstance()
                        .putInt(Constant.SHARED_KEY_AUTO_UPDATE_TIME,position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showAboutAppDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("关于");
        builder.setMessage("文字天气为本人第一个独立完成的作品，有诸多问题，谢谢包涵。 " +
                "\n若您有好的建议，可以发送邮件到1424249327@qq.com，感谢您的来信 " +
                "\n祝您生活健康愉快");
        builder.setCancelable(true);
        builder.setIcon(R.mipmap.icon100);
        builder.show();
    }

}
