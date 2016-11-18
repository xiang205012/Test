package com.test.mytest.testAndroidMVP.test1.mvpActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.test.mytest.R;
import com.test.mytest.testAndroidMVP.test1.mvp_presenter.persenter.LoginPersenter;
import com.test.mytest.testAndroidMVP.test1.mvp_viewStatus.ILoginView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * android mvp架构测试
 * Created by Administrator on 2016/1/19.
 */
public class AndroidMVPActivity extends Activity implements ILoginView {

    @InjectView(R.id.mvp_login)
    Button mvp_login;
    @InjectView(R.id.mvp_clear)
    Button mvp_clear;
    @InjectView(R.id.mvp_progressBar)
    ProgressBar mvp_progressBar;
    @InjectView(R.id.mvp_username)
    EditText mvp_username;
    @InjectView(R.id.mvp_password)
    EditText mvp_password;

    private LoginPersenter loginPersenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);

        loginPersenter = new LoginPersenter(this);

    }

    @OnClick({R.id.mvp_login,R.id.mvp_clear})
    public void Click(View view){
        switch(view.getId()){
            case R.id.mvp_clear:
                loginPersenter.clear();
                break;
            case R.id.mvp_login:
                loginPersenter.setProgressBarVisibility(View.VISIBLE);
                loginPersenter.doLogin(mvp_username.getText().toString(),mvp_password.getText().toString());
                break;
        }
    }

    @Override
    public void onClearText() {
        mvp_username.setText("");
        mvp_password.setText("");
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        loginPersenter.setProgressBarVisibility(View.INVISIBLE);
        if(result){
            //.....
        }else{
            //....
        }
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        mvp_progressBar.setVisibility(visibility);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPersenter.onDestroy();
    }
}
