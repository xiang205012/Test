package com.test.mytest.uitls;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


/**
 * 需要使用nineoldandroids-2.4.0.jar,此jar包兼容api8
 *
 * Created by Administrator on 2015/11/11.
 */
public class AnimationUtils {

    /**
     * Animation.ABSOLUTE：为绝对位置
     * Animation.RELATIVE_TO_PARENT：动画相对于父控件
     * Animation.RELATIVE_TO_SELF:动画相对于自身
     *###如果不设置type,则默认是Animation.ABSOLUTE，
     *###所以一般设置为Animation.RELATIVE_TO_SELF避免动画执行时位置有所差异
     *
     * Type分为Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF or Animation.RELATIVE_TO_PARENT
     说明Value如何解释。
     当为Animation.ABSOLUTE，为绝对位置。如10，既为相对于当前位置增量为10的坐标点
     当为Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT为百分比(0 ~ 1.0)。
     如：
     fromXType=Animation.RELATIVE_TO_SELF
     fromXValue=0
     意思为按自身的宽高来算Value，x=宽*value;y=高*value
     fromXValue=0相当于Animation.ABSOLUTE下的0
     fromXValue=0.5相当于Animation.ABSOLUTE下的宽*value为值
     fromXValue=1.0相当于Animation.ABSOLUTE下的View宽度为值
     */

    private static AnimationUtils animationUtils = null;
    /**
     * 设置动画相对自身的模式
     */
    public static int type = Animation.RELATIVE_TO_SELF;
    /**
     * view Animation动画监听器
     */
    private MyAnimationListener listener;
    /**
     * property Animation的监听器
     */
    private MyAnimatorListener torListener;
    /**
     * 判断动画是否打开
     */
    private boolean flag = false;

    public static synchronized AnimationUtils getAnimationUtils(){
        if(animationUtils == null){
            synchronized (AnimationUtils.class){
                if(animationUtils == null){
                    animationUtils = new AnimationUtils();
                }
            }
        }
        return animationUtils;
    }

    /**
     * view Animation的监听器
     */
    public interface MyAnimationListener{
        void animationStart(Animation animation, boolean flag);
        void animationEnd(Animation animation, boolean flag);
        void animationRepeat(Animation animation, boolean flag);
    }
    /**
     * view Animation的监听器
     */
    public void setMyAnimationListener(MyAnimationListener listener){
        this.listener = listener;
    }

    /**
     * property Animation的监听器
     */
    public interface MyAnimatorListener{
        void animationStart(Animator animator, boolean flag);
        void animationEnd(Animator animator, boolean flag);
        void animationCancel(Animator animator, boolean flag);
        void animationRepeat(Animator animator, boolean flag);
    }
    /**
     * property Animation的监听器
     * @param torListener
     */
    public void setMyAnimatorListener(MyAnimatorListener torListener){
        this.torListener = torListener;
    }


    /**
     * 为view设置平移动画
     * @param view         需要设置动画的view对象
     * @param fromXDelta   x方向开始位置
     * @param toXDelta     x方向结束位置
     * @param fromYDelta   y方向开始位置
     * @param toYDelta     y方向结束位置
     *  前四个参数分别设置X轴的开始位置，设置X轴的结束位置
        后四个参数分别设置Y轴的开始位置，设置Y轴的结束位置
        TranslateAnimation translateAnimation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1.0f);
    */
    public void setTranslateAnimation(View view,float fromXDelta,float toXDelta,float fromYDelta,float toYDelta,long time,boolean flag){
        Animation animation = new TranslateAnimation(type,fromXDelta,type,toXDelta,type,fromYDelta,type,toYDelta);
        startAnimation(view, animation, time, flag);
    }

    /**
     * 执行动画
     * @param view
     * @param animation
     * @param time       动画执行的时间
     */
    private void startAnimation(View view,Animation animation,long time, final boolean flags) {
        view.clearAnimation();
        animation.setDuration(time);
        animation.setInterpolator(new LinearInterpolator());//匀速
        animation.setFillAfter(true);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (listener != null) {
                    listener.animationStart(animation, flags);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null) {
                    listener.animationEnd(animation, flags);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (listener != null) {
                    listener.animationRepeat(animation, flags);
                }
            }
        });
    }

    /**
     * 为view设置旋转动画
     * @param view
     * @param fromDegrees 起始角度
     * @param toDegress   结束角度
     * @param pivotX      中心点x方向坐标
     * @param pivotY      中心点y方向坐标
     * Animation roateAnimation = new RotateAnimation(0f, 180f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
     */
    public void setRotateAnimation(View view,float fromDegrees,float toDegress,float pivotX,float pivotY,long time,boolean flag){
        Animation animation = new RotateAnimation(fromDegrees,toDegress,type,pivotX,type,pivotY);
        startAnimation(view, animation, time, flag);
    }

    /**
     * 为view设置透明度动画
     * @param view
     * @param fromAlpha 开始透明度
     * @param toAlpha   结束透明度
     * @param time      执行时间
     */
    public void setAlphaAnimation(View view,float fromAlpha,float toAlpha,long time,boolean flag){
        Animation animation = new AlphaAnimation(fromAlpha,toAlpha);
        startAnimation(view,animation,time,flag);
    }

    /**
     * 为view设置缩放动画
     * @param view
     * @param fromX        x方向起始大小
     * @param toX          x方向结束大小
     * @param fromY        y方向起始大小
     * @param toY          y方向结束大小
     * @param pivotXValue  x方向中心点坐标
     * @param pivotYValue  y方向中心点坐标
     * @param time
     * 前四个参数表示从原来大小的100%缩小到10%，后四个参数是为确定“中心点”
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.1f, 1,
                            0.1f, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
     */
    public void setScaleAnimation(View view,float fromX,float toX,float fromY,float toY,float pivotXValue,float pivotYValue,long time,boolean flag){
        Animation animation = new ScaleAnimation(fromX,toX,fromY,toY,type,pivotXValue,type,pivotYValue);
        startAnimation(view, animation, time, flag);
    }

    /**
     * 先以上下展开为例
     * @param view
     * @param isOpen 默认是否是展开的
     */
    public void setPropertyTranstAnimation(final View view,long time, final boolean isOpen){
        int startHeight;//记录开始高度
        int endHeight;//记录结束高度
        //初始化该view的layoutParams
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(isOpen){//如果一开始是展开的
            layoutParams.height = getMeasureHeight(view);
            view.setLayoutParams(layoutParams);
            startHeight = layoutParams.height;
            endHeight = 0;
        }else {
            layoutParams.height = 0;
            view.setLayoutParams(layoutParams);
            startHeight = 0;
            endHeight = getMeasureHeight(view);
        }

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取动画运行到当前点的高度值，并设置给view的layoutParams.height
                int value = (int) animation.getAnimatedValue();
                layoutParams.height = value;
                //重新设置layoutParams从而刷新视图
                view.setLayoutParams(layoutParams);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(torListener != null) torListener.animationStart(animation, isOpen);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(torListener != null) torListener.animationEnd(animation, isOpen);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if(torListener != null) torListener.animationCancel(animation, isOpen);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if(torListener != null) torListener.animationRepeat(animation, isOpen);
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(time);
        animator.start();
    }

    /**
     * 获取控件的实际高度
     * @return
     */
    private int getMeasureHeight(View view) {
        //由于是上下展开，所以宽度是不变的
        int width = view.getMeasuredWidth();
        //高度先设置成包裹内容
        view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        //制定测量规则
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);//精确值
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.AT_MOST);//以实际测量为准
        //根据上面的宽度和高度测量规则进行实际测量
        view.measure(widthMeasureSpec,heightMeasureSpec);
        //返回实际测量的高度值
        return view.getMeasuredHeight();
    }


}
