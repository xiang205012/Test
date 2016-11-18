package com.test.mytest;

import android.app.Application;

/**
 * Created by Administrator on 2016/1/6.
 */
public class MyApplication extends Application {

    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance(){
        return instance;
    }



}
