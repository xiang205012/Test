package com.xiang.viewanim.view.QQ.goo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/2/15.
 */
public class GooActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GooView(GooActivity.this));
    }
}
