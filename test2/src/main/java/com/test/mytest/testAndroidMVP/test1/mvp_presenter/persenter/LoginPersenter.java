package com.test.mytest.testAndroidMVP.test1.mvp_presenter.persenter;


import com.test.mytest.testAndroidMVP.test1.mvp_model.Imodel.IUser;
import com.test.mytest.testAndroidMVP.test1.mvp_model.model.UserModel;
import com.test.mytest.testAndroidMVP.test1.mvp_presenter.Ipersenter.ILoginPersenter;
import com.test.mytest.testAndroidMVP.test1.mvp_viewStatus.ILoginView;

/**
 * 业务逻辑处理类
 * Created by Administrator on 2016/1/19.
 */
public class LoginPersenter implements ILoginPersenter {

    ILoginView iLoginView;
    IUser iUser;

    public LoginPersenter(ILoginView iLoginView){
        this.iLoginView = iLoginView;
        initUser();
    }

    private void initUser() {
        iUser = new UserModel("mvp","mvp");
    }

    @Override
    public void clear() {
        iLoginView.onClearText();
    }

    @Override
    public void doLogin(String name, String password) {
        boolean result = true;//测试而已
        int code = iUser.checkUserValidity(name,password);
        iLoginView.onLoginResult(result,code);
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        iLoginView.onSetProgressBarVisibility(visibility);
    }

    @Override
    public void onDestroy() {
        iLoginView = null;
    }
}
