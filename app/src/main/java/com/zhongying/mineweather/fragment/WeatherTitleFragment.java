package com.zhongying.mineweather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.fragment.base.BaseFragment;

/**
 * Created by Administrator on 2017/9/18.
 */

public class WeatherTitleFragment extends BaseFragment implements View.OnClickListener {

    private Context context;

    private ImageView mChooseArea_iv;

    private ImageView mPlusArea_iv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather_title_layout,container,false);

        initView(view);

        return view;
    }

    private void initView(View view){
        mChooseArea_iv = (ImageView) view.findViewById(R.id.to_choose_area_iv);
        mChooseArea_iv.setOnClickListener(this);
        mPlusArea_iv = (ImageView) view.findViewById(R.id.plus_area_iv);
        mPlusArea_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
