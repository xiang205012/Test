package com.gordon.rrod;

import android.app.Application;

import com.gordon.rrod.Sample.component.ApplicationComponent;

/**
 * Created by gordon on 2016/5/26.
 */
public class MyApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化applicationComponent
//        this.applicationComponent

    }


    public ApplicationComponent getApplicationComponent(){
        return this.applicationComponent;
    }


}
