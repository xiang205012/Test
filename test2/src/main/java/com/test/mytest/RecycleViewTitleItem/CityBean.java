package com.test.mytest.RecycleViewTitleItem;

import android.util.Log;

/**
 * Created by gordon on 2016/11/3.
 */

public class CityBean {

    /**城市名*/
    private String cityName;
    /**城市名拼音*/
    private String cityNamePinyin;
    /**城市名拼音首字母*/
    private String cityNamePinyinFirstLetter;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNamePinyin() {
        return cityNamePinyin;
    }

    public void setCityNamePinyin(String cityNamePinyin) {
        this.cityNamePinyin = cityNamePinyin;
    }

    public String getCityNamePinyinFirstLetter() {
        return cityNamePinyinFirstLetter;
    }

    public void setCityNamePinyinFirstLetter(String cityNamePinyinFirstLetter) {
        Log.d("tag"," 数据引用 改变原数据 : ");
        this.cityNamePinyinFirstLetter = cityNamePinyinFirstLetter;
    }

    public CityBean(String cityName){
        this.cityName = cityName;
    }

}
