package com.xiang.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xiang.testapp.utils.LogUtils;

/**
 * Created by Administrator on 2016/2/29.
 */
public class SwitchButton extends View{

    private Bitmap onBitmap;
    private Bitmap offBitmap;
    private Bitmap slideBitmap;
    private int width;
    private int slideWidth;
    private boolean isOpen;
    private boolean isSlide;
    private float slide_left;
    private int downX,lastX;

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

    public void init(){
        onBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.switch_on);
        offBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.switch_off);
        slideBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.switch_sliper);
        width = onBitmap.getWidth();
        slideWidth = slideBitmap.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(onBitmap.getWidth(), onBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isOpen){
            canvas.drawBitmap(onBitmap,0,0,null);
        }else {
            canvas.drawBitmap(offBitmap,0,0,null);
        }

        canvas.drawBitmap(slideBitmap, slide_left, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = lastX = (int) event.getX();
                isSlide = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getX() - lastX;
                lastX = (int) event.getX();
                if(Math.abs(dx) > 5){
                    isSlide = true;
                }
                slide_left = slide_left + dx;
                break;
            case MotionEvent.ACTION_UP:
                if(!isSlide) {
                    if(isOpen){
                        slide_left = 0;
                    }else{
                        slide_left = width - slideWidth;
                    }
                    isOpen = !isOpen;
                }else{
                    int halfWidth = (width - slideWidth)/2;
                    if(slide_left > halfWidth){
                        slide_left = width - slideWidth;
                        isOpen = true;
                    }else{
                        slide_left = 0;
                        isOpen = false;
                    }
                }
                break;
        }
        LogUtils.d("onTouchEvent:"+event.getAction());
        controlRange();
        invalidate();

        return true;
    }

    private void controlRange() {
        slide_left = slide_left < 0 ? 0 : slide_left;
        slide_left = slide_left > width - slideWidth ? width - slideWidth : slide_left;
    }

}
