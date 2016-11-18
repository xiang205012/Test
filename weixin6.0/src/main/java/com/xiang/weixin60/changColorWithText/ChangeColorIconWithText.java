package com.xiang.weixin60.changColorWithText;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xiang.weixin60.R;

/**
 * Created by Administrator on 2016/1/21.
 */
public class ChangeColorIconWithText extends View {

    //自定义的一些属性
    private Bitmap iconBitmap;
    private int color = 0xFF45C01A;
    private float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
    private String text = getResources().getString(R.string.weixin);

    //绘图
    private Canvas bitmapCanvas;
    /**
     * 底部纯色bitmap
     */
    private Bitmap bottomBitmap;
    private Paint bitmapPaint;
    /**
     * 图片和文字的透明度，用来不断改变颜色(0.0f ~ 1.0f)
     */
    private float alpha;
    /**
     * 图片所在的矩形框
     */
    private Rect iconRect;
    /**
     * 文字所在的矩形框
     */
    private Rect textRect;
    /**
     * 绘制文字
     */
    private Paint textPaint;

    public ChangeColorIconWithText(Context context) {
        this(context, null);
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorIconWithText);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) array.getDrawable(R.styleable.ChangeColorIconWithText_iconBitmap);
        this.iconBitmap = bitmapDrawable.getBitmap();
        this.color = array.getColor(R.styleable.ChangeColorIconWithText_bottomColor, color);
        this.text = array.getString(R.styleable.ChangeColorIconWithText_text);
        this.textSize = array.getDimension(R.styleable.ChangeColorIconWithText_textSize, textSize);
        array.recycle();

        init();
    }

    private void init() {
        textRect = new Rect();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0Xff555555);
        textPaint.setTextSize(textSize);
        //测量文字的宽高，并将宽度放到textRect中,
        // 然后通过 textRect.left/right/top/bottom来获取文字所在矩形四个点的位置
        textPaint.getTextBounds(text, 0, text.length(), textRect);

    }

    /**
     * 测量图片的大小,确定icon绘制的范围
     * 有两种情况，①宽度大于高度 ②高度大于宽度
     * 此时为了确保view绘制在中心取这两种情况的最小值(此时也确定了该view中的内容区域大小就是width=最小值 height=最小值的一个正方形)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //图片所在宽度方向上的最小值
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - textRect.height());

        //根据图片的宽度来确定图片所在的矩形区域位置
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = getMeasuredHeight() / 2 - (textRect.height() + iconWidth) / 2;
        //确定图片所在矩形区域
        iconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制原图片
        canvas.drawBitmap(iconBitmap, null, iconRect, null);
        //向上取义（最大255）转为int是因为paint设置alpha时是int类型
        //测试 alpha = 1.0f;
        int bitmapAlpha = (int) Math.ceil(255 * alpha);

        //在内存中绘制一个bitmap--setAlpha--设置纯色--设置xfermode--原图
        //得到的是一个有透明度的纯色图标
        setUpTargetBitmap(bitmapAlpha);
        //绘制原文字
        drawSourceText(canvas,bitmapAlpha);
        //绘制可变色的文字
        drawChangeColorText(canvas,bitmapAlpha);
        //将绘制在内存的bitmap绘制到该view上
        canvas.drawBitmap(bottomBitmap,0,0,null);
    }

    /**绘制原文字*/
    private void drawSourceText(Canvas canvas, int bitmapAlpha) {
        textPaint.setColor(0xff333333);//原文本颜色
        //当可变色文字alpha为0时，原文字完全不透明，当为1时原文字完全透明，由此文字颜色不断交替
        textPaint.setAlpha(255 - bitmapAlpha);
        int left = getMeasuredWidth()/2 - textRect.width()/2;
        int bottom = iconRect.bottom + textRect.height();
        canvas.drawText(text, left, bottom, textPaint);
    }
    /**绘制可变色文字*/
    private void drawChangeColorText(Canvas canvas, int bitmapAlpha) {
        textPaint.setColor(color);
        textPaint.setAlpha(bitmapAlpha);
        int left = getMeasuredWidth()/2 - textRect.width()/2;
        int bottom = iconRect.bottom + textRect.height();
        canvas.drawText(text,left,bottom,textPaint);
    }

    /**在内存中绘制可变色的icon*/
    private void setUpTargetBitmap(int bitmapAlpha) {
        //创建bitmap用于显示纯色(Config.ARGB_8888是可变色的样式)
        bottomBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        //初始化画布和画笔
        bitmapCanvas = new Canvas(bottomBitmap);//绘制底图的画布
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint.setColor(color);
        bitmapPaint.setAlpha(bitmapAlpha);
        bitmapPaint.setDither(true);
        //绘制底图(就是一个和原图大小一致的纯色矩形)
        bitmapCanvas.drawRect(iconRect,bitmapPaint);

        //开始绘制原图，DST_IN:将纯色渲染到原图上，
        // 此时是不需要透明度的，bottomBitmap通过alpha得到的颜色就是渲染到原图上的颜色
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        bitmapPaint.setAlpha(255);
        bitmapCanvas.drawBitmap(iconBitmap, null, iconRect, bitmapPaint);

    }

    /**设置透明度*/
    public void setBitmapAlpha(float alpha){
        this.alpha = alpha;
        invalidateView();
    }

    /**视图重绘，可在非UI线性刷新*/
    private void invalidateView() {
        if(Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        }else{
            postInvalidate();
        }
    }

    /** View系统自己的保存和恢复 */
    private static final String INSATANCE_STATE = "instance_state";
    /** 我们自己的保存和恢复*/
    private static final String CUSTOM_STATE = "custom_state";

    /**防止系统不正常回收时变量值重置了，所以保存需要保存的值*/
    /**View和Activity一样都有系统自己的一套保存和恢复机制，但是我们自定义view需要重写*/
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        //保存view系统的
        bundle.putParcelable(INSATANCE_STATE,super.onSaveInstanceState());
        //保存自己的
        bundle.putFloat(CUSTOM_STATE,alpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            //说明是我们自己保存的
            Bundle bundle = (Bundle) state;
            //view系统的给view自己去恢复
            //恢复我们自己的
            alpha = bundle.getFloat(CUSTOM_STATE);
            super.onRestoreInstanceState(bundle.getParcelable(INSATANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}