package com.zhongying.mineweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongying.mineweather.R;
import com.zhongying.mineweather.fragment.base.BaseFragment;

/**
 * @class: 城市选择界面的Fragment
 * Created by Administrator on 2017/9/14.
 */

public class AreaFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_choose_area_layout,container,false);
        return view;
    }
}
