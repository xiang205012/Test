package com.xiang.testapp.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiang.testapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/10/21.
 */

public class ViewPagerActivity extends Activity {

    private int[] imgs = new int[]{R.drawable.guide_image1,R.drawable.guide_image2,R.drawable.guide_image3};

    private List<ImageView> viewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPager viewPager = new ViewPager(this);
        setContentView(viewPager);

        viewPager.setPageTransformer(false,new ScaleAlphaTransformer());
        viewPager.setAdapter(new VAdapter());




    }

    private class VAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = null;
            imageView = new ImageView(container.getContext());
            viewList.add(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgs[position]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }


}
