package com.xiang.viewanim.view.QQ.goo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xiang.viewanim.util.GeometryUtil;

/**
 * 粘性控件
 * Created by Administrator on 2016/2/17.
 */
public class GooView2 extends View {

    private Paint mPaint;

    public GooView2(Context context) {
        this(context, null);
    }

    public GooView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GooView2(Context context, AttributeSet attrs, int defStyleAttr) {
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
            // 1.获取直线与圆的交点，详细看图
            float xOffset = stickCenter.x - dragCenter.x;
            float yOffset = stickCenter.y - dragCenter.y;
            Double lineK = null;
            if(xOffset != 0){// 获取斜率，三角函数，邻边比对边
                lineK = (double)(yOffset / xOffset);
            }
            // 通过集合图形工具获取交点坐标
            mDragPoints = GeometryUtil.getIntersectionPoints(dragCenter,dragRadius,lineK);
            mStickPoints = GeometryUtil.getIntersectionPoints(stickCenter, stickRadius, lineK);

            // 2.获取控制点坐标
            controlPoint = GeometryUtil.getMiddlePoint(dragCenter,stickCenter);



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
        canvas.drawCircle(mStickPoints[0].x,mStickPoints[0].y,8.0f,mPaint);
        canvas.drawCircle(mStickPoints[1].x, mStickPoints[1].y, 8.0f, mPaint);
        mPaint.setColor(Color.RED);

        // 画移动圆
        //canvas.drawCircle(300f,300f,20f,mPaint);
        canvas.drawCircle(dragCenter.x,dragCenter.y,dragRadius,mPaint);


        // 画固定圆
        //canvas.drawCircle(400f,400f,14f,mPaint);
        canvas.drawCircle(stickCenter.x,stickCenter.y,stickRadius,mPaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 拖拽圆在拖拽时的中心点坐标
        float x = 0;
        float y = 0;

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = event.getRawX();
                y = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getRawX();
                y = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                x = 450f;
                y = 450f;
                break;
        }
        setDragCenter(x,y);
        return true;
    }

    /**更新拖拽圆的圆心点坐标，并重绘*/
    private void setDragCenter(float x, float y) {
        dragCenter.set(x,y);
        invalidate();
    }
}
