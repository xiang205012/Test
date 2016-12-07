package com.xiang.viewanim.anim.ballBeatIndicator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.icu.text.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class BallBeatInticatorView extends View {

    public static final float SCALE = 1.0f;
    public static final int ALPHA = 255;
    private float[] scaleFloats = new float[]{SCALE,SCALE,SCALE};
    private int[] alphas = new int[]{ALPHA,ALPHA,ALPHA};

    private Paint paint;

    float[] translateX=new float[3],translateY=new float[3];

    public BallBeatInticatorView(Context context) {
        this(context,null);
    }

    public BallBeatInticatorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BallBeatInticatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 1...........
//        float circleSpaceing = 4;
//        float radius = (getWidth() - circleSpaceing * 2) / 6;
//        float x = getWidth() / 2 - (radius * 2 + circleSpaceing);
//        float y =getHeight() / 2;
//        for (int i = 0; i < 3; i++) {
//            canvas.save();
//            float translateX = x + (radius * 2) * i + circleSpaceing * i;
//            canvas.translate(translateX,y);
//            canvas.scale(scaleFloats[i],scaleFloats[i]);
//            paint.setAlpha(alphas[i]);
//            canvas.drawCircle(0,0,radius,paint);
//            canvas.restore();
//        }

    // 2........
//        RectF rectF=new RectF(0,0,getWidth(),getHeight());
//        canvas.drawArc(rectF,-60,120,false,paint);



     // 3......
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 3; i++) {
            canvas.save();
            canvas.translate(translateX[i], translateY[i]);
            canvas.drawCircle(0, 0, getWidth() / 10, paint);
            canvas.restore();
        }

    }

    public void startAnima(){
        // 1.........
//        AnimatorSet animSet = new AnimatorSet();
//        int[] delays=new int[]{350,0,350};
//        for (int i = 0; i < 3; i++) {
//            final int index=i;
//            ValueAnimator scaleAnim=ValueAnimator.ofFloat(1,0.75f,1);
//            scaleAnim.setDuration(700);
//            scaleAnim.setRepeatCount(-1);//repeatCount为重复执行的次数。如果设置为n，则动画将执行n+1次,-1表示无限循环
//            scaleAnim.setStartDelay(delays[i]);//延迟动画执行 调用setStartDelay(1000)意味着动画在执行了start()方法1秒之后才真正运行
//            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    scaleFloats[index] = (float) animation.getAnimatedValue();
//                    postInvalidate();
//                }
//            });
//
//            ValueAnimator alphaAnim=ValueAnimator.ofInt(255,51,255);
//            alphaAnim.setDuration(700);
//            alphaAnim.setRepeatCount(-1);
//            alphaAnim.setStartDelay(delays[i]);
//            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    alphas[index] = (int) animation.getAnimatedValue();
//                    postInvalidate();
//                }
//            });
//            animSet.play(scaleAnim).with(alphaAnim);
//            animSet.start();
//        }

     // 2.......
//        ObjectAnimator rotateAnim=ObjectAnimator.ofFloat(this,"rotation",0,180,360);
//        rotateAnim.setDuration(600);
//        rotateAnim.setRepeatCount(-1);
//        rotateAnim.start();


     // 3...........
        AnimatorSet animSet = new AnimatorSet();
        float startX=getWidth()/5;
        float startY=getWidth()/5;
        for (int i = 0; i < 3; i++) {
            final int index = i;
            ValueAnimator translateXAnim = ValueAnimator.ofFloat(getWidth() / 2,
                    getWidth() - startX, startX, getWidth() / 2);
            if (i == 1) {
                translateXAnim = ValueAnimator.ofFloat(getWidth() - startX,
                        startX, getWidth() / 2, getWidth() - startX);
            } else if (i == 2) {
                translateXAnim = ValueAnimator.ofFloat(startX,
                        getWidth() / 2, getWidth() - startX, startX);
            }
            ValueAnimator translateYAnim = ValueAnimator.ofFloat(startY, getHeight() - startY,
                    getHeight() - startY, startY);
            if (i == 1) {
                translateYAnim = ValueAnimator.ofFloat(getHeight() - startY,
                        getHeight() - startY, startY, getHeight() - startY);
            } else if (i == 2) {
                translateYAnim = ValueAnimator.ofFloat(getHeight() - startY,
                        startY, getHeight() - startY, getHeight() - startY);
            }

            translateXAnim.setDuration(2000);
            translateXAnim.setInterpolator(new LinearInterpolator());
            translateXAnim.setRepeatCount(-1);
            translateXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateX[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateYAnim.setDuration(2000);
            translateYAnim.setInterpolator(new LinearInterpolator());
            translateYAnim.setRepeatCount(-1);
            translateYAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateY[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animSet.play(translateXAnim).with(translateYAnim);
            animSet.start();
        }
    }


}
