package com.xiang.weixin60.guaguaka;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.xiang.weixin60.R;

/**
 * 刮刮卡效果
 * Created by Administrator on 2016/2/23.
 */
public class GuaGuaKaView extends View {

    /**画框的笔*/
    private Paint outterPaint;
    /**手指滑动的路径*/
    private Path mPath;
    /**path的画板*/
    private Canvas mCanvas;
    /**涂层区域颜色*/
    private int outterColor;
    /**最上层显示的涂层区域*/
    private Bitmap mBitmap;
    /**手指滑动的最后位置点的坐标*/
    private int lastX;
    private int lastY;
    /**涂层区域中显示的图片，如果该图片不能沾满整个涂层区域，那么其他地方就显示的是mBitmap所构建的mCanvas画出来的东西*/
    private Bitmap outterBitmap;
    /**手指刮开时画笔的宽度*/
    private int strokeWidth;
// ------- 以上是 画上层遮盖图形的属性 ------

    /**中奖信息*/
    private String textString;
    /**文字颜色*/
    private int textColor;
    /**中奖信息文字所在的矩形区域，用来使文字居中*/
    private Rect textBound;
    /**中奖信息文字画笔*/
    private Paint bottomPaint;
    /**中奖信息文字大小*/
    private float textSize;
    /**标记用户是否已刮开设定的阈值，这里设60%.此变量因为在子线程中也用到了，所以加 volatite关键字防止与主线程冲突*/
    private volatile boolean isComplete = false;

    private OnScratchOverListener mListener;

    public interface OnScratchOverListener{
        void onScratchComplete();
    }

    public void setOnScratchOverListener(OnScratchOverListener listener){
        this.mListener = listener;
    }

    /**供外界动态设置中奖信息*/
    public void setTextInfo(String msg){
        this.textString = msg;
        // 一定要重新测量文字信息的宽高，确保绘制时文字能居中
        bottomPaint.getTextBounds(msg,0,msg.length(),textBound);
    }

    public GuaGuaKaView(Context context) {
        this(context, null);
    }

    public GuaGuaKaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaKaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GuaGuaKaView);
        textString = array.getString(R.styleable.GuaGuaKaView_gua_view_textInfo);
        textSize = array.getDimension(R.styleable.GuaGuaKaView_gua_view_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,20,getResources().getDisplayMetrics()));
        textColor = array.getColor(R.styleable.GuaGuaKaView_gua_view_textColor, Color.BLACK);
        strokeWidth = array.getInteger(R.styleable.GuaGuaKaView_gua_view_strokeWidth,10);
        outterColor = array.getColor(R.styleable.GuaGuaKaView_gua_view_outterColor, Color.parseColor("#c0c0c0"));
        BitmapDrawable drawable = (BitmapDrawable) array.getDrawable(R.styleable.GuaGuaKaView_gua_view_bitmap);
        outterBitmap = drawable.getBitmap();
        array.recycle();

        init();
    }

    private void init() {
        outterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        // canvas初始化需要有bitmap,而bitmap的创建需要宽高，所以在onMeasure中初始化
        //outterBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fg_guaguaka);
        textBound = new Rect();
        bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // 初始化bitmap(一个没有内容的bitmap)
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 初始化 canvas(使用bitmap的宽高创建canvas画布)
        mCanvas = new Canvas(mBitmap);
        // 设置绘制path画笔的一些属性
        setOutterPaintAttrs();
        // 设置中奖信息文字的画笔
        setUpBackPaint();
        // 给画布染上颜色
        //mCanvas.drawColor(Color.parseColor("#c0c0c0"));
        // 画一个圆角矩形(圆角为30)
        mCanvas.drawRoundRect(new RectF(0,0,width,height),20,20,outterPaint);
        // 画最上层的那个刮刮乐图片
        mCanvas.drawBitmap(outterBitmap,null,new RectF(0,0,width,height),null);

    }
    /**设置中奖信息文字的画笔属性*/
    private void setUpBackPaint() {
        bottomPaint.setColor(textColor);
        bottomPaint.setStyle(Paint.Style.FILL);
        bottomPaint.setTextSize(textSize);
        // 获取文字所在的矩形宽高，width = textBound.width,height = textBound.height;
        bottomPaint.getTextBounds(textString, 0, textString.length(), textBound);
    }

    /**设置绘制path画笔的一些属性*/
    private void setOutterPaintAttrs() {
        outterPaint.setColor(outterColor);
        outterPaint.setDither(true);
        outterPaint.setStrokeJoin(Paint.Join.ROUND);
        outterPaint.setStrokeCap(Paint.Cap.ROUND);
        outterPaint.setStyle(Paint.Style.FILL);
        outterPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制底图
//        canvas.drawBitmap(bottomBimtap, 0, 0, null);
        canvas.drawText(textString,getWidth()/2 - textBound.width()/2,
                getHeight()/2 + textBound.height()/2,bottomPaint);
        if(!isComplete) {//没有达到阈值就继续让用户刮
            // 绘制上层遮盖物(mBitmap在onMeasure中获取了mCanvas的所有属性,其实是在绘制mCnavas(而mCanvas是一个#c0c0c0色的圆角画布和一张刮刮乐图片))
            canvas.drawBitmap(mBitmap, 0, 0, null);// 对应 xmode2.jpg中的DST
            drawPath();// 对应 xmode2.jpg中的src
        }else{//达到阈值就全部显示出来(就是不绘制path和mBitmap就行了)，同时提供接口回调
            if(mListener != null) mListener.onScratchComplete();
        }
    }

    private void drawPath() {
        outterPaint.setStyle(Paint.Style.STROKE);
        outterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, outterPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                mPath.moveTo(lastX,lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs(x - lastX);
                int dy = Math.abs(y - lastY);
                if(dx > 3 || dy > 3){
                    mPath.lineTo(x,y);
                }
                break;
            case MotionEvent.ACTION_UP:
                // 在子线程中获取用户刮开的区域大小
                new Thread(mRunnable).start();
                break;
        }
        invalidate();
        return true;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int w = getWidth();
            int h = getHeight();
            float wipeArea = 0;// 刮开的区域大小
            float totalArea = w * h;// 总区域大小
            Bitmap bitmap = mBitmap;
            int[] mPixels = new int[w * h];
            // 获取Bitmap上所有的像素信息
            bitmap.getPixels(mPixels,0,w,0,0,w,h);
            for(int i = 0; i < w; i++){
                for(int j = 0; j < h; j++){
                    int index = i + j*w;
                    if(mPixels[index] == 0){
                        wipeArea++;
                    }
                }
            }
            if(wipeArea > 0 && totalArea > 0){
                // 刮开区域占总区域的百分比
                int parent = (int)(wipeArea * 100 / totalArea);
                Log.i("TAG","刮开区域的大小："+parent+"%");
                if(parent > 60){// 如果大小60%，就自动全部刮开(在代码中就是不绘制path和mBitmap)
                    isComplete = true;
                    postInvalidate();
                }
            }

        }
    };

}
