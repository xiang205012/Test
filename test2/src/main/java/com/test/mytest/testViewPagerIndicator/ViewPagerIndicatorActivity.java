package com.test.mytest.testViewPagerIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ViewPagerIndicatorActivity extends FragmentActivity {

    private ViewPager viewPager;
    private ViewPagerIndicator indicator;
//    private String[] titles = new String[]{"主页1","短信2","电话3",
//            "主页4","短信5","电话6",
//            "主页7","短信8","电话9"};
    private List<String> titles = Arrays.asList("主页1","短信2","电话3",
            "主页4","短信5","电话6",
            "主页7","短信8","电话9");
    private List<ViewPagerFragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_indicator);

        viewPager = (ViewPager) findViewById(R.id.customviewpager);
        indicator = (ViewPagerIndicator) findViewById(R.id.test_viewPagerIndicator);

        indicator.setVisibleTabCount(3);
        indicator.setIndicatorItems(titles);

        for(String title : titles){
            ViewPagerFragment fragment = ViewPagerFragment.newInstance(title);
            fragmentList.add(fragment);
        }

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        indicator.setViewPager(viewPager,0);

//        /**viewPager关联tab标签*/
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                // 三角形移动的计算方式：tabWidth * positionOffset + tabWidth * position
//                indicator.scroll(position,positionOffset);
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


}
