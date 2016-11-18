package cj.library.base;

import android.app.Application;

/**
 * Created by cj on 2015/11/24.
 */
public class AppBaseApplication extends Application {

    public static AppBaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


    }


    /**
     * 获取BaseApplication单例
     * @return
     */
    public static AppBaseApplication getInstance(){
        return instance;
    }

}
