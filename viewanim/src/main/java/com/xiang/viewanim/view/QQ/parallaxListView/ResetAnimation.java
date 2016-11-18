package com.xiang.viewanim.view.QQ.parallaxListView;

import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/2/16.
 */
public class ResetAnimation extends Animation{

    private ImageView imageView;
    private int startHeight;
    private int endHeight;

    public ResetAnimation(ImageView imageView,int startHeight,int endHeight){
        this.imageView = imageView;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        setDuration(300);
        setInterpolator(new OvershootInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Integer newHeight = evaluate(interpolatedTime,startHeight,endHeight);
        imageView.getLayoutParams().height = newHeight;
        imageView.requestLayout();
        super.applyTransformation(interpolatedTime, t);
    }

    private Integer evaluate(float interpolatedTime, int startHeight, int endHeight) {
        return (int)(startHeight + interpolatedTime * (endHeight - startHeight));
    }
}
