package com.test.mytest.MaterialDesign.cardview;

import android.os.Bundle;
import android.view.View;

import com.test.mytest.R;

import cj.library.base.AppBaseActivity;

/**
 * Created by Administrator on 2015/11/25.
 */
public class MyCardViewActivity extends AppBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);


    }

    @Override
    protected boolean toggleOverridePendingTrasition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTrasitionMode() {
        return null;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }
}
