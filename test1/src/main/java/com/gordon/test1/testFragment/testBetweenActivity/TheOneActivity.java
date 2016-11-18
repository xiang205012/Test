package com.gordon.test1.testFragment.testBetweenActivity;

import android.support.v4.app.Fragment;

import com.gordon.test1.testFragment.SampleFragmentActivity;


/**
 * Created by Administrator on 2016/5/12.
 */
public class TheOneActivity extends SampleFragmentActivity {

    private OneFragment oneFragment;

    @Override
    protected Fragment createFragment() {
        oneFragment = new OneFragment();
        return oneFragment;
    }
}
