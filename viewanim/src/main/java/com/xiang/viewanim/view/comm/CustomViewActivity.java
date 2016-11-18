package com.xiang.viewanim.view.comm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiang.viewanim.R;
import com.xiang.viewanim.view.comm.StepBarView.StepBarViewActivity;
import com.xiang.viewanim.view.comm.TreeView.TreeViewActivity;
import com.xiang.viewanim.view.comm.progressbar.CircleDotProgressActivity;
import com.xiang.viewanim.view.hongyang.HMainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gordon on 2016/6/5.
 */
public class CustomViewActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_stepbar_view,R.id.btn_treeView,R.id.btn_circleProgressView})
    public void click(View view){
        switch(view.getId()){
            case R.id.btn_stepbar_view:
                startActivity(new Intent(this, StepBarViewActivity.class));
                break;
            case R.id.btn_treeView:
                startActivity(new Intent(this, TreeViewActivity.class));
                break;
            case R.id.btn_circleProgressView:
                startActivity(new Intent(this, CircleDotProgressActivity.class));
                break;
        }
    }


}
