package com.test.mytest.testCustomView.canvas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.test.mytest.R;

/**
 * Created by Administrator on 2016/1/15.
 */
public class ControllerBar extends View {

    private Paint paint;
    /**初始的颜色*/
    private int defaultColor;
    /**添加或选中的颜色*/
    private int selectorColor;
    /**图片*/
    private Bitmap bitmap;
    /**画圆的笔的大小*/
    private int circleWidth;
    /**每个区块间的间隔*/
    private int spliteSize;
    /**区块总数*/
    private int totalCount;
    /**当前区块个数*/
    private int currentCount;
    /**图片所在的矩形区*/
    private Rect bitmapRect;

    public ControllerBar(Context context) {
        this(context, null);
    }

    public ControllerBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControllerBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ControllerBar);
        defaultColor = array.getColor(R.styleable.ControllerBar_defaultColor, Color.GRAY);
        selectorColor = array.getColor(R.styleable.ControllerBar_selectorColor,Color.RED);
        circleWidth = (int) array.getDimension(R.styleable.ControllerBar_pathWidth,5);
        totalCount = array.getInt(R.styleable.ControllerBar_totalCount,20);
        spliteSize = (int) array.getDimension(R.styleable.ControllerBar_spliteSize,20);
        bitmap = BitmapFactory.decodeResource(getResources(),array.getResourceId(R.styleable.ControllerBar_bg,0));
        array.recycle();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(defaultColor);
        paint.setStrokeWidth(circleWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);//圆头的触笔
        paint.setStyle(Paint.Style.STROKE);
        int center = getWidth()/2;//圆环的中心
        int radius = center - circleWidth/2;//半径

        //绘制各个区块
        drawOval(canvas, center, radius);

        //绘制图片
        int relRadius = radius - circleWidth/2;//内圆半径
        bitmapRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) - circleWidth;
        bitmapRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) - circleWidth;
        bitmapRect.right = (int) (bitmapRect.left + Math.sqrt(2) * relRadius);
        bitmapRect.bottom = (int) (bitmapRect.left + Math.sqrt(2) * relRadius);
        /**
         * 如果图片比较小，那么根据图片的尺寸放置到正中心
         */
        if (bitmap.getWidth() < Math.sqrt(2) * relRadius){
            bitmapRect.left = (int) (bitmapRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - bitmap.getWidth() * 1.0f / 2);
            bitmapRect.top = (int) (bitmapRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - bitmap.getHeight() * 1.0f / 2);
            bitmapRect.right = (int) (bitmapRect.left + bitmap.getWidth());
            bitmapRect.bottom = (int) (bitmapRect.top + bitmap.getHeight());

        }
        // 绘图
        canvas.drawBitmap(bitmap, null, bitmapRect, paint);
    }


    private void drawOval(Canvas canvas, int center, int radius) {
        int itemSize = (int) ((360 * 1.0f - totalCount * spliteSize) / totalCount);//每个区块所占的空间
        RectF rectF = new RectF(center - radius,center - radius,center + radius,center + radius);
        paint.setColor(defaultColor);
        for(int i = 0 ;i < totalCount;i++){
            //单独绘制每一个，所以结束角度都是itemSize
            canvas.drawArc(rectF,i * (itemSize + spliteSize),itemSize,false,paint);
        }

        paint.setColor(selectorColor);
        for(int i = 0;i < currentCount;i++){
            canvas.drawArc(rectF,i * (itemSize + spliteSize),itemSize,false,paint);
        }
    }

}
