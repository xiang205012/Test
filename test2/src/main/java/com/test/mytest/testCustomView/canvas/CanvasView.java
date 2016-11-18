package com.test.mytest.testCustomView.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/1/14.
 */
public class CanvasView extends View {

    /**画笔*/
    private Paint mPaint;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();//不能在onDraw或onLayout方法中实例化
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //Paint.setStrokeJoin(Join join)设置结合处的样子，Miter:结合处为锐角， Round:结合处为圆弧：BEVEL：结合处为直线。
        //setStrokeMiter(float miter )是设置笔画的倾斜度
        //mPaint.setStyle(Paint.Style.FILL);//实心
        //mPaint.setStyle(Paint.Style.STROKE);//空心
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔刷的样式
        mPaint.setStrokeWidth(10);//画笔的宽度，以px为单位
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆
        //canvas.drawCircle(100,100,100,mPaint);

        //画弧形
        //RectF rectF = new RectF(0,0,100,200);//圆弧所在的矩形区域
        //canvas.drawArc(rectF,0,90,false,mPaint);//如果设置成true，从rectF的中心开始绘制

        //画颜色
        //canvas.drawColor(Color.BLUE);

        //画线
        //canvas.drawLine(10,20,90,120,mPaint);

        //画椭圆（内切椭圆）
        //RectF oval = new RectF(0,0,100,200);
        //canvas.drawOval(oval,mPaint);

        //画字
        //mPaint.setTextSize(20);
        //canvas.drawText("android",20,30,mPaint);
        //canvas.drawText("javaandroid", 2, 7, 40, 150, mPaint);绘制从2到7的位置

        //画矩形
        //canvas.drawRect(40, 40, 90, 100, mPaint);
        //RectF rect = new RectF(50,150,140,200);
        //canvas.drawRoundRect(rect,20,20,mPaint);//圆角矩形,矩形圆角为：x方向20px y方向20px

        //绘制path
        Path path = new Path();
        path.moveTo(10, 10);
        path.lineTo(100, 90);
        path.lineTo(40, 60);
        path.lineTo(10, 10);
        canvas.drawPath(path,mPaint);

    }
}
