package com.test.mytest.testTopNavigationBar;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/23.
 */
public class TopLNAVBar extends HorizontalScrollView {

    /**导航条文字*/
    private String[] name;
    /**记录所有文字对应的textView*/
    private List<TextView> textViewList = new ArrayList<TextView>();
    /**导航线*/
    private View viewLine;
    /**包裹所有textView和viewLine*/
    private LinearLayout linearLayout;
    private static LinearLayout.LayoutParams linearParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    /**线条的宽度*/
    private int lineWidth;

    private int width;
    private int height;

    private ViewPager viewPager;

    /**设置导航栏文字*/
    public void setTitle(String[] titles){
        this.name = titles;
    }

    public TopLNAVBar(Context context) {
        this(context, null);
    }

    public TopLNAVBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopLNAVBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(Context context, final ViewPager viewPager) {
        linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(linearParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        //包裹文字的linearLayout的LayoutParams
        LinearLayout.LayoutParams conParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        conParams.gravity = Gravity.CENTER;
        //textview的LayoutParams
        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        conParams.weight = 1.0f;
        textParams.gravity = Gravity.CENTER;
        //viewLine的LayoutParams
        LinearLayout.LayoutParams lineParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;

        viewLine = new View(context);
        lineParams.weight = width;
        lineParams.height = getLineHeight();
        viewLine.setLayoutParams(lineParams);

        for(int i = 0;i < name.length;i++){
            final int index = i;
            LinearLayout conLayout = new LinearLayout(context);
            conLayout.setOrientation(LinearLayout.HORIZONTAL);
            conLayout.setLayoutParams(conParams);
            TextView textView = new TextView(context);
            textView.setLayoutParams(textParams);
            textView.setText(name[i]);
            textView.setTextColor(getStartColor());
            conLayout.addView(textView);
            linearLayout.addView(conLayout, conParams);
            textViewList.add(textView);
            conLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(index);
                }
            });
        }
        linearLayout.addView(viewLine);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
//    private void getDistance(int position, float scale) {
//        int x = 0;
//        if (position > 1) {
//            for (int i = 0; i < position - 1; i++) {
//                x += textWidth.get(i);
//            }
//        }
//        mScrollView.smoothScrollTo((int) (textWidth.get(position - 1) * scale)
//                + x, 0);
//    }
    private int startColor = Color.BLACK;//初始颜色
    private int endColor = Color.RED;//选中后颜色
    public void setStartColor(int color){
        this.startColor = color;
    }
    public int getStartColor(){
        return startColor;
    }
    public void setEndColor(int color){
        this.endColor = color;
    }
    public int getEndColor(){
        return endColor;
    }


    /**设置导航线的高度*/
    public void setLineHeight(int height) {
        this.height = height;
    }

    public int getLineHeight(){
        return height;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = getMeasuredWidth() / name.length;
        height = getMeasuredHeight();

    }
}
