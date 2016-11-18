package com.xiang.viewanim.view.QQ.dragLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 当DragLayout打开时，主面板的listview不能滑动，当滑动up时自动关闭DragLayout
 * Created by Administrator on 2016/2/17.
 */
public class MyLinearLayout extends LinearLayout {

    private DragLayout dragLayout;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDragLayout(DragLayout dragLayout){
        this.dragLayout = dragLayout;
    }

    /**
     * 将listview的滑动事件拦截
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(dragLayout.getStatus() == DragLayout.Status.CLOSE){
            //如果是关闭状态，交由系统处理
            return super.onInterceptTouchEvent(ev);
        }else{
            //如果是打开状态，返回true表示拦截事件(并在onTouchEvent()方法中自行处理)
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(dragLayout.getStatus() == DragLayout.Status.CLOSE){
            //如果是关闭状态，交由系统处理
            return super.onTouchEvent(event);
        }else{
            //如果是打开状态，当up时关闭draglayout
            if(event.getAction() == MotionEvent.ACTION_UP) {
                dragLayout.close();
            }
            return true;
        }
    }
}
