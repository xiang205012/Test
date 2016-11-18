package com.gordon.test1.zoomImageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gordon on 2016/8/16.
 */
public class CropView extends View {

    private Paint paint = new Paint();

    private Paint borderPaint = new Paint();
    private int borderColor = Color.parseColor("#ffffff");

    /** 自定义顶部栏高度，如不是自定义，则默认为0即可 */
    private int customTopBarHeight = 0;
    /** 裁剪框长宽比，默认4：3 */
    private double clipRatio = 0.75;
    /** 裁剪框宽度 */
    private int clipWidth = -1;
    /** 裁剪框高度 */
    private int clipHeight = -1;
    /** 裁剪框左边空留宽度 */
    private int clipLeftMargin = 0;
    /** 裁剪框上边空留宽度 */
    private int clipTopMargin = 0;
    /** 裁剪框边框宽度 */
    private int clipBorderWidth = 1;
    private boolean isSetMargin = false;

    private RectF rectF ;

    public RectF getBorderRectF(){
        return rectF;
    }

    public CropView(Context context) {
        super(context);
    }

    public CropView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 如果没有设置裁剪的宽高，则默认以下值
        if(clipWidth == -1 || clipHeight == -1){
            clipWidth = width - 100;
            clipHeight = (int) (clipWidth * clipRatio);

            // 横屏
            if(width > height){
                clipHeight = height - 100;
                clipWidth = (int) (clipHeight / clipRatio);
            }
        }
        // 如果没有设置裁剪框左边margin，则默认以下
        if(clipLeftMargin == 0){
            clipLeftMargin = (width - clipWidth) / 2;
        }
        // 同上
        if(clipTopMargin == 0){
            clipTopMargin = (height - clipHeight) / 2;
        }

        // 画阴影
        paint.setAlpha(50);// 数值越小越透明

        // top
        canvas.drawRect(0,customTopBarHeight,width,clipTopMargin,paint);
        // left
        canvas.drawRect(0,clipTopMargin,clipLeftMargin,height,paint);
        // bottom
        canvas.drawRect(clipLeftMargin,clipHeight + clipTopMargin,
                width,height,paint);
        // right
        canvas.drawRect(clipLeftMargin + clipWidth,clipTopMargin,
                width,clipHeight + clipTopMargin,paint);

        // 画中间的裁剪框
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(clipBorderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(clipLeftMargin,clipTopMargin,
                clipLeftMargin + clipWidth,clipTopMargin + clipHeight,borderPaint);

        rectF = new RectF(clipLeftMargin,clipTopMargin,
                clipLeftMargin + clipWidth,clipTopMargin + clipHeight);

    }


    public int getCustomTopBarHeight() {
        return customTopBarHeight;
    }

    public void setCustomTopBarHeight(int customTopBarHeight) {
        this.customTopBarHeight = customTopBarHeight;
    }

    public double getClipRatio() {
        return clipRatio;
    }

    public void setClipRatio(double clipRatio) {
        this.clipRatio = clipRatio;
    }

    public int getClipWidth() {
        return clipWidth;
    }

    public void setClipWidth(int clipWidth) {
        this.clipWidth = clipWidth;
    }

    public int getClipHeight() {
        return clipHeight;
    }

    public void setClipHeight(int clipHeight) {
        this.clipHeight = clipHeight;
    }

    public int getClipLeftMargin() {
        return clipLeftMargin;
    }

    public void setClipLeftMargin(int clipLeftMargin) {
        this.clipLeftMargin = clipLeftMargin;
    }

    public int getClipTopMargin() {
        return clipTopMargin;
    }

    public void setClipTopMargin(int clipTopMargin) {
        this.clipTopMargin = clipTopMargin;
    }

    public int getClipBorderWidth() {
        return clipBorderWidth;
    }

    public void setClipBorderWidth(int clipBorderWidth) {
        this.clipBorderWidth = clipBorderWidth;
    }

    public boolean isSetMargin() {
        return isSetMargin;
    }

    public void setSetMargin(boolean setMargin) {
        isSetMargin = setMargin;
    }

}
