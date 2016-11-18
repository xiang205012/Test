package com.xiang.weixin60.zoomImageView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiang.weixin60.R;

/**
 * Created by Administrator on 2016/4/30.
 */
public class ZoomImageViewActivity extends Activity {

    private ViewPager viewPager;
    private int[] ids = new int[]{R.drawable.testzoomimage1,R.drawable.testzoomimage2,R.drawable.testzoomimage3};
    private ImageView[] imageViews = new ImageView[ids.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomimageview);

        viewPager = (ViewPager) findViewById(R.id.zoomImageView_viewPager);
        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView imageView = new ZoomImageView(ZoomImageViewActivity.this);
                imageView.setImageResource(ids[position]);
                container.addView(imageView);
                imageViews[position] = imageView;
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews[position]);
            }

            @Override
            public int getCount() {
                return imageViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

    }
}
