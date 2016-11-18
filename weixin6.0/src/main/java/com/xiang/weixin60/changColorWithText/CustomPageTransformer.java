package com.xiang.weixin60.changColorWithText;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Administrator on 2016/1/22.
 */
public class CustomPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        Log.i("TAG-->.","view :"+ page + "  position : " + position);
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
        if(position >= -1 && position <= 0){
            ViewHelper.setAlpha(page, 1.0f + position);
            float scaleFactor = 1 + 0.5f * position;
            ViewHelper.setScaleX(page,scaleFactor);
            ViewHelper.setScaleY(page,scaleFactor);
        }
        if (position > 0 && position <= 1) {
            ViewHelper.setTranslationX(page, page.getWidth() * (-position));
            ViewHelper.setAlpha(page, 1.0f - position);
            float scaleFactor = 1 - 0.5f * position;
            ViewHelper.setScaleX(page,scaleFactor);
            ViewHelper.setScaleY(page,scaleFactor);
        }
        	//旋转
//		if(position >= -1 && position <= 0){
//			ViewHelper.setAlpha(v, 1.0f + position);
//			ViewHelper.setTranslationX(v, v.getWidth() * (-position));
//			ViewHelper.setPivotX(v, v.getWidth()/2);
//			ViewHelper.setPivotY(v, v.getHeight()/2);
//			ViewHelper.setRotationY(v,  position * 90f);
//		}
//		if (position > 0 && position <= 1) {
//			ViewHelper.setAlpha(v, 1.0f - position);
//			ViewHelper.setTranslationX(v, v.getWidth() * (-position));
//			ViewHelper.setPivotX(v, v.getWidth()/2);
//			ViewHelper.setPivotY(v, v.getHeight()/2);
//			ViewHelper.setRotationY(v, position * 90f);
//	}
// 折叠
		// if(position >= -1 && position <= 0){
		// ViewHelper.setPivotX(v, v.getWidth());
		// ViewHelper.setPivotY(v, 0);
		// ViewHelper.setScaleX(v, 1 + position);
		// }
		// if(position > 0 && position <= 1){
		// ViewHelper.setPivotX(v, 0);
		// ViewHelper.setPivotY(v, 0);
		// ViewHelper.setScaleX(v, 1 - position);
		// }

		// 立方体
		// if (position >= -1 && position <= 0) {
		// ViewHelper.setPivotX(v, v.getMeasuredWidth());
		// ViewHelper.setPivotY(v, v.getMeasuredHeight() * 0.5f);
		// ViewHelper.setRotationY(v, 90f * position);
		// ViewHelper.setAlpha(v, 1 + position);
		// }
		// if (position > 0 && position <= 1) {
		// ViewHelper.setPivotX(v, 0);
		// ViewHelper.setPivotY(v, v.getMeasuredHeight() * 0.5f);
		// ViewHelper.setRotationY(v, 90f * position);
		// ViewHelper.setAlpha(v, 1 - position);
		// }

        /**
         *
         * position参数指明给定页面相对于屏幕中心的位置。
         * 它是一个动态属性，会随着页面的滚动而改变。
         * 当一个页面填充整个屏幕是，它的值是0，
         * 当一个页面刚刚离开屏幕的右边时，它的值是1。
         * 当两个也页面分别滚动到一半时，其中一个页面的位置是-0.5，另一个页面的位置是0.5。基于屏幕上页面的位置
         * ，通过使用诸如setAlpha()、setTranslationX()、或setScaleY()方法来设置页面的属性，来创建自定义的滑动动画。
        @Override
        public void transformPage(View view, float position) {
            if (position <= 0) {
                //从右向左滑动为当前View

                //设置旋转中心点；
                ViewHelper.setPivotX(view, view.getMeasuredWidth());
                ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);

                //只在Y轴做旋转操作
                ViewHelper.setRotationY(view, 90f * position);
            } else if (position <= 1) {
                //从左向右滑动为当前View
                ViewHelper.setPivotX(view, 0);
                ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
                ViewHelper.setRotationY(view, 90f * position);
            }
        }
        */
    }
}
