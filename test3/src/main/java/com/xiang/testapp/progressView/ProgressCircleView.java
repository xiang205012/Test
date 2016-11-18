package com.xiang.testapp.progressView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xiang.testapp.R;

/**
 * Created by gordon on 2016/10/25.
 */

public class ProgressCircleView extends View{

    private float circleWidth = 5;
    private float radius = 150;
    private int circleColor;
    private int doubleColor;
    private int textColor;
    private float textSize;
    private Paint mCirclePaint;
    private Paint mTextPaint;

    private float progress = 0.0f;
    private float max = 100f;

    private static final int FILL = 0;
    private static final int STORKE = 1;
    private int currentType = STORKE;


    public ProgressCircleView(Context context) {
        this(context,null);
    }

    public ProgressCircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.CircleView);
        circleWidth = array.getDimension(R.styleable.CircleView_circleWidth,5);
        radius = array.getDimension(R.styleable.CircleView_circleSize,150);
        int type = array.getInt(R.styleable.CircleView_circleType,STORKE);
        if (type == 0) {
            currentType = FILL;
        }else {
            currentType = STORKE;
        }
        circleColor = array.getColor(R.styleable.CircleView_circleColor, Color.WHITE);
        doubleColor = array.getColor(R.styleable.CircleView_doubleColor,Color.RED);
        textColor = array.getColor(R.styleable.CircleView_textColor,Color.WHITE);
        textSize = array.getDimension(R.styleable.CircleView_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,20,getResources().getDisplayMetrics()));
        array.recycle();

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextSize(textSize);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        mCirclePaint.setStrokeWidth(circleWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(circleColor);
        canvas.drawCircle(centerX,centerY,radius,mCirclePaint);

        float pecent = progress / max * 100f;
        String text = pecent + "%";
        float tvX = centerX - mTextPaint.measureText(text) / 2;
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text,0,text.length(),rect);
        float tvY = centerY + rect.height() / 2;

        canvas.drawText(text,tvX,tvY,mTextPaint);

        float angle = pecent * ((float)360 / max);
        mCirclePaint.setColor(doubleColor);
        RectF ovel = new RectF(centerX - radius,centerY - radius,centerX + radius,centerY + radius);
        if (currentType == STORKE) {
            mCirclePaint.setStrokeWidth(circleWidth);
            mCirclePaint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(ovel,270,angle,false,mCirclePaint);

        } else {
            mCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(ovel,270,angle,true,mCirclePaint);
        }

    }

    public void setProgress(float pecent){
        this.progress = pecent;

        if (progress < 0 || progress > 100) {
            return;
        }

        postInvalidate();
    }

    public void setMax(float max){
        this.max = max;
    }
}
