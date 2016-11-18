package com.xiang.viewanim.view.QQ.goo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.nineoldandroids.animation.ValueAnimator;
import com.xiang.viewanim.util.GeometryUtil;

/**
 * 粘性控件
 * Created by Administrator on 2016/2/17.
 */
public class GooView extends View {

    private Paint mPaint;
    /**状态栏高度*/
    private int statusBarHeight;

    public GooView(Context context) {
        this(context, null);
    }

    public GooView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);
    }

    PointF dragCenter = new PointF(450f,450f);
    float dragRadius = 55f;
    PointF stickCenter = new PointF(600f,600f);
    float stickRadius = 40f;
    // 固定圆上两个点
    PointF[] mStickPoints = new PointF[]{
            new PointF(250f,250f),//右上角点
            new PointF(250f,350f)//右下角点
    };
    // 拖拽圆上的两个点
    PointF[] mDragPoints = new PointF[]{
            new PointF(50f,250f),// 左上角点
            new PointF(50f,350f)// 左下角点
    };
    // 曲线控制点
    PointF controlPoint = new PointF(150f,300f);
    /**拖拽时两圆间的最大范围*/
    float maxDistance = 250f;
    /**标记拖拽是否超出范围*/
    private boolean isOutOfRange = false;
    /**标记是否将整个view消失*/
    private boolean isDisappear = false;

    @Override
    protected void onDraw(Canvas canvas) {

        // 画出两个圆之间的连接部分
        testPath(canvas);

    }

    private void testPath(Canvas canvas) {
        Path mPath = new Path();
        // 假设要画出的图形所在的矩形宽高分别为200,100
        // 假设右上角坐标为250,250,
        // 那么左上点为50,250，
        // 左下点为50,350，
        // 右下点为250,350，
        // 中心点为150,300
        // mPath.moveTo(250f,250f);
        // mPath.quadTo(150f, 300f, 50f, 250f);// 绘制右上和左上点的连接曲线
        // mPath.lineTo(50f, 350f);//绘制左上和左下点的连接直线
        // mPath.quadTo(150f, 300f, 250f, 350f);//绘制左下和右下点的连接曲线
        // mPath.close();// 将绘制的线封闭起来
        // canvas.drawPath(mPath, mPaint);// 画出图形

        // 动态计算连接点的坐标

            // 1.根据两圆圆心的距离改变而改变固定圆的半径
            float tempStickRadius = getTempStickRadius();

            // 2.获取直线与圆的交点，详细看图
            float xOffset = stickCenter.x - dragCenter.x;
            float yOffset = stickCenter.y - dragCenter.y;
            Double lineK = null;
            if(xOffset != 0){// 获取斜率，三角函数，邻边比对边
                lineK = (double)(yOffset / xOffset);
            }
            // 通过集合图形工具获取交点坐标
            mDragPoints = GeometryUtil.getIntersectionPoints(dragCenter,dragRadius,lineK);
            mStickPoints = GeometryUtil.getIntersectionPoints(stickCenter, tempStickRadius, lineK);

            // 3.获取控制点坐标
            controlPoint = GeometryUtil.getMiddlePoint(dragCenter,stickCenter);

        // 移动画布 因为在onTouch中获取的x,y是相对屏幕坐标系的，
        // 所以在拖拽拖动圆时，圆心在y方向总是与手指中心相差一个状态栏高度
        canvas.save();
        canvas.translate(0, -statusBarHeight);

        // 画出最大范围圆(参考用)
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(stickCenter.x, stickCenter.y, maxDistance, mPaint);
        mPaint.setStyle(Paint.Style.FILL);

        if(!isDisappear){
            if(!isOutOfRange) {// 只有在没有超出范围时才有连接部分和固定圆，当超出范围时只有拖拽圆，固定圆和连接部分消失(重绘时不画)
                mPath.moveTo(mStickPoints[0].x, mStickPoints[0].y);
                mPath.quadTo(controlPoint.x, controlPoint.y, mDragPoints[0].x, mDragPoints[0].y);// 绘制右上和左上点的连接曲线
                mPath.lineTo(mDragPoints[1].x, mDragPoints[1].y);//绘制左上和左下点的连接直线
                mPath.quadTo(controlPoint.x, controlPoint.y, mStickPoints[1].x, mStickPoints[1].y);//绘制左下和右下点的连接曲线
                mPath.close();// 将绘制的线封闭起来
                canvas.drawPath(mPath, mPaint);// 画出图形

                // 画出两个圆连接的四个点
                mPaint.setColor(Color.BLUE);
                canvas.drawCircle(mDragPoints[0].x, mDragPoints[0].y, 8.0f, mPaint);
                canvas.drawCircle(mDragPoints[1].x, mDragPoints[1].y, 8.0f, mPaint);
                canvas.drawCircle(mStickPoints[0].x, mStickPoints[0].y, 8.0f, mPaint);
                canvas.drawCircle(mStickPoints[1].x, mStickPoints[1].y, 8.0f, mPaint);
                mPaint.setColor(Color.RED);

                // 画固定圆
                //canvas.drawCircle(400f,400f,14f,mPaint);
                canvas.drawCircle(stickCenter.x, stickCenter.y, tempStickRadius, mPaint);
            }
            // 画移动圆
            //canvas.drawCircle(300f,300f,20f,mPaint);
            canvas.drawCircle(dragCenter.x,dragCenter.y,dragRadius,mPaint);
        }

        // 恢复之前保存的画布状态
        canvas.restore();
    }

    /**根据两圆圆心的距离改变而改变固定圆的半径*/
    private float getTempStickRadius() {
        // 两圆圆心距离
        float distance = GeometryUtil.getDistanceBetween2Points(dragCenter,stickCenter);
        // 一开始时两圆是重叠的所以distance是0，最大是maxDistance
        //if(distance > maxDistance) distance = maxDistance;
        distance = Math.min(distance,maxDistance);
        // 0.0f --> 1.0f
        float percent = distance / maxDistance;
        Log.i("TAG","拖拽了"+percent+"%");
        // 拖拽距离越大，固定圆半径越小(半径从100% -> 20%)
        return evaluate(percent,stickRadius,stickRadius * 0.2f);
    }
    /**根据拖拽的百分比返回固定圆的半径*/
    public Float evaluate(float fraction,Number startValue,Number endValue){
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 拖拽圆在拖拽时的中心点坐标
        float x = 0;
        float y = 0;

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isOutOfRange = false;
                isDisappear = false;
                x = event.getRawX();
                y = event.getRawY();
                setDragCenter(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getRawX();
                y = event.getRawY();
                setDragCenter(x,y);
                float distance = GeometryUtil.getDistanceBetween2Points(dragCenter,stickCenter);
                if(distance > maxDistance){
                    isOutOfRange = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isOutOfRange) {
                    float dis = GeometryUtil.getDistanceBetween2Points(dragCenter,stickCenter);
                    if(dis > maxDistance){
                        // a.拖拽超出范围，断开连接部分和不画固定圆，松手时如果两圆心距离大于maxDistance就全部消失(什么都不画)
                        isDisappear = true;
                        invalidate();
                    }else{
                        // b.如果又拖回到maxDistance的范围就恢复
                        setDragCenter(stickCenter.x,stickCenter.y);
                    }
                }else{
                    // 拖拽没有超出范围，松手弹回去
                    //记住up时拖拽圆的圆心坐标
                    final PointF upDargCenter = new PointF(dragCenter.x,dragCenter.y);
                    ValueAnimator anim = ValueAnimator.ofFloat(1.0f);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float fraction = valueAnimator.getAnimatedFraction();
                            PointF newDragCenterPoint = GeometryUtil.getPointByPercent(upDargCenter,stickCenter,fraction);
                            setDragCenter(newDragCenterPoint.x,newDragCenterPoint.y);
                        }
                    });
                    anim.setDuration(500);
                    anim.setInterpolator(new OvershootInterpolator(4));
                    anim.start();
                }
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        statusBarHeight = getStatusBarHeight(this);
    }

    /**更新拖拽圆的圆心点坐标，并重绘*/
    private void setDragCenter(float x, float y) {
        dragCenter.set(x,y);
        invalidate();
    }

    /**获取状态栏高度(屏幕上显示时间和网络信息的那一栏，最顶部)*/
    private int getStatusBarHeight(View view){
        if(view == null) return 0;
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }
}
