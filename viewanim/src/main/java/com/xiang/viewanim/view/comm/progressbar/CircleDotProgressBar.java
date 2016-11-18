package com.xiang.viewanim.view.comm.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xiang.viewanim.R;

/**
 * Created by gordon on 2016/10/26.
 * http://blog.csdn.net/a10615/article/details/52658927
 */

public class CircleDotProgressBar extends View {

    /**最大进度值*/
    private int progressMax;
    /**当前进度*/
    private int progress;
    /**当前进度百分比*/
    private int percent;

    /**完成进度点的颜色*/
    private int dotColor;
    /**未完成进度点的颜色*/
    private int dotBgColor;
    // {{ 显示模式
    /**空*/
    public static final int SHOW_MODE_NULL = 0;
    /**只有百分比和单位*/
    public static final int SHOW_MODE_PERCENT = 1;
    /**全部显示 包括百分比，单位，按钮*/
    public static final int SHOW_MODE_ALL = 2;
    /**当前显示模式*/
    private int showMode;
    // }}

    // {{ 百分比样式
    /**百分比数字字体大小*/
    private float percentTextSize;
    /**百分比数字字体颜色*/
    private int percentTextColor;
    /**百分比数字字体是否使用系统字体 默认为false(使用外部导入字体 assets/fonts/HelveticaNeueLTPro.ttf)*/
    private boolean isPercentFontSystem;
    /**百分比瘦身大小*/
    private int percentThinPadding;
    /**百分比单位*/
    private String unitText;
    /**百分比单位字体大小*/
    private float unitTextSize;
    /**百分比单位字体颜色*/
    private int unitTextColor;

    // 单位与百分比数字的底部对齐方式
    /**默认对齐*/
    public static final int UNIT_TEXT_ALIGN_MODE_DEFAULT = 0;
    /**中文对齐*/
    public static final int UNIT_TEXT_ALIGN_MODE_CN = 1;
    /**英文对齐：主要针对底部长的字母或字符，如：g、j、p、q、y、$、@等，其他几乎字母或字符都可以使用默认对齐*/
    public static final int UNIT_TEXT_ALIGN_MODE_EN = 2;
    /**当前单位与百分比底部对齐方式*/
    private int unitTextAlignMode;
    // }}

    // {{ 按钮
    /**按钮文字*/
    private String buttonText;
    /**按钮文字大小*/
    private float buttonTextSize;
    /**按钮文字颜色*/
    private int buttonTextColor;
    /**按钮背景颜色*/
    private int buttonBgColor;
    /**按钮点击时颜色*/
    private int buttonClickColor;
    /**按钮点击时颜色*/
    private int buttonClickBgColor;
    /**按钮与上面百分比之间的间距*/
    private float buttonTopOffset;
    // }}

    private Paint mPaint;
    /**画按钮*/
    private Path mPath;
    private RectF mRectF;
    /** sin(1°) */
    private float mSin_1;
    /**按钮是否被触摸到*/
    private boolean mIsButtonTouched;
    /**设置字体工具类*/
    private Typeface mPercentTypeface;
    /**按钮背景的方块左上角开始点*/
    private PointF mButtonRect_start;
    /**按钮背景的方块右下角结束点*/
    private PointF mButtonRect_end;
    /**按钮上圆的半径*/
    private float mButtonRadius;
    /*整个控件的中心点*/
    private float mCenterX;
    private float mCenterY;

    private Canvas mCanvas;
    private Bitmap mBitmap;
    /**清空canvas的Xfermode*/
    private Xfermode mClearCavansXfermode;
    /**百分比瘦身大小Xfermode*/
    private Xfermode mPercentThinXfermode;

    private OnButtonClickListener mButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener listener){
        this.mButtonClickListener = listener;
    }

    public interface OnButtonClickListener{
        void onButtonClick(View view);
    }

    public CircleDotProgressBar(Context context) {
        this(context,null);
    }

    public CircleDotProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleDotProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleDotProgressBar);
        progressMax = array.getInt(R.styleable.CircleDotProgressBar_progressMax,100);
        dotColor = array.getColor(R.styleable.CircleDotProgressBar_dotColor, Color.WHITE);
        dotBgColor = array.getColor(R.styleable.CircleDotProgressBar_dotBgColor,Color.GRAY);
        showMode = array.getInt(R.styleable.CircleDotProgressBar_showMode,SHOW_MODE_PERCENT);

        if (showMode != SHOW_MODE_NULL) {
            percentTextSize = array.getDimension(R.styleable.CircleDotProgressBar_percentTextSize,dp2px(30));
            percentTextColor = array.getColor(R.styleable.CircleDotProgressBar_percentTextColor,Color.WHITE);
            isPercentFontSystem = array.getBoolean(R.styleable.CircleDotProgressBar_isPercentFontSystem,false);
            percentThinPadding = array.getInt(R.styleable.CircleDotProgressBar_percentThinPadding,0);

            unitText = array.getString(R.styleable.CircleDotProgressBar_unitText);
            unitTextSize = array.getDimension(R.styleable.CircleDotProgressBar_unitTextSize,percentTextSize);
            unitTextColor = array.getColor(R.styleable.CircleDotProgressBar_unitTextColor,percentTextColor);
            unitTextAlignMode = array.getInt(R.styleable.CircleDotProgressBar_unitTextAlignMode,UNIT_TEXT_ALIGN_MODE_DEFAULT);
            if (unitText == null) {
                unitText = "%";
            }
        }

        if (showMode == SHOW_MODE_ALL) {
            buttonText = array.getString(R.styleable.CircleDotProgressBar_buttonText);
            buttonTextSize = array.getDimension(R.styleable.CircleDotProgressBar_buttonTextSize,dp2px(16));
            buttonTextColor = array.getColor(R.styleable.CircleDotProgressBar_buttonTextColor,Color.GRAY);
            buttonBgColor = array.getInt(R.styleable.CircleDotProgressBar_buttonBgColor, Color.WHITE);
            buttonClickColor = array.getInt(R.styleable.CircleDotProgressBar_buttonClickColor, buttonBgColor);
            buttonClickBgColor = array.getInt(R.styleable.CircleDotProgressBar_buttonClickBgColor, buttonTextColor);
            buttonTopOffset = array.getDimension(R.styleable.CircleDotProgressBar_buttonTopOffset, dp2px(15));
            if (buttonText == null) {
                buttonText = "一键加速";
            }
        }
        array.recycle();

        // 其他准备工作
        // 求sin(1°)。角度需转换成弧度
        mSin_1 = (float) Math.sin(Math.toRadians(1));
        Log.d("tag"," Math.toRadians(1) ：" + Math.toRadians(1));
        Log.d("tag"," Math.sin(Math.toRadians(1)) ：" + Math.sin(Math.toRadians(1)));
        Log.d("tag","  mSin_1 : " + mSin_1);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mRectF = new RectF();
        mButtonRect_start = new PointF();
        mButtonRect_end = new PointF();
        mPercentTypeface = isPercentFontSystem ? Typeface.DEFAULT
                : Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeueLTPro.ttf");
        mClearCavansXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        if (percentThinPadding != 0) {
            mPercentThinXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mCenterX = getWidth() / 2f;
        mCenterY = getHeight() / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制外围圆点
        drawCircleDot(mCanvas);

        if (showMode != SHOW_MODE_NULL) {
            // 绘制百分比和单位
            drawPercentUnit(mCanvas);
            if (showMode == SHOW_MODE_ALL) {
                // 画按钮
                drawButton(mCanvas);
            }
        }

        canvas.drawBitmap(mBitmap,0,0,null);

    }

    private void drawCircleDot(Canvas mCanvas) {
        // 先清除上次会绘制的
        mPaint.setXfermode(mClearCavansXfermode);
        mCanvas.drawPaint(mPaint);// 在canvas上将设置了CLEAR配置的paint画上去，就清空了
        mPaint.setXfermode(null);

        // 计算最外层圆的半径
        float outerRadius = (getWidth() < getHeight() ? getWidth() : getHeight()) / 2f;
        Log.d("tag"," width : " + getWidth() + "   height : " + getHeight() + "  outerRadius : " + (getHeight() / 2f));
        // 计算圆点半径
        float dotRadius = mSin_1 * outerRadius / (1 + mSin_1);
        Log.d("tag"," dotRadius : " + dotRadius + " \n1 + mSin_1 : " + (1 + mSin_1)
                + " \nouterRadius : " + outerRadius + " \nouterRadius / (1 + mSin_1) : " + (outerRadius / (1+mSin_1)));

        // 画进度
        mPaint.setColor(dotColor);
        mPaint.setStyle(Paint.Style.FILL);
        int count = 0;
        // 1.1 当前进度
        while(count++ < percent){
            mCanvas.drawCircle(mCenterX,mCenterY - outerRadius + dotRadius,dotRadius,mPaint);
            mCanvas.rotate(3.6f,mCenterX,mCenterX);
        }
        Log.d("tag","进度点圆心Y坐标 ：" + (mCenterY - outerRadius + dotRadius));
        // 1.2 未完成进度
        mPaint.setColor(dotBgColor);
        count--;
        while(count++ < 100){
            mCanvas.drawCircle(mCenterX,mCenterX - outerRadius + dotRadius,dotRadius,mPaint);
            mCanvas.rotate(3.6f,mCenterX,mCenterY);
        }
    }

    private void drawPercentUnit(Canvas mCanvas) {
        // 测量百分比和单位的宽度
        mPaint.setTypeface(mPercentTypeface);
        mPaint.setTextSize(percentTextSize);
        float percentTextWidth = mPaint.measureText(percent + "");

        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(unitTextSize);
        float unitTextWidth = mPaint.measureText(unitText);

        float textWidth = percentTextWidth + unitTextWidth;

        float baseLine = 0;
        if (showMode == SHOW_MODE_PERCENT) {
            float textHeight = percentTextSize > unitTextSize ? percentTextSize : unitTextSize;
            // 计算Text 垂直居中时的baseline
            mPaint.setTextSize(textHeight);
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            // 字体在垂直居中时，字体中间就是centerY，加上字体实际高度的一半就是descent线，减去descent就是baseline线的位置（fm中以baseline为基准）
            baseLine = mCenterY + ((fm.descent - fm.ascent) / 2 - fm.descent);
            Log.d("tag"," mCenterY : " + mCenterY
                    + " \n fm.top : " + fm.top
                    + " \n fm.bottom : " + fm.bottom
                    + " \n fm.ascent :　" + fm.ascent
                    + " \n fm.descent : " + fm.descent
                    + " \n centerY - fm.descent : " + (mCenterY - fm.descent)
                    + " \n baseLine : " + baseLine);
        } else if (showMode == SHOW_MODE_ALL){
            baseLine = mCenterY;
        }

        // 2.1 画百分比
        mPaint.setTypeface(mPercentTypeface);
        mPaint.setTextSize(percentTextSize);
        mPaint.setColor(percentTextColor);
        mCanvas.drawText(percent + "",mCenterX - textWidth / 2,baseLine,mPaint);
        // 2.1.1 对百分比瘦身
        if (percentThinPadding != 0) {
            mPaint.setXfermode(mPercentThinXfermode);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(percentThinPadding);
            mCanvas.drawText(percent + "",mCenterX - textWidth / 2,baseLine,mPaint);
            mPaint.setXfermode(null);
            mPaint.setStyle(Paint.Style.FILL);
        }

        // 2.2 画单位
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(unitTextSize);
        mPaint.setColor(unitTextColor);
        // 单位对齐方式
        Paint.FontMetrics fm_unit = mPaint.getFontMetrics();
        switch (unitTextAlignMode){
            case UNIT_TEXT_ALIGN_MODE_CN:
                baseLine -= fm_unit.descent / 4;
                break;
            case UNIT_TEXT_ALIGN_MODE_EN:
                baseLine -= fm_unit.descent * 2 / 3;
                break;
        }
        mCanvas.drawText(unitText,mCenterX - textWidth / 2 + percentTextWidth,baseLine,mPaint);

    }

    private void drawButton(Canvas mCanvas) {
        // 3 画按钮
        mPaint.setTextSize(buttonTextSize);
        float buttonTextWidth = mPaint.measureText(buttonText);
        Paint.FontMetrics btnFm = mPaint.getFontMetrics();

        // 3.1 画按钮背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mIsButtonTouched ? buttonClickBgColor : buttonBgColor);
        float buttonHeight = 2 * buttonTextSize;
        mButtonRadius = buttonHeight / 2;

        mButtonRect_start.set(mCenterX - buttonTextWidth / 2,mCenterY + buttonTopOffset);
        mButtonRect_end.set(mCenterX + buttonTextWidth / 2,mCenterY + buttonTopOffset + buttonHeight);

        mPath.reset();
        mPath.moveTo(mButtonRect_start.x,mButtonRect_start.y);
        //同LineTo，区别在于LineTo中的(x,y)是对应于坐标原点。而此处(dx,dy)是对应于路径结束点的相对坐标。其他r*函数都类似。
        mPath.rLineTo(buttonTextWidth,0);
        float left = mCenterX + buttonTextWidth / 2 - mButtonRadius;
        float top = mCenterY + buttonTopOffset;
        float right = left + 2 * mButtonRadius;
        float bottom = top + 2 * mButtonRadius;
        mRectF.set(left,top,right,bottom);
        // arcTo和addArc的区别在于:
        // 1. 首先，使用addArc可以直接加入一段椭圆弧。而使用arcTo还需要使用moveTo指定当前点的坐标。
        // 2. 对于arcTo来说，如果当前点坐标和欲添加的曲线的起始点不是同一个点的话，还会自动添加一条直线补齐路径。
        mPath.arcTo(mRectF,270,180);// 参数1：内切这个方形，参数2：开始角度，参数3：画的角度范围
        mPath.rLineTo(-buttonTextWidth,0);
        mRectF.offset(-buttonTextWidth,0);// 将矩形平移
        mPath.arcTo(mRectF,90,180);
        mPath.close();
        mCanvas.drawPath(mPath,mPaint);

        // 3.2 画按钮文本
        mPaint.setColor(mIsButtonTouched ? buttonClickColor : buttonTextColor);
        float baseline = mCenterY + buttonTopOffset + buttonTextSize + (btnFm.descent - btnFm.ascent) /2 - btnFm.descent;
        mCanvas.drawText(buttonText,mCenterX - buttonTextWidth / 2,baseline,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (showMode == SHOW_MODE_ALL) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isTouchInButton(event.getX(),event.getY())){
                        mIsButtonTouched = true;
                        postInvalidate();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mIsButtonTouched) {
                        if (!isTouchInButton(event.getX(),event.getY())) {
                            mIsButtonTouched = false;
                            postInvalidate();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mIsButtonTouched && mButtonClickListener != null) {
                        mButtonClickListener.onButtonClick(this);
                    }
                    mIsButtonTouched = false;
                    postInvalidate();
                    break;
            }

            if (mIsButtonTouched) {
                return true;
            }

        }

        return super.onTouchEvent(event);
    }

    /**
     * 判断点击的坐标是否在按钮中
     * @param x
     * @param y
     * @return true 在按钮中，false 不在
     */
    private boolean isTouchInButton(float x, float y) {
        // 判断是否在矩形中
        if (x >= mButtonRect_start.x && x <= mButtonRect_end.x
                && y >= mButtonRect_start.y && y <= mButtonRect_end.y){
            return true;
        }

        // 判断是否在左圆内
        float centerX = mButtonRect_start.x;
        float centerY = (mButtonRect_end.y - mButtonRect_start.y) / 2;
        float newX = x - centerX;
        float newY = y - centerY;
        if (newX * newX + newY * newY <= mButtonRadius * mButtonRadius){
            return true;
        }

        // 判断是否在右圆内
        centerX = mButtonRect_end.x;
        newX = x - centerX;
        return newX * newX + newY * newY <= mButtonRadius * mButtonRadius;
    }

    private int dp2px(int dp){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public synchronized int getProgressMax() {
        return progressMax;
    }

    public synchronized void setProgressMax(int progressMax) {
        if (progressMax < 0){
            throw new IllegalArgumentException(" progressMax can not less than 0");
        }
        this.progressMax = progressMax;
    }

    public synchronized int getProgress(){
        return progress;
    }

    public synchronized void setProgress(int progress){
        if (progress < 0 || progress > progressMax) {
            throw new IllegalArgumentException("progress setting error");
        }
        this.progress = progress;
        percent = progress * 100 / progressMax;
        postInvalidate();
    }

    public int getDotColor() {
        return dotColor;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
    }

    public int getShowMode() {
        return showMode;
    }

    public void setShowMode(int showMode) {
        this.showMode = showMode;
    }

    public float getPercentTextSize() {
        return percentTextSize;
    }

    public void setPercentTextSize(float percentTextSize) {
        this.percentTextSize = percentTextSize;
    }

    public int getPercentTextColor() {
        return percentTextColor;
    }

    public void setPercentTextColor(int percentTextColor) {
        this.percentTextColor = percentTextColor;
    }

    public boolean isPercentFontSystem() {
        return isPercentFontSystem;
    }

    public void setPercentFontSystem(boolean percentFontSystem) {
        isPercentFontSystem = percentFontSystem;
    }

    public int getPercentThinPadding() {
        return percentThinPadding;
    }

    public void setPercentThinPadding(int percentThinPadding) {
        this.percentThinPadding = percentThinPadding;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    public float getUnitTextSize() {
        return unitTextSize;
    }

    public void setUnitTextSize(float unitTextSize) {
        this.unitTextSize = unitTextSize;
    }

    public int getUnitTextColor() {
        return unitTextColor;
    }

    public void setUnitTextColor(int unitTextColor) {
        this.unitTextColor = unitTextColor;
    }

    public int getUnitTextAlignMode() {
        return unitTextAlignMode;
    }

    public void setUnitTextAlignMode(int unitTextAlignMode) {
        this.unitTextAlignMode = unitTextAlignMode;
    }

    public float getButtonTextSize() {
        return buttonTextSize;
    }

    public void setButtonTextSize(float buttonTextSize) {
        this.buttonTextSize = buttonTextSize;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonTextColor(int buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
    }

    public int getButtonBgColor() {
        return buttonBgColor;
    }

    public void setButtonBgColor(int buttonBgColor) {
        this.buttonBgColor = buttonBgColor;
    }

    public int getButtonClickColor() {
        return buttonClickColor;
    }

    public void setButtonClickColor(int buttonClickColor) {
        this.buttonClickColor = buttonClickColor;
    }

    public int getButtonClickBgColor() {
        return buttonClickBgColor;
    }

    public void setButtonClickBgColor(int buttonClickBgColor) {
        this.buttonClickBgColor = buttonClickBgColor;
    }

    public float getButtonTopOffset() {
        return buttonTopOffset;
    }

    public void setButtonTopOffset(float buttonTopOffset) {
        this.buttonTopOffset = buttonTopOffset;
    }

}
