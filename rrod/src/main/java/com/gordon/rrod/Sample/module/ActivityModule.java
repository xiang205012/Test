package com.gordon.rrod.Sample.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.gordon.rrod.Sample.custom.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gordon on 2016/5/29.
 */
@Module // 可以理解为这是一个创建实例的工厂
public class ActivityModule {

    // module中的创建属性时，必须同时创建以此属性同一类型的参数作为参数的构造方法
    // 如果有多个属性必须使用包含所有属性的一个构造方法，不能有多个构造方法

    // 如果module中有@Provides修饰的方法上有带参数，
    // 此参数是由带@Provides修饰的另一个方法返回值或者该返回值类型的类中带@Inject的构造方法

//    private final Activity activity;
//    private final Fragment fragment;
//   错误构造：
//    public ActivityModule(Activity activity){
//        this.activity = activity;
//    }
//
//    public ActivityModule(Fragment fragment){
//        this.fragment = fragment;
//    }
    // 正确构造：
//    public ActivityModule(Activity activity, Fragment fragment) {
//        this.activity = activity;
//        this.fragment = fragment;
//    }


    private final Activity activity;
    // 在baseActivity中初始化
    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @Provides
    @PerActivity // 定义作用为activity范围内
    Activity activity(){
        return this.activity;
    }

}
