package com.test.mytest.testADViewPager;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 轮播效果的{@link ViewPager}
 * <ol>
 * <li>当attach到window上时，会自动触发轮播定时任务</li>
 * <li>当从window中detach时，会触发关闭轮播定时任务</li>
 * </ol>
 *
 * @author xpleemoon
 */
public class BannerViewPager extends ViewPager {
    @IntDef({RESUME, PAUSE, DESTROY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LifeCycle {
    }

    public static final int RESUME = 0;
    public static final int PAUSE = 1;
    public static final int DESTROY = 2;
    /**
     * 生命周期状态，保证{@link #mCarouselTimer}在各生命周期选择执行策略
     */
    private int mLifeCycle = RESUME;
    /**是否可以自动轮播*/
    private boolean isAutoScrollAble = true;
    /**轮播间隔时间*/
    private int intervalTime = 3000;
    /**
     * 是否正在触摸状态，用以防止触摸滑动和自动轮播冲突
     */
    private boolean mIsTouching = false;

    /**
     * 轮播定时器
     */
    private ScheduledExecutorService mCarouselTimer;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLifeCycle(@LifeCycle int lifeCycle) {
        this.mLifeCycle = lifeCycle;
    }

    public void setAutoScrollAble(boolean isAutoAble){
        this.isAutoScrollAble = isAutoAble;
    }

    public void setIntervalTime(int time){
        this.intervalTime = time;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIsTouching = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsTouching = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        shutdownTimer();
        mCarouselTimer = Executors.newSingleThreadScheduledExecutor();
        mCarouselTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                switch (mLifeCycle) {
                    case RESUME:
                        if (isAutoScrollAble && !mIsTouching
                                && getAdapter() != null
                                && getAdapter().getCount() > 1) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    setCurrentItem(getCurrentItem() + 1);
                                }
                            });
                        }
                        break;
                    case PAUSE:
                        break;
                    case DESTROY:
                        shutdownTimer();
                        break;
                }
            }
        }, intervalTime, intervalTime, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        shutdownTimer();
    }

    private void shutdownTimer() {
        if (mCarouselTimer != null && mCarouselTimer.isShutdown() == false) {
            mCarouselTimer.shutdown();
        }
        mCarouselTimer = null;
    }
}
