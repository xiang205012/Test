package com.xiang.viewanim.view.QQ.goo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 粘性控件
 * Created by Administrator on 2016/2/17.
 */
public class GooView1 extends View {

    private Paint mPaint;

    public GooView1(Context context) {
        this(context, null);
    }

    public GooView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GooView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    PointF dragCenter = new PointF(600f,600f);
    float dragRadius = 40f;
    PointF stickCenter = new PointF(700f,700f);
    float stickRadius = 30f;
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


        // 曲线控制点
        PointF controlPoint = new PointF(150f,300f);
        mPath.moveTo(mStickPoints[0].x,mStickPoints[0].y);
        mPath.quadTo(controlPoint.x, controlPoint.y, mDragPoints[0].x, mDragPoints[0].y);// 绘制右上和左上点的连接曲线
        mPath.lineTo(mDragPoints[1].x, mDragPoints[1].y);//绘制左上和左下点的连接直线
        mPath.quadTo(controlPoint.x, controlPoint.y, mStickPoints[1].x, mStickPoints[1].y);//绘制左下和右下点的连接曲线
        mPath.close();// 将绘制的线封闭起来
        canvas.drawPath(mPath, mPaint);// 画出图形


        // 画移动圆
        //canvas.drawCircle(300f,300f,20f,mPaint);
        canvas.drawCircle(dragCenter.x,dragCenter.y,dragRadius,mPaint);


        // 画固定圆
        //canvas.drawCircle(400f,400f,14f,mPaint);
        canvas.drawCircle(stickCenter.x,stickCenter.y,stickRadius,mPaint);

    }
}
