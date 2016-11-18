package com.xiang.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by gordon on 2016/10/11.
 */

public class SwitchButton2 extends View {

    private Bitmap bitOn;
    private Bitmap bitOff;
    private Bitmap bitMid;

    private static final int OPEN = 0;
    private static final int CLOSE = 1;
    private int currentState = CLOSE;
    private float left = 0;

    public SwitchButton2(Context context) {
        this(context,null);
    }

    public SwitchButton2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwitchButton2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        bitOn = BitmapFactory.decodeResource(getResources(),R.drawable.switch_on);
        bitOff = BitmapFactory.decodeResource(getResources(),R.drawable.switch_off);
        bitMid = BitmapFactory.decodeResource(getResources(),R.drawable.switch_sliper);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(bitOn.getWidth(),bitOn.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(currentState == CLOSE){
            left = 0;
            canvas.drawBitmap(bitOff,0,0,new Paint());
        }else {
            left = bitOn.getWidth() - bitMid.getWidth();
            canvas.drawBitmap(bitOn,0,0,new Paint());
        }

        canvas.drawBitmap(bitMid,left,0,new Paint());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float dx = 0;
        float lastX = 0;

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                float downX = event.getX();
                if (downX > bitMid.getWidth()) {
                    left = lastX = downX;
                }
                Log.d("tag"," down lastX : " + lastX + " left : " + left);
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getX();
                dx = currentX - lastX;
                left += dx;
                Log.d("tag"," check before  left ： " + left + " dx : " + dx);
                checkBorder(left);
                Log.d("tag","  check after  left : " + left);
                lastX = currentX;
                break;
            case MotionEvent.ACTION_UP:
                checkStateInLeft(left);
                Log.d("tag","  up currentState : " + currentState + "  up left : " + left);
                break;
        }

        return true;
    }

    private void checkStateInLeft(float left) {
        if (left > (bitOn.getWidth() - (bitOn.getWidth() / 2 + bitMid.getWidth() / 2))) {
            currentState = OPEN;
        }else {
            currentState = CLOSE;
        }
        invalidate();
    }

    private void checkBorder(float lefts) {
        if(lefts < 0){
            left = 0;
        }else if(lefts > bitOn.getWidth() - bitMid.getWidth()){
            left = bitOn.getWidth() - bitMid.getWidth();
        }
        invalidate();
    }
}
