package com.test.mytest.testCustomView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.test.mytest.R;
import com.test.mytest.testCustomView.canvas.CanvasActivity;
import com.test.mytest.testCustomView.circleProgress.CircleProgressActivity;
import com.test.mytest.testCustomView.zhanghongyang.ZhangCustomViewMainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/9.
 */
public class CustomViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.btn_cicle_progressbar,R.id.btn_canvas})
    public void click(View view){
        Intent intent = null;
        switch(view.getId()) {
            case R.id.btn_cicle_progressbar://自定义圆形进度条
                intent = new Intent(this, CircleProgressActivity.class);
                break;
            case R.id.btn_canvas://自定义圆形进度条
                intent = new Intent(this, CanvasActivity.class);
                break;
            case R.id.btn_zhanghongyang://张鸿洋自定view系列
                intent = new Intent(this, ZhangCustomViewMainActivity.class);
                break;
        }
        startActivity(intent);
    }

}
