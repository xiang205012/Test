package com.xiang.weixin60.satelliteMenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.xiang.weixin60.R;

/**
 * Created by Administrator on 2016/2/3.
 */
public class MySatelliteMenu extends ViewGroup implements View.OnClickListener {

    private static final int POS_LEFT_TOP = 0;
    private static final int POS_LEFT_BOTTOM = 1;
    private static final int POS_RIGHT_TOP = 2;
    private static final int POS_RIGHT_BOTTOM = 3;

    /**菜单的位置*/
    private enum Position{
        LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM;
    }

    /**菜单的状态（打开还是关闭）*/
    private enum State{
        OPEN,CLOSE;
    }

    /**当前位置*/
    private Position currentPosition = Position.LEFT_TOP;
    /**当前状态*/
    private State currentState = State.CLOSE;
    /**圆形菜单的半径*/
    private int radius;
    /**菜单主按钮*/
    private View mainBtn;

    private onMenuClickListener listener;
    /**菜单点击接口*/
    public interface onMenuClickListener{
        void menuClick(View view, int position);
    }

    public void setOnMenuClickListener(onMenuClickListener listener){
        this.listener = listener;
    }
    public MySatelliteMenu(Context context) {
        this(context,null);
    }

    public MySatelliteMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MySatelliteMenu);
        radius = (int) array.getDimension(R.styleable.MySatelliteMenu_satemenu_radius,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics()));
        int position = array.getInt(R.styleable.MySatelliteMenu_satemenu_position, POS_LEFT_TOP);
        switch(position){
            case POS_LEFT_TOP:
                currentPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:
                currentPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:
                currentPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:
                currentPosition = Position.RIGHT_BOTTOM;
                break;
        }
        array.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for(int i = 0 ;i < count ;i++){
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){//当布局发生变化时
            //定位主按钮
            layoutMainButton();
            //定位子菜单位置
            int count = getChildCount();
            for(int i = 0;i < count -1;i++){
                View childView = getChildAt(i+1);//因为第一个是主按钮
                childView.setVisibility(GONE);
                int width = childView.getMeasuredWidth();
                int height = childView.getMeasuredHeight();

                // 三角函数获取子view的位置 count - 2是有多少个这样的角度(本来是count-1，但是主按钮也在count内)
                // 把view当作一个点来看待就好理解了
                int childLeft = (int) (radius * Math.sin(Math.PI / 2 / (count -2)*i));
                int childTop = (int) (radius * Math.cos(Math.PI / 2 / (count -2)*i));

                if(currentPosition == Position.LEFT_BOTTOM || currentPosition == Position.RIGHT_BOTTOM){
                    childTop = getMeasuredHeight() - height - childTop;
                }
                if(currentPosition == Position.RIGHT_TOP || currentPosition == Position.RIGHT_BOTTOM){
                    childLeft = getMeasuredWidth() - width - childLeft;
                }
                int childRight = childLeft + width;
                int childBottom = childTop + height;
                childView.layout(childLeft,childTop,childRight,childBottom);

            }
        }

    }

    /**定位主按钮*/
    private void layoutMainButton() {
        mainBtn = getChildAt(0);
        mainBtn.setOnClickListener(this);
        int viewWidth = mainBtn.getMeasuredWidth();
        int viewHeight = mainBtn.getMeasuredHeight();
        int left = 0;
        int top = 0;

        switch(currentPosition){
            case LEFT_TOP:
                left = 0;
                top = 0;
                break;
            case LEFT_BOTTOM:
                left = 0;
                top = getMeasuredHeight() - viewHeight;
                break;
            case RIGHT_TOP:
                left = getMeasuredWidth() - viewWidth;
                top = 0;
                break;
            case RIGHT_BOTTOM:
                left = getMeasuredWidth() - viewWidth;
                top = getMeasuredHeight() - viewHeight;
                break;
        }
        int right = left + viewWidth;
        int bottom = top + viewHeight;
        mainBtn.layout(left, top, right, bottom);

    }


    @Override
    public void onClick(View v) {
        // 主按钮旋转动画
        mainBtnAnim(300);

        //子菜单旋转平移动画
        ItemAnim(300);
    }

    public void ItemAnim(int duration) {
        int count = getChildCount();
        for(int i = 0;i < count -1;i++){
            final View childView = getChildAt(i + 1);
            childView.setVisibility(VISIBLE);
            // 三角函数获取子view的位置 count - 2是有多少个这样的角度(本来是count-1，但是主按钮也在count内)
            int childLeft = (int) (radius * Math.sin(Math.PI / 2 / (count -2)*i));
            int childTop = (int) (radius * Math.cos(Math.PI / 2 / (count -2)*i));

            AnimationSet animSet = new AnimationSet(true);
            TranslateAnimation tAnim = null;

            /**标记mainBtn的位置不同是，x和y在平移动画上是不一样的，有加有减，默认为加*/
            int xFlag = 1;
            int yFlag = 1;

            if(currentPosition == Position.LEFT_TOP || currentPosition == Position.LEFT_BOTTOM){
                xFlag = -1;
            }
            if(currentPosition == Position.LEFT_TOP || currentPosition == Position.RIGHT_TOP){
                yFlag = -1;
            }
            if(currentState == State.CLOSE){//子菜单展开动画
                tAnim = new TranslateAnimation(xFlag * childLeft,0,yFlag * childTop,0);// 0 表示相对自身的中心点
                childView.setClickable(true);
                childView.setFocusable(true);
            }else{//子菜单关闭动画
                tAnim = new TranslateAnimation(0, xFlag * childLeft,0,yFlag * childTop);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            RotateAnimation rAnim = new RotateAnimation(0,720, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            rAnim.setDuration(duration);
            rAnim.setFillAfter(true);
            tAnim.setDuration(duration);
            tAnim.setStartOffset(i * 100 / count);//设置动画执行的延迟时间
            tAnim.setFillAfter(true);
            //先添加旋转再添加平移，否则播放动画时顺序有点乱
            animSet.addAnimation(rAnim);
            animSet.addAnimation(tAnim);
            childView.startAnimation(animSet);
            //一定要设置动画的监听，在动画执行完后，如果是CLOSE状态childView设为不可见，否则一直是可见的
            animSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (currentState == State.CLOSE) {
                        childView.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            //Log.i("TAG-->>", " 当前状态 ：" + currentState);
            //设置item的点击事件
            final int position = i + 1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.menuClick(v, position);
                    }
                    //点击item时的动画，被点击的item放大透明度变淡，其他的item放小透明度变淡，最后改变currentState
                    clickItemAnim(position-1,300);
                }
            });
        }
        changeState();//改变状态

    }

    public void clickItemAnim(int position,int duration) {
        for(int i = 0 ;i < getChildCount() - 1 ;i++){
            View childView = getChildAt(i + 1);
            if(position == i){
                //被点击的放大，透明度变暗
                childView.startAnimation(itemBigAnim(duration));
            }else{
                //其他的缩小，透明度变暗
                childView.startAnimation(itemSmallAnim(duration));
            }
            childView.setClickable(false);
            childView.setFocusable(false);
        }

        changeState();
    }

    private Animation itemBigAnim(int duration) {
        ScaleAnimation sAnim = new ScaleAnimation(1.0f,4.0f,1.0f,4.0f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation aAnim = new AlphaAnimation(1.0f,0.0f);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(sAnim);
        set.addAnimation(aAnim);
        set.setDuration(duration);
        set.setFillAfter(true);
        return set;
    }

    private Animation itemSmallAnim(int duration) {
        ScaleAnimation sAnim = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation aAnim = new AlphaAnimation(1.0f,0.0f);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(sAnim);
        set.addAnimation(aAnim);
        set.setDuration(duration);
        set.setFillAfter(true);
        return set;
    }


    /**改变状态*/
    private void changeState() {
        currentState = (currentState == State.CLOSE ? State.OPEN : State.CLOSE);
    }

    private void mainBtnAnim(int duration) {
        RotateAnimation anim = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true);
        mainBtn.startAnimation(anim);
    }

    /**菜单是否为打开状态*/
    public boolean getCurrentState(){
        return currentState == State.OPEN;
    }

}
