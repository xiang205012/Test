package com.xiang.testapp.ViewPager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by gordon on 2016/10/21.
 */

public class ScaleAlphaTransformer implements ViewPager.PageTransformer {

    private float gradient = 0.2f;
    private float jiaodu = 30f;

    @Override
    public void transformPage(View view, float position) {

        if (position >= -1 && position <= 1) {
            view.setPivotX(position <= 0 ? view.getWidth() : 0);
            view.setRotationY(-90 * Math.abs(position));
        }

    }



}
