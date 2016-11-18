package com.test.mytest.testViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义viewpager平移缩放动画兼容3.0以下，还可以自定义其他动画
 * Created by CJ on 2016/1/11.
 */
public class ViewPagerAnim extends ViewPager {

    private static final float MIN_SCALE = 0.6f;
    private float mTrans;//滑动过程中偏移动画变化梯度 0 ~ 1
    private float mScale;//滑动过程中缩放动画变化梯度 0 ~ 1
    private View rightView;//右边的view
    private View leftView;//左边的view
    //存放viewpager左右的view
    private Map<Integer,View> mChild = new HashMap<Integer,View>();

    /**添加一个view*/
    public void addViews(int position,View view){
        mChild.put(position,view);
    }
    /**删除一个view*/
    public void removeViews(Integer position){
        mChild.remove(position);
    }

    public ViewPagerAnim(Context context) {
        super(context);
    }

    public ViewPagerAnim(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * viewpager滑动时调用
     * @param position  view在viewpager中的位置
     * @param offset    滑动时偏移量梯度
     *                  0 ~ 1之间的float类型值(第0页滑到第一页) 或 1 ~ 0之间的值(第一页滑到第0页)，更多页也是这个规律
     * @param offsetPixels
     *                  滑动时真实的偏移量
     */
    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        rightView = mChild.get(position);
        leftView = mChild.get(position + 1);
        animStack(rightView,leftView,offset,offsetPixels);

        super.onPageScrolled(position, offset, offsetPixels);
    }

    /**添加动画*/
    private void animStack(View right, View left, float offset, int offsetPixels) {
        if(right != null) {
            //mScale的取值范围：0 ~ 1
            mScale = (1 - MIN_SCALE) * offset + MIN_SCALE;
            //平移的量 viewpager宽度
            mTrans = -getWidth() - getPageMargin() + offsetPixels;
            //设置缩放动画
            ViewHelper.setScaleX(right,mScale);
            ViewHelper.setScaleY(right,mScale);
            //设置平移动画(只需要设置x轴，y轴是不变的)
            ViewHelper.setTranslationX(right,mTrans);
        }
        if(left != null){
            left.bringToFront();
            //bringToFront:Android中的ViewGroup是通过一个Array来保存其Children，
            // 当调用某个childView的bringToFront时，
            // 是将该childView放在其Parent的Array数组的最后，
            // ViewGroup的dispatchDraw在draw时是按照Array从前往后依次调用drawChild的，
            // 这样最后一个childView就在最前面了。
        }

    }
}
