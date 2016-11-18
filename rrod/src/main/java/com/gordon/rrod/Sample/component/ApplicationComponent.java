package com.gordon.rrod.Sample.component;

import android.content.Context;

import com.gordon.rrod.Sample.base.BaseActivity;
import com.gordon.rrod.Sample.module.ApplicationModule;
import com.gordon.rrod.Sample.repository.LoginRepository;

import dagger.Component;

/**
 * dagger的module生成工厂,生产的对象跟application有共同的生命周期
 * Created by gordon on 2016/5/29.
 */
// 如果添加@Singleton标明该Component中有Module使用了@Singleton
//@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(BaseActivity activity);
    //void inject(BaseFragment fragment);

    Context context();

//    LoginRepository loginRepository();


}
