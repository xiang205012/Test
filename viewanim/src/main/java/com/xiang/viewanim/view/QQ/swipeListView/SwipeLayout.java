package com.xiang.viewanim.view.QQ.swipeListView;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.xiang.viewanim.view.QQ.dragLayout.DragLayout;

/**
 * 测拉删除
 * Created by Administrator on 2016/2/25.
 */
public class SwipeLayout extends FrameLayout {

    private ViewDragHelper dragHelper;
    /**显示在前面的view*/
    private View frontView;
    /**显示在后面的view*/
    private View backView;
    /** 前view的宽度 */
    private int width;
    /** 前view的高度 */
    private int height;
    /** 后view的宽度 */
    private int range;
    /**当前状态*/
    private SwipeStatus curStatus = SwipeStatus.Close;
    private OnSwipeLayoutListener mListener;

    public enum SwipeStatus{
        Close,Open,Draging
    }

    public interface OnSwipeLayoutListener{

        void onClose(SwipeLayout layout);
        void onOpen(SwipeLayout layout);
        void onDraging(SwipeLayout layout);
        // 以下两个回调方法是在Draging状态判断是要开还是要关，
        // 同时在ListView中拖拽的Item如果要开，而其他的Item如果已经是开的就要关闭
        void onStartClose(SwipeLayout layout);
        void onStartOpen(SwipeLayout layout);
    }


    public void setStatus(SwipeStatus status){
        this.curStatus = status;
    }

    public void setOnSwipeLayoutListener(OnSwipeLayoutListener listener){
        this.mListener = listener;
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化ViewDragHelper
        dragHelper = ViewDragHelper.create(this, 1.0f, mCallback);

    }

    ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            // 控制滑动范围
            if(child == frontView){
                if(left > 0){//前view不能往右滑
                    return 0;
                }else if(left < -range){//前view往左滑，最大距离为后view的宽度range
                    return -range;
                }
            }else if(child == backView){
                if(left > width){
                    return width;
                }else if(left < width - range){
                    return width - range;
                }
            }

            return left;
        }

        /**滑动的不是同一个childView时调用*/
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            // 前view和后view之间互相传递事件
            if(changedView == frontView){
                // 如果是滑动前view，后view跟着滑到dx
                backView.offsetLeftAndRight(dx);
            }else if(changedView == backView){
                // 如果是滑动后view，前view跟着滑到dx
                frontView.offsetLeftAndRight(dx);
            }
            // 监听SwipeLayout的开闭状态
            dispatchSwipeLayoutEvent();

            // 为兼容老版本，重绘视图
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // xvel:释放时，x方向滑动的速度
            if(xvel == 0 && frontView.getLeft() < -range/2){
                open();
            }else if(xvel < 0){
                open();
            }else{
                close();
            }
        }
    };

    private void dispatchSwipeLayoutEvent() {
        if(mListener != null) mListener.onDraging(this);

        // 记录上一次的开闭状态
        SwipeStatus pre = curStatus;
        // 滑动后的状态
        curStatus = updateStatus();
        if(pre != curStatus && mListener != null){
            // 当前状态跟上一次状态不一样时
            if(curStatus == SwipeStatus.Close){
                mListener.onClose(this);
            }else if(curStatus == SwipeStatus.Open){
                mListener.onOpen(this);
            }else if(curStatus == SwipeStatus.Draging){
                if(pre == SwipeStatus.Close){
                    // 拖拽状态下，如果上一次的状态是Close,那么就是将要打开
                    mListener.onStartOpen(this);
                }else if(pre == SwipeStatus.Open){
                    mListener.onStartClose(this);
                }
            }
        }

    }

    private SwipeStatus updateStatus() {
        int left = frontView.getLeft();
        if(left == 0){
            return SwipeStatus.Close;
        }else if(left == -range){
            return SwipeStatus.Open;
        }else {
            return SwipeStatus.Draging;
        }
    }

    private void close() {
       close(true);
    }

    private void open() {
        open(true);
    }

    public void open(boolean isSmooth){
        int left = -range;
        if(isSmooth){
            if(dragHelper.smoothSlideViewTo(frontView,left,0)){
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else{
            layoutContent(false);
        }
    }

    /**是否执行平滑动画*/
    public void close(boolean isSmooth){
        int left = 0;
        if(isSmooth){
            if(dragHelper.smoothSlideViewTo(frontView,left,0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else{
            layoutContent(false);
        }
    }

    /**持续平滑动画*/
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(dragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**拦截ViewGroup事件分发*/
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    /**处理事件*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            dragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("TAG","Action: "+event.getAction());
        return true;
    }

    /**摆放两个view的位置*/
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 摆放位置
        layoutContent(false);
    }

    private void layoutContent(boolean isOpen) {
        // isOpen标记后view是否是展开状态
        // 摆放前view
        Rect fronRect = computeFrontViewRect(isOpen);
        frontView.layout(fronRect.left, fronRect.top, fronRect.right, fronRect.bottom);
        // 摆放后view
        Rect backRect = computeBackViewRect(fronRect);
        backView.layout(backRect.left,backRect.top,backRect.right,backRect.bottom);
        // 由于是先摆放前view后摆放后view，所以后view是叠在前view上的，我们要放在后面
        bringChildToFront(frontView);
    }
    /**后view所在的矩形框*/
    private Rect computeBackViewRect(Rect fronRect) {
        // 后view的left是根据前view的right移动的
        int left = fronRect.right;
        return new Rect(left,0,left + range,height);
    }

    /**前view所在的矩形框*/
    private Rect computeFrontViewRect(boolean isOpen) {
        int left = 0;//默认是关闭状态
        if(isOpen){
            left = -range;//如果是展开状态，那前view的left就是负的后view宽度
        }
        return new Rect(left,0,left + width,0 + height);
    }

    /**当xml充填完毕后调用*/
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        backView = getChildAt(0);
        frontView = getChildAt(1);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = frontView.getMeasuredWidth();
        height = frontView.getMeasuredHeight();
        range = backView.getMeasuredWidth();

    }
}
