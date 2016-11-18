package com.xiang.viewanim.view.QQ.parallaxListView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/2/6.
 */
public class MyScrollView extends ScrollView {

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected boolean overScrollBy(int deltaX,
                                   int deltaY,//竖直方向的瞬时偏移量/变化量，顶部到头下拉为负值，底部到头上拉为正值
                                   int scrollX,
                                   int scrollY,//竖直方向的偏移量/变化量
                                   int scrollRangeX,
                                   int scrollRangeY,//竖直方向滑动的范围
                                   int maxOverScrollX,
                                   int maxOverScrollY,//竖直方向最大的滑动范围
                                   boolean isTouchEvent// 是否为手指触摸滑动，true为手指，false为惯性
                                    ) {
        Log.i("TAG","deltaY:"+deltaY+"  scrollY:"+scrollY+"  scrollRangeY:"+scrollRangeY+"  maxOverScrollY:"+maxOverScrollY+"  isTouchEvent:"+isTouchEvent);


        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
}
