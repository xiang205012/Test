package com.test.mytest.testViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/10.
 */
public class ViewPagerActivity extends Activity {

//    @InjectView(R.id.viewpager)
//    ViewPager mViewpager;
    @InjectView(R.id.anim_viewpager)
    ViewPagerAnim mViewpager;
    /**图片数组*/
    private int[] imgIds = new int[]{R.drawable.guide_image1,R.drawable.guide_image2,R.drawable.guide_image3};
    /**ImageView集合*/
    private List<ImageView> imgViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        ButterKnife.inject(this);

        //设置动画
        //mViewpager.setPageTransformer(true,new DepthPageTransformer());
        //mViewpager.setPageTransformer(true,new ZoomOutPageTransformer());
        //mViewpager.setPageTransformer(true,new RotateDownPageTransformer());
        mViewpager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(ViewPagerActivity.this);
                imageView.setImageResource(imgIds[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.addView(imageView);
                imgViews.add(imageView);
                //mViewpager.addViews(position,imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imgViews.get(position));
                //mViewpager.removeViews(position);
            }

            @Override
            public int getCount() {
                return imgIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });


    }
}
