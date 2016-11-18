package com.xiang.viewanim.view.QQ.parallaxListView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.xiang.viewanim.util.ScreenUtil;

/**
 * 视差特效的listview
 * Created by Administrator on 2016/2/6.
 */
public class ParallaxListView extends ListView {

    private int ivHeight;
    private int origeHeight;
    private ImageView imageView;

    public ParallaxListView(Context context) {
        this(context, null);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 得到ImageView的引用
     * @param iv_header
     */
    public void getIvHeight(ImageView iv_header) {
        imageView = iv_header;

        ivHeight = iv_header.getHeight();//布局中写的高度
        int measureHeight = iv_header.getMeasuredHeight();// 测量的高度
        origeHeight = iv_header.getDrawable().getIntrinsicHeight();//图片原始高度

        Log.i("TAG","图片的高度---》》"+ "ivHeight :"+ivHeight+"  measureHeight:"+measureHeight+"  origeHeight:"+origeHeight);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY,
                                   int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY,
                                   boolean isTouchEvent) {
        // deltaY : 竖直方向的瞬时偏移量 / 变化量 dx   顶部到头下拉为-, 底部到头上拉为+
        // scrollY : 竖直方向的偏移量 / 变化量
        // scrollRangeY : 竖直方向滑动的范围
        // maxOverScrollY : 竖直方向最大滑动范围
        // isTouchEvent : 是否是手指触摸滑动, true为手指, false为惯性

        Log.d("TAG", "deltaY: " +deltaY + " scrollY: " + scrollY + " scrollRangeY: " + scrollRangeY
                + " maxOverScrollY: " + maxOverScrollY + " isTouchEvent: " + isTouchEvent);

        // 当向下滑动时
        if(deltaY < 0 && isTouchEvent){
            if(imageView.getHeight() < origeHeight) {
                // 放大效果,在滑动过程中由于requestLayout()方法重新设置了imageView的高度，所以不能用ivHeight来判断
                int dx = Math.abs(deltaY / 3);// 滑动的距离，滑动3像素让它移动1像素
                int newHeight = imageView.getHeight() + dx;// 滑动后图片新的高度
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = newHeight;
                imageView.requestLayout();// 重新布局，以达到放大效果
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    /**处理放大后回弹到布局的高度*/
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_UP:
                // 从当前高度imageView.getHeight() 执行动画到布局中的设置的高度ivHeight
                int startHeight = imageView.getHeight();
                int endHeight = ivHeight;

                // 方式一：属性动画
                //valueAnimator(startHeight,endHeight);

                // 方式二：自定义动画
                ResetAnimation animation = new ResetAnimation(imageView,startHeight,endHeight);
                startAnimation(animation);
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void valueAnimator(final int startHeith, final int endHeight) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1);
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                // fraction ：0.0 -- 1.0
                Log.d("TAG", "fraction: " + fraction);
                Integer newHeight = evaluate(fraction, startHeith, endHeight);
                imageView.getLayoutParams().height = newHeight;
                imageView.requestLayout();
            }
        });
        valueAnimator.start();
    }

    /**
     * 根据fraction来计算动画执行过程中的新高度
     * 类型估值器 是计算一定变化范围内，某一个时段的一个值，这个值在变化两端之间（包含首尾）
     */
    private Integer evaluate(float fraction, int startHeith, int endHeight) {
        return (int)(startHeith + fraction * (endHeight - startHeith));
    }
}
