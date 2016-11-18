package com.test.mytest.testCalendarView;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by gordon on 2016/11/1.
 */

public class AlphaAndScaleTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {

        if(position < -1){
            page.setAlpha(0);
        } else if(position <= 0){
            page.setAlpha(1.0f + position);
            float scaleFactor = 1 + 0.5f * position;
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        }else if(position <= 1){
            page.setTranslationX(page.getWidth() * (-position));
            page.setAlpha(1.0f - position);
            float scaleFactor = 1 - 0.5f * position;
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        }else {
            // {{ 避免一进入时就一直往右滑，滑到大于最后一张时出现白屏，但是在view上稍微再滑一下又出来了
            //    为解决此问题，在这里让view再滑一点点
            page.setTranslationX(-1f);
            page.setTranslationX(1f);
            // }}
            page.setAlpha(0);
        }

    }
}
