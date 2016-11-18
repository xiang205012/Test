package com.gordon.rrod.Sample.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gordon.rrod.MyApplication;
import com.gordon.rrod.Sample.component.ApplicationComponent;
import com.gordon.rrod.Sample.module.ActivityModule;

/**
 * Created by gordon on 2016/5/29.
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将全局的注入到Activity
        this.getApplicationComponent().inject(this);

    }

    protected ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }

    protected ApplicationComponent getApplicationComponent(){
        return ((MyApplication)getApplication()).getApplicationComponent();
    }

}
