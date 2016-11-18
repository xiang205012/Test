package com.test.mytest.testCustomView.zhanghongyang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.test.mytest.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/18.
 */
public class ZhangCustomViewMainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhang_customview);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_scale_image})
    public void Click(View view){
        Intent intent = null;
        switch(view.getId()){
            case R.id.btn_scale_image:

                break;
        }
    }


}
