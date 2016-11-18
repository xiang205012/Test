package com.gordon.rrod.Dagger2;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gordon on 2016/6/16.
 */
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @Provides
    Activity activity(){
        return this.activity;
    }



}
