package com.xiang.viewanim.view.comm.testA_Z_ListView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xiang.viewanim.util.ToastUtil;


/**
 * A-#的绘制view
 * Created by CJ on 2015/11/13.
 */
public class A_Z_View extends View{

    /**
     * 26个字母
     */
    public static String[] LETTERS = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };

    /**
     * 绘制字母的笔
     */
    private Paint paint = new Paint();

    /**
     * 每个字母所在单元格的宽高
     */
    private float cellHeight;
    private float cellWidth;

    /**
     * 记录上次单元格的位置，用于避免重复调用
     */
    private int lastIndex = -1;

    private LetterChangeListener listener;

    /**
     * 点击字母时的回调接口
     */
    public interface LetterChangeListener{
        void letterChange(String text);
    }

    public void setLetterChangeListener(LetterChangeListener listener){
        this.listener = listener;
    }

    public A_Z_View(Context context) {
        this(context, null);
    }

    public A_Z_View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public A_Z_View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 绘制字母
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置默认黑体
        paint.setAntiAlias(true);//打开抗锯齿
        paint.setTextSize(20);

        for(int i = 0;i < LETTERS.length;i++){
            String text = LETTERS[i];
            //字母在屏幕中x坐标：该view宽度的一半 - 字母宽度的一半 (paint.measureText:只能得到宽度)
            float xPos = cellWidth / 2 - paint.measureText(text) / 2;
            //字母在屏幕中y坐标：先得到文字所在的矩形从而得到文字的高度，加上上面所有单元格的高度
            Rect rect = new Rect();//矩形
            paint.getTextBounds(text, 0, text.length(), rect);//测量文字所在的矩形
            float textHeight = rect.height();//矩形的高度即是文字的高度
            float yPos = cellHeight / 2 + textHeight / 2 + cellHeight * i;
            paint.setColor(lastIndex == i ? Color.GRAY : Color.BLACK);//重绘时改变颜色
            //绘制文字
            canvas.drawText(text,xPos,yPos,paint);
        }
    }

    /**
     * 通过点击时的y坐标来确定是哪个字母(即y值在哪个字母所在的单元格内)
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = -1;//字母所在单元格的位置
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                index = (int) (event.getY() / cellHeight);
                if(index >= 0 && index < LETTERS.length){
                    //当move时也许y值还没有移出当前单元格的高度
                    //那么index还是一样的，所以记录当前index(一个单元格只调用一次)，判断，避免重复调用
                    if(index != lastIndex){//如果不是同一个单元格才调用
                        if(listener != null){
                            listener.letterChange(LETTERS[index]);
                            ToastUtil.showToast(this.getContext(), LETTERS[index]);
                            Log.i("TAG", LETTERS[index]);
                        }
                        lastIndex = index;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                lastIndex = -1;//恢复lastIndex;
                break;
        }
        invalidate();//点击时重绘(改变点击字母的颜色)
        return true;
    }

    /**
     * 获取单元格的大小
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cellWidth = getMeasuredWidth();//单元格的宽就是布局中写的宽

        float mHeight = getMeasuredHeight();//整个布局的高
        cellHeight = mHeight / LETTERS.length;//每一个单元格的高

    }
}
