package com.test.mytest.testViewPager;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;


/**
 * 让它0 ~ 20度之间倾斜着进入进出
 * Created by cj on 2016/1/10.
 */
public class RotateDownPageTransformer implements ViewPager.PageTransformer{

    /**最大值*/
    private static final float MAX_SCALE = 20f;//即旋转到20度时A页完全消失B页完全显示

    /**position的变化*/
    private float mRot;

    public void transformPage(View view, float position) {
        Log.i("TAG-->.", "view :" + view + "  position : " + position);
        /**
         * 从第一页滑动到第二页时position的变化：
         *      第一页的变化是：从 -0 递减到 -1
         *      第二页的变化是：从 1 递减到 0
         *
         *
         * 从第二页滑回到第一页时position的变化：
         *      第一页的变化是：从 -1 递减到 -0
         *      第二页的变化是：从 0 递增到 1
         *
         * 由此规律：无论左滑还是右滑 左边的变化永远是 -0 到 -1 。右边的变化永远是 0 到 1
         */
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            view.setAlpha(0);

        } else if (position <= 0) { //  A页position的变化规律是 (-0.0 --> -1) float类型
            //android:transformPivotX
            //关联方法: setPivotX(float)
            //属性说明: 水平方向偏转量
            mRot = position * MAX_SCALE;// 0 ~ -20之间
            //以y底部 x中心点作为旋转的中心点
            //view.setPivotX(pageWidth/2);
            //view.setPivotY(view.getMeasuredHeight());
            //view.setRotation(mRot);

            //为了兼容3.0以下
            ViewHelper.setPivotX(view,pageWidth/2);
            ViewHelper.setPivotY(view, view.getMeasuredHeight());
            ViewHelper.setRotation(view,mRot);

        } else if (position <= 1) { // (0,1] B页position的变化规律是 (1 --> 0.0) float类型
            //android:transformPivotX
            //关联方法: setPivotX(float)
            //属性说明: 水平方向偏转量
            mRot = position * MAX_SCALE;// 0 ~ -20之间
            //以y底部 x中心点作为旋转的中心点
            //view.setPivotX(pageWidth/2);
            //view.setPivotY(view.getMeasuredHeight());
            //view.setRotation(mRot);

            //为了兼容3.0以下
            ViewHelper.setPivotX(view,pageWidth/2);
            ViewHelper.setPivotY(view, view.getMeasuredHeight());
            ViewHelper.setRotation(view,mRot);
        } else { // (1,+Infinity]
            view.setAlpha(0);
        }
    }

}
