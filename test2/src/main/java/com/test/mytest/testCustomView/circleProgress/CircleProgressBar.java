package com.test.mytest.testCustomView.circleProgress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.test.mytest.R;

/**
 * 圆形进度条
 * Created by cj on 2016/1/15.
 */
public class CircleProgressBar extends View {
    /**画笔*/
    private Paint paint;
    /**圆环的宽度*/
    private float circleWidth;
    /**最外层圆环的颜色*/
    private int circleColor;
    /**进度环的颜色*/
    private int progressColor;
    /**进度字体的颜色*/
    private int textColor;
    /**进度字体的大小*/
    private float textSize;
    /**进度最大值*/
    private int max;
    /**当前进度*/
    private int progress;
    /**是否显示中间进度值文字*/
    private boolean textIsDisplayable;

    /**圆环样式，实心或空心*/
    private static final int PAINT_STYLE_STROKE = 0;
    private static final int PAINT_STYLE_FILL = 1;
    private int paintStyle = -1;//当前画笔样式

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        circleWidth = array.getDimension(R.styleable.CircleProgressBar_circleWidth,5);
        circleColor = array.getColor(R.styleable.CircleProgressBar_circleColor, Color.GRAY);
        progressColor = array.getColor(R.styleable.CircleProgressBar_progressColor, Color.RED);
        textColor = array.getColor(R.styleable.CircleProgressBar_progressTextColor, Color.RED);
        textSize = array.getDimension(R.styleable.CircleProgressBar_progressTextSize, 15);
        max = (int) array.getDimension(R.styleable.CircleProgressBar_progressMax,100);
        textIsDisplayable = array.getBoolean(R.styleable.CircleProgressBar_textIsDisplayable,true);
        paintStyle = array.getInt(R.styleable.CircleProgressBar_paintStyle, 0);
        array.recycle();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //最外层圆环
        paint.setColor(circleColor);
        paint.setStrokeWidth(circleWidth);
        paint.setStyle(Paint.Style.STROKE);
        int center = getWidth()/2;//获取圆心的x坐标
        int radius = (int) (center - circleWidth/2);//圆环的半径
        canvas.drawCircle(center, center, radius, paint);

        //进度百分比文字
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体为粗体
        int percent = (int)(((float)progress / (float)max) * 100);//中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%");//测量文字的宽度
        if(textIsDisplayable && percent != 0 && paintStyle == PAINT_STYLE_STROKE){
            canvas.drawText(percent + "%",center - textWidth/2,center + textSize/2,paint);
        }

        //进度环
        paint.setColor(progressColor);
        paint.setStrokeWidth(circleWidth);
        RectF rectF = new RectF(center - radius,center - radius,center + radius,center + radius);
        switch(paintStyle){
            case PAINT_STYLE_STROKE:
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rectF,-90,360 * progress / max,false,paint);
                break;
            case PAINT_STYLE_FILL:
                paint.setStyle(Paint.Style.FILL);
                canvas.drawArc(rectF,-90,360 * progress / max,true,paint);
                break;
        }

    }

    public synchronized int getMax() {
        return max;
    }

    /**设置进度最大值*/
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**获取进度*/
    public synchronized int getProgress() {
        return progress;
    }

    /**设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新*/
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();//刷新重绘界面
        }
    }

    public float getCircleWidth() {
        return circleWidth;
    }

    public void setCircleWidth(float circleWidth) {
        this.circleWidth = circleWidth;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setPaintStyle(int style){
        this.paintStyle = style;
    }

    public int getPaintStyle(){
        return paintStyle;
    }
}
