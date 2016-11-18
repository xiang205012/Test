package com.test.mytest.testPopWindow;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.mytest.R;
import com.test.mytest.uitls.AnimationUtils;

/**
 * Created by Administrator on 2015/11/8.
 */
public class PopWindowActivity extends Activity implements View.OnClickListener {

    /**
     * 当时写这个的思路：
     *  1.先把导航条颜色的变化写出来，
     *      需求：①点击变蓝再点击变黑，
     *           ②第一个为蓝色点击第二个时，第一个变黑第二个变蓝
     *
     *  2.显示和关闭pop框，这里就需要根据导航的颜色即isBlue来判断，isBlue = false时显示，反之关闭
     *      当用isBlue来判断后发现还不够，接着还需要用两个index来判断（如果点击的不是同一个先把isBlue置为false）
     *
     */


    RelativeLayout fl_popwindow;

    LinearLayout ll_sortType;
    TextView tv_navigation_house_type;
    TextView tv_navigation_price;
    TextView tv_navigation_school_district;
    TextView tv_navigation_area;
    RelativeLayout bt_dismiss;

    private View view;

    private TextView[] tvs = new TextView[4];
    private int lastClickIndex = -1;//记录最后一个
    private int curretClickIndex = -1;//记录当前点击的一个
    private boolean isBlue;//记录当前是否是蓝色，true时字体蓝色并显示pop框，false时黑色并关闭pop框

    private ValueAnimator openAnimation;
    private ValueAnimator closeAnimation;

    private String TAG = "POP--->>";

    private ImageView imageView;

    private FrameLayout fl_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_bar);

        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PopWindowActivity.this,"点击了...",Toast.LENGTH_SHORT).show();
            }
        });

        fl_content = (FrameLayout) findViewById(R.id.fl_content);

        fl_popwindow = (RelativeLayout) findViewById(R.id.fl_popwindow);

        ll_sortType = (LinearLayout) findViewById(R.id.ll_sortType);
        tv_navigation_area = (TextView) findViewById(R.id.tv_navigation_area);
        tv_navigation_house_type = (TextView) findViewById(R.id.tv_navigation_house_type);
        tv_navigation_price = (TextView) findViewById(R.id.tv_navigation_price);
        tv_navigation_school_district = (TextView) findViewById(R.id.tv_navigation_school_district);

        bt_dismiss = (RelativeLayout) findViewById(R.id.bt_dismiss);

        tvs[0] = tv_navigation_area;
        tvs[1] = tv_navigation_house_type;
        tvs[2] = tv_navigation_price;
        tvs[3] = tv_navigation_school_district;

        tv_navigation_area.setOnClickListener(this);
        tv_navigation_house_type.setOnClickListener(this);
        tv_navigation_price.setOnClickListener(this);
        tv_navigation_school_district.setOnClickListener(this);
        bt_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_popwindow.removeAllViews();
                animationUtils.setPropertyTranstAnimation(fl_popwindow, 500, true);
                fl_content.setVisibility(View.GONE);
                resetAllViewsState();
            }
        });

    }

    /**当pop消失是还原所有view的初始状态*/
    private void resetAllViewsState() {
        isBlue = false;
        curretClickIndex = -1;
        lastClickIndex = -1;
        for(TextView tv : tvs){
            tv.setTextColor(Color.BLACK);
        }
    }


    @Override
    public void onClick(View v) {
        view = showAreaPop();
        switch(v.getId()) {
            case R.id.tv_navigation_area:
                curretClickIndex = 0;
                //可以在此处初始化不同的view。  view = createHousePop();
                showOrClose(curretClickIndex,view,300);
                break;
            case R.id.tv_navigation_house_type:
                curretClickIndex = 1;
                showOrClose(curretClickIndex,view,200);
                break;
            case R.id.tv_navigation_price:
                curretClickIndex = 2;
                showOrClose(curretClickIndex, view, 100);
                break;
            case R.id.tv_navigation_school_district:
                curretClickIndex = 3;
                showOrClose(curretClickIndex, view, 80);
                break;
        }
        changeNaviBarColor(curretClickIndex,lastClickIndex);
        lastClickIndex = curretClickIndex;//一定要写在changeNavi...方法后面
    }

    private AnimationUtils animationUtils;
    /**
     * 弹框的显示和关闭
     * @param curretClickIndex 当前点击的位置
     * @param view 需要显示的view
     * @param height 显示的高度
     */
    private void showOrClose(int curretClickIndex, View view, int height) {
        animationUtils = AnimationUtils.getAnimationUtils();
        if (lastClickIndex != curretClickIndex){
            //防止弹框不显示。当点击第一个时，isBlue=true，此时第一个弹框出来(不要让它消失)接着点第二个
            // 但点击第二个时，第二个的弹框不显示但颜色已改为blue
            isBlue = false;
        }
        if (isBlue) {
            fl_popwindow.removeAllViews();
//            openAnimation = ObjectAnimator.ofInt(fl_popwindow,"translationY",height,0);
            bt_dismiss.setVisibility(View.GONE);
            animationUtils.setPropertyTranstAnimation(fl_popwindow,500,true);
        }else {
            fl_content.setVisibility(View.VISIBLE);
            fl_popwindow.removeAllViews();//必须先移除之前的view，否则显示上会有异常
            // 在实际开发中应该自定义RelativeLayout,并重写onSizeChanged()方法来获取rl的真实高度
            fl_popwindow.addView(view, -1, height);
            // 如果需要弹出框的其他部分透明，则需要在xml中设置bt_dismiss的background颜色，
            // 然后在此处bt_dismiss.setAlpha(0.3f)
            animationUtils.setPropertyTranstAnimation(fl_popwindow,500,false);
//            openAnimation = ObjectAnimator.ofInt(fl_popwindow,"translationY",0, height);
        }
//        openAnimation.setInterpolator(new LinearInterpolator());
//        openAnimation.setDuration(300);
//        openAnimation.setRepeatCount(-1);//设置动画重复次数
//        openAnimation.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
//        openAnimation.setStartDelay(1000);//动画延时执行
//        openAnimation.start();
    }
//    ObjectAnimator animator = ObjectAnimator.ofFloat(textview, "rotation", 0f, 360f);
//    注：rotation(旋转)，translationX(X方向移动)，scale(缩放，scaleX:X方向缩放)，alpha(透明度)。也可以自定义属性
//    animator.setDuration(5000);
//    animator.start();

//    ValueAnimator animator = ValueAnimator.ofFloat(0,100);
//    animator.setTarget(view);
//    animator.setDuration(500);
//    animator.start();

//    PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat('translationX', 0.0f, 300.0f);
//    PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat('scaleX', 1.0f, 1.5f);
//    PropertyValuesHolder valuesHolder2 = PropertyValuesHolder.ofFloat('rotationX', 0.0f, 90.0f, 0.0F);
//    PropertyValuesHolder valuesHolder3 = PropertyValuesHolder.ofFloat('alpha', 1.0f, 0.3f, 1.0F);
//    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(imageView, valuesHolder, valuesHolder1, valuesHolder2, valuesHolder3);
//    objectAnimator.setDuration(2000).start();
    //精简方式：
//    imageView.animate().translationX(200).scaleX(2).setDuration(2000).start();

    /**
     * 改变导航条的颜色
     * @param curretClickIndex 当前点击的位置
     * @param lastClickIndex 上一次点击的位置
     */
    private void changeNaviBarColor(int curretClickIndex,int lastClickIndex) {
        if(curretClickIndex == lastClickIndex){
            //如果点击的是同一个，就判断isBlue
            if(isBlue){
                tvs[curretClickIndex].setTextColor(Color.BLACK);
            }else{
                tvs[curretClickIndex].setTextColor(Color.BLUE);
            }
            isBlue = !isBlue;
        }else{
            //不是同一个，注意isBlue的值肯定是false
            isBlue = true;
            tvs[curretClickIndex].setTextColor(Color.BLUE);
            if(lastClickIndex != -1) {//防止第一次点击时数组越界
                tvs[lastClickIndex].setTextColor(Color.BLACK);
            }
        }
    }

    /**
     * pop框
     * @return
     */
    private View showAreaPop() {
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                , 200));
        layout.setBackgroundColor(Color.GRAY);
        TextView textView = new TextView(this);
        textView.setText("弹出框");
        textView.setPadding(10,10,10,10);
        textView.setTextColor(Color.WHITE);
        layout.addView(textView);
        return layout;
    }

    public void on(){
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
    }

    public void off(){
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
    }


}
