package com.zhongying.mineweather.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.fragment.base.BaseFragment;

/**
 * Created by Administrator on 2017/9/19.
 */

public class WeatherFragment extends BaseFragment implements View.OnClickListener,View.OnLongClickListener{

    private String TAG = "WeatherFragment";

    private ImageView mChooseArea_iv;

    private ImageView mPlusArea_iv,mSetting_iv,mShare_iv;

    boolean isHadLongClick = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG,"onAttach()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView()");
        View view = inflater.inflate(R.layout.fragment_weather_layout,container,false);
        initView( view);
        return view;
    }

    private void initView(View view){
        mChooseArea_iv = (ImageView) view.findViewById(R.id.to_choose_area_iv);
        mChooseArea_iv.setOnClickListener(this);
        mPlusArea_iv = (ImageView) view.findViewById(R.id.plus_area_iv);
        mPlusArea_iv.setOnClickListener(this);
        mPlusArea_iv.setOnLongClickListener(this);
        mSetting_iv = (ImageView) view.findViewById(R.id.setting_iv);
        mSetting_iv.setOnClickListener(this);
        mShare_iv = (ImageView) view.findViewById(R.id.share_iv);
        mShare_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.to_choose_area_iv:
                Log.i(TAG,"to_choose_area_iv()");
                break;
            case R.id.plus_area_iv:
                Log.i(TAG,"onClick()");
                if(isHadLongClick){
                //收起两个ImageView
                    closeWindow();
                    break;
                }

                break;
            case R.id.setting_iv:

                break;
            case R.id.share_iv:
                Log.i(TAG,"share_iv 被点击!");
                break;

        }
    }

    private void openWindow(){
        mPlusArea_iv.setImageResource(R.mipmap.plus);
        isHadLongClick = false;

        ObjectAnimator animatorPlus = ObjectAnimator.
                ofFloat(mSetting_iv,"translationY",0,250f);
        ObjectAnimator animatorShare = ObjectAnimator.
                ofFloat(mShare_iv,"translationY",0,500f);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animatorPlus,animatorShare);
        set.setInterpolator(new BounceInterpolator());
        set.setDuration(120);
        set.start();
    }

    private void closeWindow(){
        mPlusArea_iv.setImageResource(R.mipmap.plus);
        isHadLongClick = false;

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorPlus;
        ObjectAnimator animatorShare;
        animatorPlus = ObjectAnimator.
                ofFloat(mSetting_iv,"translationY",250f,0);
        animatorShare = ObjectAnimator.
                ofFloat(mShare_iv,"translationY",500f,0);
        set.playTogether(animatorPlus,animatorShare);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSetting_iv.setVisibility(View.INVISIBLE);
                mShare_iv.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.plus_area_iv:
                Log.i(TAG,"onLongClick()");

                if(isHadLongClick){
                //收起两个imageView
                    closeWindow();
                }else {
                //展开两个imageView
                   openWindow();
                }

                break;
        }
        return true;
    }
}
