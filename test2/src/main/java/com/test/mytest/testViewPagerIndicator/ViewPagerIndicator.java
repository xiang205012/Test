package com.test.mytest.testViewPagerIndicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.mytest.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ViewPagerIndicator extends LinearLayout {

    /**导航底部三角形的绘制*/
    private Paint mPaint;
    //绘制三角形
    private Path mPath;
    // 三角形固定宽度是一个tab的1/6
    private static final float RADIO_TAIANGLE_WIDTH = 1/6F;
    // 三角形底部宽度
    private int mTriangleWidth;
    // 三角形高度
    private int mTriangleHeight;
    // 三角形X方向初始偏移量
    private int mInitTranslationX;
    // 三角形跟随viewpager移动时的偏移量
    private int mTranslationX;
    // 可见tab数量(从自定义属性中获取,其余的需要滑动)
    private int tabCount;
    // 默认可见tab数量
    private static final int visibleTabCount = 4;

    private static final int NORMAL_COLOR = 0x77FFFFFF;
    private static final int SELECTOR_COLOR = 0xFFFFFFFF;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        tabCount = array.getInteger(R.styleable.ViewPagerIndicator_tabCount,visibleTabCount);
        if(tabCount <= 0){
            tabCount = visibleTabCount;
        }
        array.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(SELECTOR_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
        // 设置画笔在画折线时不至于太尖锐，有一定的圆角
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTriangleWidth = (int) (w / tabCount * RADIO_TAIANGLE_WIDTH);
        mInitTranslationX = w / tabCount / 2 - mTriangleWidth / 2;
        initTrangle();
    }

    /**初始化三角形*/
    private void initTrangle() {
        mTriangleHeight = mTriangleWidth / 2;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // 将画布平移到正确的位置
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * viewpager关联tab滑动
     * @param position viewPager第几页(它是从0开始的)
     * @param positionOffset viewPager滑动时的偏移比例
     */
    public void scroll(int position, float positionOffset) {
        // 三角形的移动
        int tabWidth = getWidth() / tabCount;
        mTranslationX = (int) (tabWidth * positionOffset + tabWidth * position);

        // 当滑动到后面的时，整个自定义view滑动
        if(position >= (tabCount - 2) && positionOffset > 0 && getChildCount() > tabCount){

            if(tabCount != 1) {
                // position >= (tabCount - 2) 此时position >= 2，也就是viewpager至少滑动到了第三页
                // positionOffset > 0 && getChildCount() > tabCount 必须是滑动的而且tab总数量大于可见数量
                this.scrollTo((position - (tabCount - 2)) * tabWidth + (int) (tabWidth * positionOffset), 0);
            }else{
                this.scrollTo(position * tabWidth + (int)(tabWidth * positionOffset),0);
            }

        }

        invalidate();

    }

    /**
     * 当XML布局文件加载完毕后调用，此处用于排列显示前4个tab
     * 如果是动态添加此方法就不会调用，因为布局文件中没有添加子view
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // 得到所有的子view
        int childCount = getChildCount();
        if(childCount == 0)return;
        for(int i = 0;i < childCount;i++){
            View view = getChildAt(i);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.weight = 0;
            params.width = getScreenWidth() / tabCount;
            view.setLayoutParams(params);
        }
        changeSelectorTvColor(0);
        setOnItemClickListener();
    }

    /**获取屏幕的宽度*/
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    private List<String> mTitles;

    /**
     * 根据tab的title动态创建tab
     * @param titles
     */
    public void setIndicatorItems(List<String> titles){
        if(titles != null && titles.size() > 0) {
            this.mTitles = titles;
            removeAllViews();
            for (String title : mTitles) {
                addView(createItems(title));
            }
            changeSelectorTvColor(0);
            setOnItemClickListener();
        }
    }

    private View createItems(String title) {
        TextView tv = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        params.width = getScreenWidth()/tabCount;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tv.setTextColor(NORMAL_COLOR);
        tv.setText(title);
        return tv;
    }

    /**动态设置tab的可见数量，一定要在setIndicatorItems()之前调用*/
    public void setVisibleTabCount(int count){
        tabCount = count;
    }

    private ViewPager mViewPager;
    public void setViewPager(ViewPager viewPager,int pos){
        this.mViewPager = viewPager;
        /**viewPager关联tab标签*/
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 三角形移动的计算方式：tabWidth * positionOffset + tabWidth * position
                scroll(position,positionOffset);

            }

            @Override
            public void onPageSelected(int position) {
                changeSelectorTvColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(pos);

    }

    /**
     * 改变选中tab的文本颜色
     * @param position
     */
    public void changeSelectorTvColor(int position){
        int count = getChildCount();
        for(int i = 0;i < count;i++){
            View view = getChildAt(i);
            if(i == position){
                if(view instanceof TextView){
                    ((TextView) view).setTextColor(SELECTOR_COLOR);
                }
            }else{
                if(view instanceof TextView){
                    ((TextView) view).setTextColor(NORMAL_COLOR);
                }
            }
        }

    }

    /**设置点击事件*/
    public void setOnItemClickListener(){
        int count = getChildCount();
        for(int i = 0;i < count ;i++){
            View view = getChildAt(i);
            final int j = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeSelectorTvColor(j);
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }


}
