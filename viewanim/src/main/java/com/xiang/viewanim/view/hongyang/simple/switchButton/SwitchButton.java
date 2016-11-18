package com.xiang.viewanim.view.hongyang.simple.switchButton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xiang.viewanim.R;


/**
 * 滑动开关
 * Created by Administrator on 2016/2/5.
 */
public class SwitchButton extends View implements View.OnClickListener {

    /**滑动开关背景*/
    private Bitmap bg_on,bg_off,bg_btn;
    /**滑动按钮左边x*/
    private float slideBtn_left;
    /**记录用户是否在滑动*/
    private boolean isSlide = false;
    /**当前开还是关的状态*/
    private boolean status = false;
    /**按下时x位置，不动时的x*/
    private int donwX,lastX;

    private SwitchListener listener;

    public void setSwitchListener(SwitchListener listener){
        this.listener = listener;
    }

    public interface SwitchListener{
        void onSwitch(View view, boolean status);
    }

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.switch_on);
        bg_off = BitmapFactory.decodeResource(getResources(), R.drawable.switch_off);
        bg_btn = BitmapFactory.decodeResource(getResources(),R.drawable.switch_sliper);
        setOnClickListener(this);
    }

    public void setSwitchBg(int onId,int offId,int btnId){
        bg_on = BitmapFactory.decodeResource(getResources(), onId);
        bg_off = BitmapFactory.decodeResource(getResources(),offId);
        bg_btn = BitmapFactory.decodeResource(getResources(),btnId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(bg_on.getWidth(),bg_on.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(status){
            canvas.drawBitmap(bg_on,0,0,null);
        }else{
            canvas.drawBitmap(bg_off,0,0,null);
        }
        canvas.drawBitmap(bg_btn, slideBtn_left, 0, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);//如果想onClick执行必须写这句，下面是自定义处理滑动的逻辑
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                donwX = lastX = (int) event.getX();
                isSlide = false;
                slideBtn_left = donwX - bg_btn.getWidth()/2;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否发生了滑动
                if(Math.abs(event.getX() - donwX) > 5){
                    isSlide = true;
                }
                //计算手指在屏幕上移动的距离
                int dis = (int) (event.getX() - lastX);
                //将本次的位置设置给lastX
                lastX = (int) event.getX();
                //根据移动的距离，改变slideBtn_left的值
                slideBtn_left = slideBtn_left + dis;
                break;
            case MotionEvent.ACTION_UP:
                //在发生滑动的情况下，根据最后的位置，判断当前开关的状态
                if(isSlide){
                    int maxLeft = bg_on.getWidth() - bg_btn.getWidth();
                    if(slideBtn_left > maxLeft/2){
                        status = true;
                    }else{
                        status = false;
                    }
                    flushState();
                }
                //isSlide = false;
                break;
        }
        flushView();
        return true;
    }

    private void flushState() {
        if(status){
            slideBtn_left = bg_on.getWidth() - bg_btn.getWidth();
        }else{
            slideBtn_left = 0;
        }
        flushView();
    }

    private void flushView() {
        int maxLeft = bg_on.getWidth() - bg_btn.getWidth();
        slideBtn_left = (slideBtn_left <= 0) ? 0 : slideBtn_left;
        slideBtn_left = (slideBtn_left >= maxLeft) ? maxLeft : slideBtn_left;
        invalidate();
    }

    @Override
    public void onClick(View v) {
        if(!isSlide){
            status = !status;
            flushState();
        }
        Log.i("TAG", "滑动");
    }
}
