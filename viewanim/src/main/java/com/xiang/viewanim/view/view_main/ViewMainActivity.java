package com.xiang.viewanim.view.view_main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiang.viewanim.R;
import com.xiang.viewanim.view.QQ.QQMainActivity;
import com.xiang.viewanim.view.comm.CustomViewActivity;
import com.xiang.viewanim.view.hongyang.HMainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cj on 2016/2/one.
 */
public class ViewMainActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_view_hong,R.id.btn_view_qq,R.id.btn_view_custom})
    public void click(View view){
        switch(view.getId()){
            case R.id.btn_view_hong:
                startActivity(new Intent(this, HMainActivity.class));
                break;
            case R.id.btn_view_qq:
                startActivity(new Intent(this, QQMainActivity.class));
                break;
            case R.id.btn_view_custom:
                startActivity(new Intent(this, CustomViewActivity.class));
                break;
        }
    }

}
