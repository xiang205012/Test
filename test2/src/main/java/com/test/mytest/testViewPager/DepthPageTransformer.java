package com.test.mytest.testViewPager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 如果不使用nineoldandroids-2.4.0.jar，只能运行在3.0以上的机器上才有效
 * viewpager切换动画效果，从小打打，从透明到不透明
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0] A页position的变化规律是 (0.0 ~ -1) float类型
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);//属性动画
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);

        } else if (position <= 1) { // (0,1] B页position的变化规律是 (1 ~ 0.0) float类型
            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}