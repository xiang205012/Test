package com.test.mytest.testAndroidMVP.test1.mvp_presenter.Ipersenter;

/**
 * 连接IUser和ILoginView接口的纽带
 * Created by Administrator on 2016/1/19.
 */
public interface ILoginPersenter {

    void clear();
    void doLogin(String name, String password);
    void setProgressBarVisibility(int visibility);
    void onDestroy();

}
