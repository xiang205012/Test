package com.xiang.viewanim.view.hongyang.simple.volumControlBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xiang.viewanim.R;

/**
 * Created by Administrator on 2016/2/1.
 */
public class CustomVolumControlBar extends View {

    private int firstColor;
    private int secondColor;
    private int total;
    private int slipt;
    private int circleWidth;

    private Paint paint;
    private Rect iconRect;

    private int currentCount = 3;

    public CustomVolumControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar);
        firstColor = array.getColor(R.styleable.CustomVolumControlBar_CustomVolumControlBar_firstColor, Color.WHITE);
        secondColor = array.getColor(R.styleable.CustomVolumControlBar_CustomVolumControlBar_secondColor,Color.RED);
        total = array.getInt(R.styleable.CustomVolumControlBar_CustomVolumControlBar_total, 15);
        slipt = array.getInt(R.styleable.CustomVolumControlBar_CustomVolumControlBar_split, 20);
        circleWidth = (int) array.getDimension(R.styleable.CustomVolumControlBar_CustomVolumControlBar_circleWidth,100);
        array.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(firstColor);
        paint.setStrokeWidth(circleWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        iconRect = new Rect();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth()/2;
        int radius = center - circleWidth/2;
        drawOval(center,radius,canvas);



    }

    private void drawOval(int center,int radius, Canvas canvas) {
//        float itemSize = (180*1.0f - total * slipt)/total;
        float itemSize = (360*1.0f - total * slipt)/total;
        RectF oval = new RectF(center - radius,center - radius,center + radius,center + radius);

        paint.setColor(firstColor);
        for(int i = 0;i < total;i++){
//            canvas.drawArc(oval,180+(i * (itemSize+slipt)),itemSize,false,paint);
            canvas.drawArc(oval,i * (itemSize+slipt),itemSize,false,paint);
        }
        paint.setColor(secondColor);
        for(int i = 0;i < currentCount;i++){
//            canvas.drawArc(oval,180+i * (itemSize+slipt),itemSize,false,paint);
            canvas.drawArc(oval,i * (itemSize+slipt),itemSize,false,paint);
        }
    }
}
