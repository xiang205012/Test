package com.xiang.viewanim.view.QQ.dragLayout;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * 仿QQ侧滑面板 步骤：A B C D E
 * Created by Administrator on 2016/2/16.
 */
public class DragLayout extends FrameLayout {

    private ViewDragHelper dragHelper;
    private ViewGroup mLeftContent;// 拉开时左边面板
    private ViewGroup mMainContent;// 主面板

    private int width;// 控件的宽度
    private int height;// 控件的高度
    private int mRange;// 滑动的范围

    private Status currentStatus = Status.CLOSE;//面板当前的状态

    public enum Status{
        CLOSE,OPEN,DRAGING
    }

    private OnDragStatusChangeListener listener;
    public void setDragStatusListener(OnDragStatusChangeListener listener){
        this.listener = listener;
    }
    /**面板状态监听器*/
    public interface OnDragStatusChangeListener{
        void onClose();
        void onOpen();
        void onDraging(float percent);// 拖拽过程中
    }

    public void setStatus(Status mStatus){
        this.currentStatus = mStatus;
    }

    public Status getStatus(){
        return currentStatus;
    }

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //ViewDragHelper.create(ViewGroup parent, Callback cb);
        //ViewDragHelper.create(ViewGroup forParent, float sensitivity, Callback cb);
        // parent :是滑动子view的父View,sensitivity:表示敏感度(跟touchSlop有关，越小越敏感，越容易滑动) cb:滑动时的回调
// A.
        dragHelper = ViewDragHelper.create(this, setCallback());
    }

    /**滑动时回调*/
    @NonNull
    private ViewDragHelper.Callback setCallback() {
        return new ViewDragHelper.Callback() {
// E.
            // 1. 根据返回结果决定当前child是否可以拖拽 true表示可以，false表示不可以
            // child 当前被拖拽的View
            // pointerId 区分多点触摸的id
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //return false;不能拖拽
                // 如果只想主面板可以拖拽
                //return mMainContent == child;
                // 如果全部可以拖拽
                return true;
            }

            /**当capturedChild被捕获是调用*/
            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                // 当capturedChild被捕获是调用
                super.onViewCaptured(capturedChild, activePointerId);
            }

            /**返回拖拽的范围, 不对拖拽进行真正的限制. 仅仅决定了动画执行速度*/
            @Override
            public int getViewHorizontalDragRange(View child) {
                return mRange;// 是在onSizeChanged()中获取
            }

            /* 2.根据建议值修正将要移动的(横向)位置
            * 此时还有发生真的的移动
            */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // child : 当前拖拽的view
                // left : 新的位置的建议值，
                // dx ：位置变化量
                // left 和 oldleft的区别
                Log.i("TAG","oldLeft:"+child.getLeft() + "  变化量dx:"+ dx +"  新的left："+left );
                // left = oldleft + dx;

                // 只对主面板做限制,在第三步中对左面板位置控制
                if(child == mMainContent){
                    left = fixLeft(left);
                }

                return left;
            }
            // 2.根据建议值修正将要移动的(竖直方向)位置
            //@Override
            //public int clampViewPositionVertical(View child, int top, int dy) {
            //    return super.clampViewPositionVertical(child, top, dy);
            //}

            /*3. 当View位置改变的时候, 处理要做的事情 (更新状态, 伴随动画, 重绘界面)
            * 此时,View已经发生了位置的改变
            */
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                //如果拖拽的是左面板 ,左面板不动，并把拖拽的变化量交给主面板，
                // 此时主面板就可以实现无论拖拽左面板还是主面板都可以移动
                int newLeft = left;
                if(changedView == mLeftContent){
                    // 当左面板移动后，再强制放回去(看上去就是不动的)
                    mLeftContent.layout(0,0,width,height);
                    // 将拖拽后新的建议值修正后交给主面板
                    newLeft = fixLeft(mMainContent.getLeft() + dx);
                    // 主面板根据新的建议值移动位置
                    mMainContent.layout(newLeft,0,newLeft + width,height);
                }

         // 6.
                // 执行伴随动画
                dispatchDragEventAnimation(newLeft);

                // 为了兼容低版本，每次修改值后都要重绘
                invalidate();
            }
            /**4. 当View被释放的时候(即松手时), 处理的事情(执行动画)*/
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                // releasedChild 被释放的子view(当前拖拽的view)
                // xvel 水平方向的瞬时速度 拖拽松手时的速度越快数值越大 往右为正值，往左为负值
                // yvel 竖直方向的瞬时速度 拖拽松手时的速度越快数值越大 往右为正值，往左为负值
                Log.d("TAG->","releasedChild:"+releasedChild+"  xvel:"+xvel+"  yvel:"+yvel);
                super.onViewReleased(releasedChild, xvel, yvel);
                // 松手后判断 是打开还是关闭
                if(xvel == 0 && mMainContent.getLeft() > mRange / 2.0f){
                    open();
                }else if(xvel > 0){
                    open();
                }else{
                    close();
                }

            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
            }
        };
    }

    private void dispatchDragEventAnimation(int newLeft) {
        // 根据新的位置得到移动的百分比
        float percent = newLeft * 1.0f / mRange;
        // percent : 0.0f(关闭时) --> 1.0f(打开时)

        if(listener != null){
            listener.onDraging(percent);
        }

        // 更新状态，执行回调
        Status preStatus = currentStatus;
        currentStatus = updateStatus(percent);
        if(currentStatus != preStatus){
            // 只有当前状态和之前的状态不一样时才执行(说明状态发生了改变)
            if(currentStatus == Status.CLOSE){
                if(listener != null) listener.onClose();
            }else if(currentStatus == Status.OPEN){
                if(listener != null) listener.onOpen();
            }
        }

        // 执行动画
        valueAnim(percent);

    }

    private Status updateStatus(float percent) {
        if(percent == 0f){
            return Status.CLOSE;
        }else if(percent == 1.0f){
            return Status.OPEN;
        }
        return Status.DRAGING;
    }

    private void valueAnim(float percent) {
      // 1.左面板 缩放动画，平移动画，透明度动画
//        mLeftContent.setScaleX(0.5f + percent * 0.5f);// 缩放范围：0.5 - 1
//        mLeftContent.setScaleY(0.5f + percent * 0.5f);
//        mLeftContent.setTranslationX(0.5f + percent * 0.5f);
        ViewHelper.setScaleX(mLeftContent,evaluate(percent,0.5f,1.0f));
        ViewHelper.setScaleY(mLeftContent,evaluate(percent,0.5f,1.0f));
        ViewHelper.setTranslationX(mLeftContent, evaluate(percent, -width / 2.0f, 0));//平移时让它从负的一半移到0
        ViewHelper.setAlpha(mLeftContent,evaluate(percent,0.5f,1.0f));
      //  2.主面板 缩放动画 1.0f -- 0.8f
        ViewHelper.setScaleX(mMainContent,evaluate(percent,1.0f,0.8f));
        ViewHelper.setScaleY(mMainContent, evaluate(percent, 1.0f, 0.8f));
      //  3.背景颜色变化
        getBackground().setColorFilter((Integer)evaluateColor(percent, Color.BLACK,Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);

    }

    // 0~100 如果是从 0 开始 50%就是50
    // |------|-----------|-------------|
    // 0      10          50%           100
    // 50%所在位置的值：perRe = 0 + 50% * (100 - 0);

    // 10~110 如果是从10 开始 50%就是60
    // |-------------|---------------|
    // 10           50%             110
    // 50%所在位置的值：perRe = 10 + 50% * (110 - 10);
    private Float evaluate(float percent,Number start,Number end){
        return start.floatValue() + percent * (end.floatValue() - start.floatValue());
    }
    // 这两个方法 参考 TypeEvaluator的实现类(eclipse中进入此类，ctrl+t就能看到所有的实现类)
    /**颜色渐变*/
    private Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        //TypeEvaluator
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }

    private boolean isSmooth = true;//是否要平滑的执行动画
    public void setSmooth(boolean isTrue){
        isSmooth = isTrue;
    }

// 5.2
    /**此方法属于高频率调用*/
    @Override
    public void computeScroll() {
        super.computeScroll();
        // 持续平滑动画
        if(dragHelper.continueSettling(true)) {
            // continueSettling()返回true表示动画还需要继续执行
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**主面板打开*/
    public void open() {
        if(isSmooth){
 // 5.1
            if(dragHelper.smoothSlideViewTo(mMainContent,mRange,0)){
                // smoothSlideViewTo返回true表示还没有移动到指定的位置，需要刷新界面
                ViewCompat.postInvalidateOnAnimation(this);// 必须是子view的父view才能引起重绘，重绘后就能确定子view的位置
            }
        }else {
            mMainContent.layout(mRange, 0, mRange + width, height);
        }
    }
    /**主面板关闭*/
    public void close() {
        if(isSmooth) {
            if (dragHelper.smoothSlideViewTo(mMainContent, 0, 0)) {
                // smoothSlideViewTo返回true表示还没有移动到指定的位置，需要刷新界面
                ViewCompat.postInvalidateOnAnimation(this);// 必须是子view的父view才能引起重绘，重绘后就能确定子view的位置
            }
        }else {
            mMainContent.layout(0, 0, width, height);
        }
    }

    /**对滑动范围做真正的限制*/
    private int fixLeft(int left){
        // 对滑动范围做真正的限制
        if(left < 0){
            left = 0;
        }else if(left > mRange){
            left = mRange;
        }
        return left;
    }

// B.
    /**事件传递处理*/
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 通过dragHelper将事件拦截，并在onTouchEvent中处理
        return dragHelper.shouldInterceptTouchEvent(ev);
    }
// C.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {// 多次触控时可能有异常，所以最好try一下
            // 将触摸事件交给dragHelper来处理，也就是ViewDragHelper.Callback()
            dragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回true 持续接收事件
        return true;
    }

// D.
    /**当布局文件xml填充完毕后，所有的子view都被添加进来后调用*/
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 获取到两个子view
        if(getChildCount() < 2){
            throw new IllegalStateException("布局至少有俩孩子. Your ViewGroup must have 2 children at least.");
        }
        if(!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("子View必须是ViewGroup的子类. Your children must be an instance of ViewGroup");
        }

        mLeftContent = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 当尺寸有变化的时候调用 (是在onMeasure之后)
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        mRange = (int) (width * 0.6f);
    }
}
