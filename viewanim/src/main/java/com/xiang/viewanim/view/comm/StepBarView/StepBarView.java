package com.xiang.viewanim.view.comm.StepBarView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.xiang.viewanim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿淘宝横向步骤进度提示，步骤指示器
 * 如：下订单----支付完成---已经发货----交易成功
 * 宽度设为：match_parent  高度建议设为：60dp
 * Created by gordon on 2016/6/5.
 */
public class StepBarView extends View {

    /**默认距离边缘的距离*/
    public static final int DEFAULT_PADDING=20;
    /**中心点Y坐标*/
    private float mCenterY=0.0f;
    // {{横线所在的矩形位置(主意从头到尾只画一条线，然后在线上画圆)
    /**横线左边x坐标*/
    private float mLeft=0.0f;
    /**横线左边y坐标*/
    private float mTop=0.0f;
    /**横线右边x坐标*/
    private float mRight=0.0f;
    /**横线右边x坐标*/
    private float mBottom=0.0f;
    // }}
    /**每个步骤之间的间距*/
    private float mDistance=0.0f;
    /**线条高度*/
    private float mLineHeight;
    /**未完成小圆的半径*/
    private float mUnDoneRadius;
    /**已完成大圆的半径*/
    private float mDoneRadius;
    /**未完成的步骤的颜色*/
    private int mUnDoneColor;
    /**完成步骤的颜色*/
    private int mDoneColor=0XFF00FF00;
    /**总步数*/
    private int mTotalStep;
    /**已完成步数*/
    private int mCompleteStep;
    private int defaultWidth;

    /**画线和画圆的笔*/
    private Paint lineAndCirclePaint;
    /**文字所在的矩形*/
    private Rect textBounds;
    /**画文字的笔*/
    private Paint textPaint;

    /**步骤集合*/
    private List<String> steps = new ArrayList<>();


    public StepBarView(Context context) {
        this(context,null);
    }

    public StepBarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StepBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StepBarView);
        mLineHeight = array.getDimensionPixelOffset(R.styleable.StepBarView_lineHeight,dp2Px(5));
        mUnDoneRadius = array.getDimensionPixelOffset(R.styleable.StepBarView_unDoneRadius,dp2Px(10));
        mDoneRadius = array.getDimensionPixelOffset(R.styleable.StepBarView_doneRadius,dp2Px(15));
        mUnDoneColor = array.getColor(R.styleable.StepBarView_unDoneColor, 0XFF808080);
        mDoneColor = array.getColor(R.styleable.StepBarView_doneColor,0XFF00FF00);
        array.recycle();
        setPadding(dp2Px(30),0,dp2Px(30),0);
        mTotalStep = steps.size();
        lineAndCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineAndCirclePaint.setStyle(Paint.Style.FILL);
        textBounds = new Rect();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(26);
    }

    /**获取相关位置*/
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterY = getHeight() / 2;
        mLeft = getLeft() + getPaddingLeft();
        mTop = mCenterY - mLineHeight / 2;
        mRight = getRight() - getPaddingRight();
        mBottom = mCenterY + mLineHeight / 2;
        if(mTotalStep > 1){
            mDistance = (mRight - mLeft) / (mTotalStep - 1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mTotalStep <= 0 || mCompleteStep < 0 || mCompleteStep > mTotalStep){
            return;
        }
        // 先画所有步骤
        lineAndCirclePaint.setColor(mUnDoneColor);
        // 画出整条线
        canvas.drawRect(mLeft,mTop,mRight,mBottom,lineAndCirclePaint);
        float xLoc = mLeft;
        // 画所有的步骤（圆形）
        for(int i = 0 ; i < mTotalStep; i++){
            canvas.drawCircle(xLoc,mCenterY,mUnDoneRadius,lineAndCirclePaint);
            // 如果直接在这里绘制文字
            // 比如：已下单
            String text = steps.get(i);
            textPaint.getTextBounds(text,0,text.length(),textBounds);
            float x = xLoc - textBounds.width()/2;
            float y = mCenterY + mDoneRadius/2 + dp2Px(20);
            if(x < 0) x = 10;
            // TODO
            if(xLoc + textBounds.width()/2 > getResources().getDisplayMetrics().widthPixels) {
                textBounds.left = getResources().getDisplayMetrics().widthPixels - textBounds.right - 10;
                canvas.drawText(text,textBounds.left,y,textPaint);
                continue;
            }
            canvas.drawText(text,x,y,textPaint);

            xLoc += mDistance;
        }

        // 画已经完成的步骤
        xLoc = mLeft;
        for(int i = 0;i <= mCompleteStep;i++){
            lineAndCirclePaint.setColor(mDoneColor);
            if(i > 0) {
                canvas.drawRect(xLoc, mTop, xLoc + mDistance, mBottom, lineAndCirclePaint);
                canvas.drawCircle(xLoc + mDistance,mCenterY,mUnDoneRadius,lineAndCirclePaint);
                lineAndCirclePaint.setColor(getTranspartColorByAlpha(mDoneColor,0.2f));
                canvas.drawCircle(xLoc + mDistance, mCenterY, mDoneRadius, lineAndCirclePaint);
                xLoc += mDistance;
            }else{ // 默认第一个圆是已完成
                canvas.drawCircle(xLoc,mCenterY,mUnDoneRadius,lineAndCirclePaint);
                lineAndCirclePaint.setColor(getTranspartColorByAlpha(mDoneColor,0.2f));
                canvas.drawCircle(xLoc,mCenterY,mDoneRadius,lineAndCirclePaint);
            }
        }

    }

    /**默认宽度为屏幕宽度减去padding值*/
    public int getDefaultWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int dp2Px(int dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density*dp + 0.5f);
    }

    /**
     * 将指定的颜色转换成制定透明度的颜色
     * @param color
     * @param ratio
     * @return
     */
    private int getTranspartColorByAlpha(int color, float ratio){
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    /**设置步骤，重绘视图*/
    public void setSteps(List<String> steps){
        this.steps = steps;
        mTotalStep = steps.size();
        invalidate();
    }
    /**已完成后进入下一步*/
    public void nextStep(){
        mCompleteStep++;
        invalidate();
    }

    /**重置*/
    public void reset(){
        mCompleteStep = 0;
        invalidate();
    }

}