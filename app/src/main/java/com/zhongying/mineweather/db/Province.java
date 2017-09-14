package com.zhongying.mineweather.db;

import org.litepal.crud.DataSupport;

/**
 * @Class： 省份级Json数据的保存对象
 * Created by Administrator on 2017/9/14.
 */

public class Province extends DataSupport {

    private int id;

    private String provinceName;

    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }


}
