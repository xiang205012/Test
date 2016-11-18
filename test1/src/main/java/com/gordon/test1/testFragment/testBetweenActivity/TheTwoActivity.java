package com.gordon.test1.testFragment.testBetweenActivity;

import android.support.v4.app.Fragment;

import com.gordon.test1.testFragment.SampleFragmentActivity;


/**
 * Created by Administrator on 2016/5/12.
 */
public class TheTwoActivity extends SampleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        String content = getIntent().getStringExtra(TwoFragment.twoArgument);

        TwoFragment fragment = TwoFragment.newInstance(content);
        return fragment;
    }
}
