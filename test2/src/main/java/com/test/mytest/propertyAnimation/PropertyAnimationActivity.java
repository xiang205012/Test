package com.test.mytest.propertyAnimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public class PropertyAnimationActivity extends Activity implements View.OnClickListener {

    private ImageView iv_property;
    private Button btn_start;
    private int[] ids = {R.id.iv_property0,R.id.iv_property1,
            R.id.iv_property2,R.id.iv_property3,R.id.iv_property4,
            R.id.iv_property5,R.id.iv_property6, R.id.iv_property7};

    private List<ImageView> ivList = new ArrayList<>();

    /**是否为展开状态*/
    private boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);

        iv_property = (ImageView) findViewById(R.id.iv_property);
        btn_start = (Button) findViewById(R.id.btn_property_start);
        btn_start.setOnClickListener(this);

        for(int i = 0;i < ids.length;i++){
            ImageView imageView = (ImageView) findViewById(ids[i]);
            imageView.setOnClickListener(this);
            ivList.add(imageView);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_property_start:
                testAnimator();
                break;
            case R.id.iv_property0:
                //展开，收缩动画
                if(!isOpen){
                    open();
                }else{
                    close();
                }
                isOpen = !isOpen;
                break;
            case R.id.iv_property1:
            case R.id.iv_property2:
            case R.id.iv_property3:
            case R.id.iv_property4:
            case R.id.iv_property5:
            case R.id.iv_property6:
            case R.id.iv_property7:
                Toast.makeText(PropertyAnimationActivity.this,"点击了展开菜单",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void open() {
        for(int i = 0;i < ids.length;i++){
            ObjectAnimator animator = ObjectAnimator.ofFloat(ivList.get(i),"translationY",0,i * 200f);
            animator.setDuration(300);
            animator.setInterpolator(new BounceInterpolator());
            animator.setStartDelay(i * 500);//延迟执行
            animator.start();
        }
    }

    private void close() {
        for(int i = 0;i < ids.length;i++){
            ObjectAnimator animator = ObjectAnimator.ofFloat(ivList.get(i),"translationY",i * 200f,0);
            animator.setDuration(300);
            animator.setInterpolator(new BounceInterpolator());
            animator.setStartDelay(i * 500);
            animator.start();
        }
    }

    private void testAnimator() {
//        TranslateAnimation ta = new TranslateAnimation(0,250,0,250);
//        ta.setDuration(500);
//        ta.setFillAfter(true);
//        iv_property.startAnimation(ta);
        ObjectAnimator.ofFloat(iv_property,"rotation",0,360f).setDuration(500).start();
        ObjectAnimator.ofFloat(iv_property,"translationX",0,250f).setDuration(500).start();

        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotation",0,200f);
        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("tanslationX",0,200f);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("tanslationY",0,20f);
        ObjectAnimator.ofPropertyValuesHolder(iv_property,p1,p2,p3).setDuration(500).start();;

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv_property,"rotation",0,270f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv_property,"tanslationX",0,250f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(iv_property,"tanslationY",0,300f);
        AnimatorSet set = new AnimatorSet();
        //动画一起执行：set.playTogether(animator1,animator2,animator3);
        //按顺序执行：set.playSequentially(animator1,animator2,animator3);
        //精确控制执行顺序
        set.play(animator2).with(animator3);
        set.play(animator1).after(animator3);
        set.setDuration(500);
        set.start();
    }

    /**属性动画监听*/
    public void testAnimatorListener(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_property,"rotation",0,250f);
        //监听有两种方式：
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                Toast.makeText(PropertyAnimationActivity.this,"属性动画结束了",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Toast.makeText(PropertyAnimationActivity.this,"属性动画结束了",Toast.LENGTH_SHORT).show();
            }
        });
        animator.start();

    }



}
