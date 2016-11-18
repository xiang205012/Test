package com.gordon.test1.zoomImageView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by gordon on 2016/8/22.
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    /**只进行一次获取*/
    private boolean mOnce = false;

    private float mInitScale;

    private float mMidScale;

    private float mMaxScale;

    private Matrix mScaleMatrix;

    private ScaleGestureDetector scaleGestureDetector;

    //*********************************

    private int mLastPointerCount;
    private float lastX;
    private float lastY;
    private int mTouchSlop;
    private boolean isMove;

    // *********************************

    private RectF mBorderRectF;

    public void setmBorderRectF(RectF rectF){
        this.mBorderRectF = rectF;
    }

    private GestureDetector mGestureDetector;
    private boolean isAutoScale;

    public ZoomImageView(Context context) {
        this(context,null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        scaleGestureDetector = new ScaleGestureDetector(context,this);
        setOnTouchListener(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                if(isAutoScale) return true;

                float x = e.getX();
                float y = e.getY();
                Log.d("tag-->>>","\n/**************  onDoubleTap *************************");
                Log.d("tag-->>>"," getX(): " + getX() + "  getY(): " + getY()
                                    + "  e.getX(): " + e.getX() + "  e.getY(): " + e.getY());
                Log.d("tag-->>>","onDoubleTap currentScale : " + getCurrentScale());
                Log.d("tag-->>>","mMidScale/currentScale : " + mMidScale / getCurrentScale());
                Log.d("tag-->>>","mInitScale/currentScale : " + mInitScale / getCurrentScale());
                Log.d("tag-->>>","\n/*************   onDoubleTap  *************************");
                if(getCurrentScale() < mMidScale){
//                    mScaleMatrix.postScale(mMidScale / getCurrentScale(),
//                            mMidScale / getCurrentScale(),x,y);
//                    setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mMidScale,x,y),16);
                }else {
//                    mScaleMatrix.postScale(mInitScale / getCurrentScale(),
//                            mInitScale / getCurrentScale(),x,y);
//                    setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mInitScale,x,y),16);
                }


                return true;
            }
        });
    }

    private class AutoScaleRunnable implements Runnable{

        private float targetScale;
        private float x;
        private float y;

        private float bigScale = 1.2f;// 最大缩放梯度
        private float smallScale = 0.7f;// 最小缩放梯度

        private float tmpScale;

        public AutoScaleRunnable(float targetScale,float x,float y){
            this.targetScale = targetScale;
            this.x = x;
            this.y = y;

            if(getCurrentScale() < targetScale){
                tmpScale = bigScale;
            }
            if(getCurrentScale() > targetScale){
                tmpScale = smallScale;
            }

        }

        @Override
        public void run() {
            Log.d("tag-->>>","run before : " +
                    "  currentScale: " + getCurrentScale() +
                    "  tmpScale: " + tmpScale +
                    "  targetScale: " + targetScale);
            mScaleMatrix.postScale(tmpScale,tmpScale,x,y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            if((getCurrentScale() < targetScale && tmpScale > 1.0f)
                    || (getCurrentScale() > targetScale && tmpScale < 1.0f)){
                isAutoScale = true;
                postDelayed(this,16);
            }else {
                tmpScale = targetScale / getCurrentScale();
                mScaleMatrix.postScale(tmpScale,tmpScale,x,y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
            Log.d("tag-->>>","run after : " +
                    "  currentScale: " + getCurrentScale() +
                    "  tmpScale: " + tmpScale +
                    "  targetScale: " + targetScale);

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
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /***
     * 加载图片大小
     */
    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
            // 获得控件的宽高
            int width = getWidth();
            int height = getHeight();

            // 获取图片
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            float scale = 1.0f;
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();
            if(dw > width && dh < height){
                scale = width * 1.0f / dw;
            }
            if(dw < width && dh > height){
                scale = height * 1.0f / dh;
            }
            if((dw < width && dh < height) || (dw > width && dh > height)){
                scale = Math.min(width * 1.0f / dw,height * 1.0f / dh);
            }

            mInitScale = scale/2;
            mMidScale = scale * 2;
            mMaxScale = scale * 4;

            // 将图片移动到图片的中心
            int dx = width / 2 - dw / 2;
            int dy = height / 2 - dh / 2;

            mScaleMatrix.postTranslate(dx,dy);
//            mScaleMatrix.postScale(scale,scale,width/2,height/2);
            setImageMatrix(mScaleMatrix);
            Log.d("tag-->>","dw: "+ dw + "  dh: " + dh + "  width : " + width + "  height: " + height
                            + "  mInitScale: " + mInitScale + "  mMidScale: " + mMidScale
                            + "  mMaxScale: " + mMaxScale);
            mOnce = true;
        }
    }

    private float getCurrentScale(){
        float[] values = new float[9];
        // 将mScaleMatrix中数组拷贝一份到values中
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if(getDrawable() == null) return true;
        float currentScale = getCurrentScale();
        float scaleFactor = detector.getScaleFactor();
        Log.d("tag-->>>","scaleFactor : " + scaleFactor);
        // 当前缩放比例小于最大值，并且缩放scaleFactor的缩放比率大于1表示还可以放大，同理第二个判断是可以缩小
        if((currentScale < mMaxScale && scaleFactor > 1.0f) ||
                (currentScale > mInitScale && scaleFactor < 1.0f)){
            if(currentScale * scaleFactor < mInitScale){
                scaleFactor = mInitScale / currentScale;// 重置为最小值
            }
            if(currentScale * scaleFactor > mMaxScale){
                scaleFactor = mMaxScale / currentScale;// 重置为最大值
            }
            // 以控件中心点发大缩小
            //mScaleMatrix.postScale(scaleFactor,scaleFactor,getWidth()/2,getHeight()/2);
            // 以手指中心点放大缩小
            mScaleMatrix.postScale(scaleFactor,scaleFactor,detector.getFocusX(),detector.getFocusY());

//            checkBorderAndCenterWhenScale();

            setImageMatrix(mScaleMatrix);
        }
        Log.d("tag-->>","onScale");
        return true;
    }

    private void checkBorderAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        float width = (float) getWidth();
        float height = (float) getHeight();
//        float width = (float) (getWidth() + 0.01);
//        float height = (float) (getHeight() + 0.01);
        // {{ 检查边界，如果图片大于等于控件的宽高让其在缩放时不留空白，如果小于有空白是正常的
        if(rectF.width() >= width){
            if(rectF.left > 0){
                deltaX = -rectF.left;
            }
            if(rectF.right < width){
                deltaX = width - rectF.right;
            }
        }
        if(rectF.height() >= height){
            if(rectF.top > 0){
                deltaY = -rectF.top;
            }
            if(rectF.bottom < height){
                deltaY = height - rectF.bottom;
            }
        }
        // }}

        // 如果宽度或高度小于控件的宽高，让其居中
        if(rectF.width() < width){
            deltaX = width/2f - rectF.right + rectF.width()/2f;
        }
        if (rectF.height() < height){
            deltaY = height/2f - rectF.bottom + rectF.height()/2f;
        }
        // 通过matrix调整位置
        Log.d("tag-->>","缩放时检查边界：" + "  deltaX: " + deltaX + "  deltaY: " + deltaY);
        mScaleMatrix.postTranslate(deltaX,deltaY);

    }

    /**获取放大缩小后图片的宽高，和所在矩形的 l,t,r,b*/
    private RectF getMatrixRectF(){
//        RectF 这个是获取到的坐标位置
//        matrix.mapRect(RectF)把坐标位置放入矩阵
//        根据宽高拿到在view在parent左上右下四个点的矩形范围
        RectF rectF = new RectF();
        Matrix matrix = mScaleMatrix;
        Drawable drawable = getDrawable();
        if(drawable != null){
            rectF.set(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            //将这个matrix应用与一个矩阵，矩阵的信息依旧写入到它自己中。通过设置矩阵的4个顶点来完成设置
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(mGestureDetector.onTouchEvent(event)){
            return true;// 双击时不允许缩放和平移
        }

        scaleGestureDetector.onTouchEvent(event);
        float x = 0;
        float y = 0;
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x/pointerCount;
        y = y/pointerCount;
        if(mLastPointerCount != pointerCount) {
            isMove = false;
            lastX = x;
            lastY = y;
        }
        mLastPointerCount = pointerCount;
        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastX;
                float dy = y - lastY;
                if(!isMove) isMove = checkIsMove(dx,dy);
                if(isMove){
                    RectF rectF = getMatrixRectF();
                    if(getDrawable() != null) {
                        if (rectF.width() < getWidth() + 0.01){
                            dx = 0;
                        }
                        if (rectF.height() < getHeight() + 0.01){
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx,dy);
                        checkBorderWhenPostTranlater(getMatrixRectF());
                        setImageMatrix(mScaleMatrix);
                    }
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }

        return true;
    }

    private void checkBorderWhenPostTranlater(RectF rectF) {
        float dx = 0;
        float dy = 0;
        float width = mBorderRectF.width();
        float height = mBorderRectF.height();
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
        Log.d("tag-->>","平移时检查边界：" + "  dx: " + dx + "  dy: " + dy);
        mScaleMatrix.postTranslate(dx,dy);
    }

    private boolean checkIsMove(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }
}
