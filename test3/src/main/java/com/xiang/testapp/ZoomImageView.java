package com.xiang.testapp;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by gordon on 2016/10/9.
 */

public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener {

    private boolean isOnce;
    private float minScale;
    private float midScale;
    private float maxScale;
    private float currentScale;
    private Matrix matrix;

    private ScaleGestureDetector scaleGestureDetector;

    private int lastX = 0;
    private int lastY = 0;
    private int lastPointCount = 0;
    private boolean isCanDrag = false;
    private boolean isLeftAndRight;
    private boolean isTopAndBottom;
    private int mTouchSlop = 0;

    private GestureDetector gestureDetector;
    private boolean isAutoScale;

    public ZoomImageView(Context context) {
        this(context,null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        matrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        scaleGestureDetector = new ScaleGestureDetector(context,this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                if(isAutoScale) return true;
                float x = e.getX();
                float y = e.getY();
                float currentScale = getCurrentScale();
                if(currentScale < midScale){
                    postDelayed(new DelayedRunnable(x,y,midScale),16);
                    isAutoScale = true;
                }else {
                    postDelayed(new DelayedRunnable(x,y,minScale),16);
                    isAutoScale = true;
                }
                return true;
            }
        });


    }

    private class DelayedRunnable implements Runnable{

        private float x;
        private float y;
        private float targetScale;
        private float temScale;

        private static final float BIG = 1.023f;
        private static final float SMALL = 0.78f;

        public DelayedRunnable(float x, float y, float targetScale){
            this.x = x ;
            this.y = y;
            this.targetScale = targetScale;
            if(targetScale < midScale){
                temScale = BIG;
            }else {
                temScale = SMALL;
            }
        }


        @Override
        public void run() {
            matrix.postScale(temScale,temScale,x,y);
            checkBorderInScale();
            setImageMatrix(matrix);

            float currentScale = getCurrentScale();
            if((currentScale < targetScale && temScale > 1)
                    || currentScale >= targetScale && temScale < 1){
                postDelayed(this,16);
            }else {
                float scale = targetScale / currentScale;
                matrix.postScale(scale,scale,x,y);
                checkBorderInScale();
                setImageMatrix(matrix);
                isAutoScale = false;
            }


        }
    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (!isOnce) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                float width = getWidth();
                float height = getHeight();
                float dw = drawable.getIntrinsicWidth();
                float dh = drawable.getIntrinsicHeight();

                if (dw > width && dh < height) {
                    currentScale = height / dh ;
                }

                if (dw < width && dh > height){
                    currentScale = width / dw;
                }

                if((dw > width && dh > height) || (dw < width && dh < height)){
                    currentScale = Math.min(width / dw,height / dh);
                }

                minScale = currentScale;
                midScale = minScale * 2;
                maxScale = minScale * 4;

                float dx = width / 2 - dw / 2;
                float dy = height / 2 - dh / 2;

                matrix.postTranslate(dx,dy);
                matrix.postScale(minScale,minScale,width / 2,height / 2);
                setImageMatrix(matrix);

            }

            isOnce = true;
        }
    }

    private RectF getMatrixRectf(){
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if(drawable != null){
            rectF.set(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    private float getCurrentScale(){
        float[] value = new float[9];
        matrix.getValues(value);
        return value[Matrix.MSCALE_X];
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        if (getDrawable() == null) {
            return false;
        }

        float function = detector.getScaleFactor();
        float currentScale = getCurrentScale();

        if((currentScale < minScale && function < 1.0f)
                || (currentScale > maxScale && function > 1.0f)){

            if(currentScale * function < minScale){
                function = minScale / currentScale;
            }
            if(currentScale * function > maxScale){
                function = maxScale / currentScale;
            }
            matrix.postScale(function,function,detector.getFocusX(),detector.getFocusY());
            checkBorderInScale();
            setImageMatrix(matrix);

        }
        return true;
    }

    private void checkBorderInScale() {
        RectF rectF = getMatrixRectf();
        int width = getWidth();
        int height = getHeight();

        float dx = 0;
        float dy = 0;

        if(rectF.width() > width){
            if(rectF.left > 0){
                dx = -rectF.left;
            }
            if(rectF.right < width){
                dx = width - rectF.right;
            }
        }

        if(rectF.height() > height){
            if(rectF.top > 0){
                dy = -rectF.top;
            }
            if(rectF.bottom < height){
                dy = height - rectF.bottom;
            }
        }

        if(rectF.width() < width){
            dx = width / 2 - rectF.right + rectF.width() / 2;
        }
        if(rectF.height() < height){
            dy = height / 2 - rectF.bottom + rectF.height() / 2;
        }
        matrix.postTranslate(dx,dy);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        scaleGestureDetector.onTouchEvent(event);

        int dx = 0;
        int dy = 0;
        int x = 0;
        int y = 0;
        int pointCount = event.getPointerCount();
        for (int i = 0; i < pointCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x /= pointCount;
        y /= pointCount;
        if (lastPointCount != pointCount) {
            lastPointCount = pointCount;
            lastX = x;
            lastY = y;
        }

        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:
                dx = x - lastX;
                dy = y - lastY;
                RectF rectF = getMatrixRectf();
                isLeftAndRight = isTopAndBottom = true;
                isCanDrag = (Math.sqrt(dx * dx + dy * dy) > mTouchSlop);
                if(isCanDrag){
                    if(rectF.width() < getWidth()){
                        isLeftAndRight = false;
                        dx = 0;
                    }
                    if (rectF.height() < getHeight()){
                        isTopAndBottom = false;
                        dy = 0;
                    }
                    matrix.postTranslate(dx,dy);
                    checkBorderInTranslate();
                    setImageMatrix(matrix);

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointCount = 0;
                break;

        }
        return true;
    }

    private void checkBorderInTranslate() {
        checkBorderInScale();
    }
}
