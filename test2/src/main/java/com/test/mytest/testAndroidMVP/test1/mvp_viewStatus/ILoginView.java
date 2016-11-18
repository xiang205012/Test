package com.test.mytest.testAndroidMVP.test1.mvp_viewStatus;

/**
 * 此接口公布对应view在交互过程中显示状态方法
 * Created by Administrator on 2016/1/19.
 */
public interface ILoginView {

    void onClearText();
    void onLoginResult(Boolean result, int code);
    void onSetProgressBarVisibility(int visibility);

}
