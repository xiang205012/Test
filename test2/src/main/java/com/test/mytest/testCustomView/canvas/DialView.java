package com.test.mytest.testCustomView.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * 绘制钟表盘
 * Created by Administrator on 2016/1/14.
 */
public class DialView extends View{

    /**大圆画笔*/
    private Paint circlePaint;
    /**文字画笔*/
    private Paint textPaint;
    /**刻度画笔*/
    private Paint scalePaint;
    /**图形抗锯齿*/
    private DrawFilter drawFilter;

    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.RED);
        circlePaint.setStrokeWidth(8);
        circlePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLUE);
        textPaint.setTextAlign(Paint.Align.CENTER);//设置文字居中对齐
        textPaint.setTextSize(30);
        textPaint.setStrokeWidth(5);

        scalePaint = new Paint(textPaint);
        scalePaint.setColor(Color.RED);
        scalePaint.setStrokeWidth(3);

        drawFilter = new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG |Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将canvas中心点(默认是0,0)移动到屏幕的中心
        //canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        //RectF rectF = new RectF(0,0,250,250);
        //canvas.drawRect(rectF, textPaint);
        //canvas.save();
        ////在上一次平移之后的中心再平移，此时画布就平移了canvas.getWidth()/2+100,canvas.getHeihgt()/2+100
        //canvas.translate(100,100);
        //canvas.drawRect(rectF, scalePaint);
        //测试平移实例(画一个尺子)
        //drawrule(canvas);

        //测试缩放实例
        //drawScale(canvas);

        //测试旋转实例(钟表盘)
        drawRotate(canvas);

    }

    /**利用canvas.translate绘制尺子*/
    private void drawrule(Canvas canvas) {
        /**尺子高度*/
        int ruleHeight = pxTodp(40);
        /**尺子距屏幕宽度左右间距*/
        int ruleMargin = pxTodp(10);
        /**第一条线距离边框距离*/
        int firstLineMargin = pxTodp(10);
        /**每格所占屏幕的宽度的*/
        int lerval;
        /**尺子底部位置*/
        int ruleBottom = canvas.getHeight()/2 + ruleHeight;

        Rect outRect = new Rect(ruleMargin,canvas.getHeight()/2,canvas.getWidth()-ruleMargin,ruleBottom);

        canvas.drawRect(outRect,circlePaint);
        lerval = (canvas.getWidth() - 2 * ruleMargin - 2 * firstLineMargin)/90;//最好100以下，超过100屏幕不适配

        canvas.save();
        canvas.translate(ruleMargin + firstLineMargin, 0);//第一条线的x y坐标
        Log.i("TAG--", "尺子的高度 ： " + outRect.height());
        int top = 60;//刻度线最高值
        int index = 0;//0,1,2,3,4...
        for(int i = 0;i <= 90;i++){
            int keHeight = 0;
            if(i % 10 == 0){
                keHeight = top;
                canvas.drawLine(0,ruleBottom,0,ruleBottom - keHeight,textPaint);
                canvas.drawText(index + "", 0, ruleBottom - keHeight - 10, textPaint);
                index++;
            }else if(i % 5 == 0){
                keHeight = top-10;
                canvas.drawLine(0,ruleBottom,0,ruleBottom - keHeight,textPaint);
            }else{
                keHeight = top-20;
                canvas.drawLine(0,ruleBottom,0,ruleBottom - keHeight,textPaint);
            }
            //canvas.drawLine(0,ruleBottom,0,ruleBottom - keHeight,textPaint);
            canvas.translate(lerval,0);
        }
        canvas.restore();
    }


    private void drawScale(Canvas canvas) {
        Rect outRect = new Rect(0,(canvas.getHeight()/2)-200,canvas.getWidth(),(canvas.getHeight()/2)+200);
        for(int i = 0;i < 10;i++){
            canvas.save();
            float fraction = (float)i/10;
            //px和py 分别为缩放的基准点,如果是两个参数的构造方法默认是原点为基准点
            canvas.scale(fraction,fraction,canvas.getWidth()/2,canvas.getHeight()/2);
            canvas.drawRect(outRect,circlePaint);
            canvas.restore();
        }
    }

    /**绘制钟表盘*/
    private void drawRotate(Canvas canvas) {
        int centerX = canvas.getWidth()/2;
        int centerY = canvas.getHeight()/2;
        /**一共是120格（360度）*/
        int count = 120;
        int radius = 300;
        canvas.translate(centerX, centerY);
        canvas.drawCircle(0, 0, radius, circlePaint);
        canvas.drawLine(0, 0, 30, -150, textPaint);
        RectF rectF = new RectF(-(radius+10),-(radius+10),radius+10,radius+10);
        Path textPath = new Path();
        textPath.addArc(rectF,-180,180);
        canvas.drawTextOnPath("google.android",textPath,0,0,textPaint);
        //RectF rectF = new RectF(0,0,radius+10,radius+10);
        //canvas.drawArc(rectF,0,90,true,textPaint);
        //canvas.drawRect(rectF,circlePaint);
        canvas.rotate(180);
        canvas.save();
        int index = 1;
        boolean isFirst = false;
        for(int i = 0;i < count; i++){
            if(i % 10 == 0){//长线
                canvas.drawLine(0, radius, 0, radius - 70, circlePaint);
                if(!isFirst) {
                    canvas.drawText(12 + "", 0, radius - 70 - 20, textPaint);
                    isFirst = true;
                }else{
                    canvas.drawText(index + "", 0, radius - 70 - 20, textPaint);
                    index++;
                }
            }else if(i % 5 == 0){
                canvas.drawText(index + "", 0, radius - 55, textPaint);
            } else {//短线
                canvas.drawLine(0, radius, 0, radius - 40, textPaint);
            }
            canvas.rotate(360 / count, 0, 0);

        }
        canvas.restore();




//3、旋转后, 图片的抗锯齿。
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
//                | Paint.FILTER_BITMAP_FLAG)); //设置图形、图片的抗锯齿。可用于线条等。按位或.

    }

    private int pxTodp(float size){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,size,getResources().getDisplayMetrics());
    }



}
