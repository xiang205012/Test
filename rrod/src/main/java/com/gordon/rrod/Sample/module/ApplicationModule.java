package com.gordon.rrod.Sample.module;

import android.content.Context;

import com.gordon.rrod.MyApplication;
import com.gordon.rrod.Sample.repository.LoginDataRepository;
import com.gordon.rrod.Sample.repository.LoginRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 对象生成工厂,
 * 这里生成的对象会与application 有共同的生命周期,一般用于全局的单例类
 * Created by gordon on 2016/5/29.
 */
@Module
public class ApplicationModule {

    private final MyApplication application;
    // 在MyApplication类中初始化
    public ApplicationModule(MyApplication application) {
        this.application = application;
    }

    /**全局Context*/
    @Provides
    @Singleton
    Context applicationContext(){
        return this.application;
    }

    /**
     * LoginRepository是一个接口
     * @param loginDataRepository 接口的实现类
     * @return 返回接口的实现类，
     *              在其他地方使用时可以直接使用LoginRepository.xxx()，
     *              因为这里返回的就是LoginRepository
     *              但是对应到loginDataRepository.xxx()的方法在执行
     *  这里没有提供由@Provides修饰的方法返回LoginDataRepository，
     *  此时会对应到LoginDataRepository类中带@Inject修饰的构造方法
     */
//    @Provides
//    @Singleton
//    LoginRepository loginRepository(LoginDataRepository loginDataRepository){
//        return loginDataRepository;
//    }


}
