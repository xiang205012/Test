package com.test.mytest.testAndroidMVP.test2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.mytest.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/19.
 */
public class AndroidMVPActivity2 extends Activity implements ILoadView{

    @InjectView(R.id.tv_mvp1)
    TextView tv_mvp1;
    @InjectView(R.id.tv_mvp2)
    TextView tv_mvp2;
    @InjectView(R.id.tv_mvp3)
    TextView tv_mvp3;
    @InjectView(R.id.tv_mvp4)
    TextView tv_mvp4;
    @InjectView(R.id.tv_mvp5)
    TextView tv_mvp5;
    @InjectView(R.id.tv_mvp6)
    TextView tv_mvp6;
    @InjectView(R.id.tv_mvp7)
    TextView tv_mvp7;
    private ProgressBar progressBar;
    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        progressBar = new ProgressBar(this);
        presenter = new Presenter(this);
        //获取数据，成功就会执行setModel(),失败就会执行showError()
        presenter.getWeater();
    }

    @Override
    public void showDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDialog() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        Toast.makeText(this,"加载失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setModel(WeaterModel weaterModel) {
        tv_mvp1.setText(weaterModel.getSD());
        tv_mvp2.setText(weaterModel.getSD());
        tv_mvp3.setText(weaterModel.getSD());
        tv_mvp4.setText(weaterModel.getSD());
        tv_mvp5.setText(weaterModel.getSD());
        tv_mvp6.setText(weaterModel.getSD());
        tv_mvp7.setText(weaterModel.getSD());
    }
}
