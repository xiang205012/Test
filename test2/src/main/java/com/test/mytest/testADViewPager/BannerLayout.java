package com.test.mytest.testADViewPager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/12/7 0007
 */

public class BannerLayout extends RelativeLayout {

    private BannerViewPager viewPager;
    private RadioGroup indictorGroup;
    private int indictorCount;
    private int selectorIndictorColor = Color.parseColor("#FF8C00");;
    private int normalIndictorColor = Color.parseColor("#FFFFE0");
    /**指示器显示位置*/
    public static final int INDICTOR_ALIGN_LEFT = 0;
    public static final int INDICTOR_ALIGN_CENTER = 1;
    public static final int INDICTOR_ALIGN_RIGHT = 2;

    private List<ImageView> allImages = new ArrayList<>();

    private OnClickBannerItemListener mClickListener;

    public void setOnBannerLayoutListener(OnClickBannerItemListener listener){
        this.mClickListener = listener;
    }

    public interface OnClickBannerItemListener{
        /**轮播时点击item*/
        void onClickItem(int position);
    }

    private OnLastIndexBannerListener mLastIndexListener;

    public void setOnLastIndexBannerListener(OnLastIndexBannerListener listener){
        this.mLastIndexListener = listener;
    }

    public interface OnLastIndexBannerListener{
        /**可用于引导页，在最后一页时显示需要显示的*/
        void isLastItem();
    }

    /**设置指示器颜色*/
    public void setIndictorColor(int selectorColor,int normalColor){
        this.selectorIndictorColor = selectorColor;
        this.normalIndictorColor = normalColor;
    }

    /**设置指示器位置*/
    public void setIndictorAlignType(int type){
        LayoutParams layoutParams = (LayoutParams) indictorGroup.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, type == INDICTOR_ALIGN_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, type == INDICTOR_ALIGN_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, type == INDICTOR_ALIGN_CENTER ? RelativeLayout.TRUE : 0);
        indictorGroup.setLayoutParams(layoutParams);
    }

    /**
     * 是否可以轮播,默认是可以 isAuto = true
     * @param isAuto true:轮播 false:静态
     */
    public void setAutoScroll(boolean isAuto){
        viewPager.setAutoScrollAble(isAuto);
    }

    /**绑定生命周期*/
    public void setLifeCycle(int lifeCycle){
        viewPager.setLifeCycle(lifeCycle);
    }

    /**设置轮播间隔时间*/
    public void setintervalTime(int time){
        viewPager.setIntervalTime(time);
    }

    public BannerLayout(Context context) {
        this(context,null);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        viewPager = new BannerViewPager(context);
        RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(vpParams);
        viewPager.setBackgroundColor(Color.parseColor("#ee779798"));
        addView(viewPager);

        indictorGroup = new RadioGroup(context);
        RelativeLayout.LayoutParams rgParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rgParams.addRule(ALIGN_PARENT_BOTTOM,TRUE);
        rgParams.addRule(CENTER_HORIZONTAL,TRUE);
        indictorGroup.setLayoutParams(rgParams);
        indictorGroup.setGravity(Gravity.CENTER);
        indictorGroup.setOrientation(LinearLayout.HORIZONTAL);
        addView(indictorGroup);

        try {
            Field mScrollerField = ViewPager.class.getDeclaredField("mScroller");
            mScrollerField.setAccessible(true);
            mScrollerField.set(viewPager,new ViewPagerScroller(viewPager.getContext(),new LinearInterpolator()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setWitchIndex(position % indictorCount);
                if(position % indictorCount == (indictorCount-1) && mLastIndexListener != null){
                    mLastIndexListener.isLastItem();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // TODO 需要按实际情况和图片框架修改
    /**设置图片来源 如果是res下的图片可以放到assets下然后通过图片框架加载*/
    public void setImgList(List<Integer> imgPath){
        if(imgPath != null && imgPath.size() > 0){
            createIndictor(imgPath.size());
            ADAdapter adAdapter = new ADAdapter(imgPath);
            viewPager.setAdapter(adAdapter);
            viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        }
    }

    private void createIndictor(int size) {
        indictorCount = 0;
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setPadding(5,5,5,5);
            Drawable bgDrawable = createSelectorDrawable(selectorIndictorColor,normalIndictorColor);
            if (bgDrawable != null) {
                radioButton.setButtonDrawable(bgDrawable);
                indictorGroup.addView(radioButton);
                indictorCount++;
            }
        }
        setWitchIndex(0);
    }

    private void setWitchIndex(int i) {
        for (int index = 0; index < indictorCount; index++) {
            if (i == index) {
                indictorGroup.getChildAt(index).setSelected(true);
            }else{
                indictorGroup.getChildAt(index).setSelected(false);
            }
        }
    }

    private Drawable createSelectorDrawable(int selectorIndictorColor, int normalIndictorColor) {
        Drawable selectorDrawable = createShapeDrawable(selectorIndictorColor);
        Drawable normalDrawable = createShapeDrawable(normalIndictorColor);
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_checkable},selectorDrawable);
        bg.addState(new int[]{android.R.attr.state_selected},selectorDrawable);
        bg.addState(new int[]{},normalDrawable);
        return bg;
    }

    private GradientDrawable createShapeDrawable(int color){
        int roundRadius = px2dp(8);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setGradientRadius(roundRadius);
        gd.setShape(GradientDrawable.OVAL);
        gd.setSize(roundRadius,roundRadius);
        return gd;
    }

    private class ADAdapter extends PagerAdapter{

        private List<Integer> imgList;

        public ADAdapter(List<Integer> list){
            this.imgList = list;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = null;
            if(allImages.size() > 0){
                imageView = allImages.remove(0);
            }else{
                imageView = new ImageView(getContext());
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(imgList.get(position % imgList.size()));
            container.addView(imageView);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickListener != null){
                        mClickListener.onClickItem(position % imgList.size());
                    }
//                    Log.d("tag"," imageView onClickListener position : " + (position % imgList.size()) );
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView)object);
            allImages.add((ImageView)object);
        }

    }

    // 轮播速度
    public class ViewPagerScroller extends Scroller {

        private static final int M_SCROLL_DURATION = 800;

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy,M_SCROLL_DURATION);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, M_SCROLL_DURATION);
        }
    }

    private int px2dp(int px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,px,getResources().getDisplayMetrics());
    }

}
