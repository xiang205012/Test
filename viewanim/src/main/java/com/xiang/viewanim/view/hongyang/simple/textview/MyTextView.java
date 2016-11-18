package com.xiang.viewanim.view.hongyang.simple.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.xiang.viewanim.R;

import java.util.Random;

/**
 * Created by Administrator on 2016/2/1.
 */
public class MyTextView extends View {

    private String text;
    private int color;
    private int size;

    private Paint paint;
    private Rect rect;


    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        text = array.getString(R.styleable.MyTextView_mytextview_text);
        color = array.getColor(R.styleable.MyTextView_mytextview_color, Color.RED);
        size = (int) array.getDimension(R.styleable.MyTextView_mytextview_size,30);
        array.recycle();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(color);
        paint.setTextSize(size);

        rect = new Rect();
        paint.getTextBounds(text,0,text.length(),rect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                text = randomText();
                postInvalidate();
            }
        });
    }

    private String randomText() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 4; i++){
            int randomInt = random.nextInt(10);
            sb.append(""+randomInt);
        }
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int viewWidth = 0;
        int viewHeight = 0;
        switch(widthMode){
            case MeasureSpec.EXACTLY:
                viewWidth = getPaddingLeft() + getPaddingRight() + widthSize;
                break;
            case MeasureSpec.AT_MOST:
                viewWidth = getPaddingLeft() + getPaddingRight() + rect.width();
                break;
        }

        switch(heightMode){
            case MeasureSpec.EXACTLY:
                viewHeight = getPaddingTop() + getPaddingBottom() + heightSize;
                break;
            case MeasureSpec.AT_MOST:
                viewHeight = getPaddingTop() + getPaddingBottom() + rect.height();
                break;
        }
        setMeasuredDimension(viewWidth,viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setColor(color);
        canvas.drawText(text,getWidth()/2 - rect.width()/2,getHeight()/2 + rect.height()/2,paint);

    }
}
