package com.xiang.viewanim.view.hongyang.simple.textview;

import android.app.Activity;
import android.os.Bundle;

import com.xiang.viewanim.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/1.
 */
public class MyTextViewActivity extends Activity {

    @InjectView(R.id.mytextview)
    MyTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytextview);
        ButterKnife.inject(this);
    }

}
