package com.xiang.testapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.xiang.testapp.SwitchButton2;

/**
 * Created by gordon on 2016/10/11.
 */

public class SwitchButtonActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1,-1);
        RelativeLayout layout = new RelativeLayout(this);
        layout.setGravity(Gravity.CENTER);
        layout.setLayoutParams(params);

        RelativeLayout.LayoutParams sParams = new RelativeLayout.LayoutParams(-2,-2);
        SwitchButton2 switchButton2 = new SwitchButton2(this);
        sParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        switchButton2.setLayoutParams(sParams);
        layout.addView(switchButton2);

        setContentView(layout);
    }
}
