package com.test.mytest.testTopIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/23.
 */
public class TopIndicatorActivity extends FragmentActivity {

    // ViewPager用的Fragment的集
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    // 适配器
    private CustomPagerAdapter mAdapter;
    private ViewPager pager;
    private TopIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topnavigetionbar_activity);
        init();
    }

    private void init() {
        String[] strs = { "赛事", "热门", "我的" };
        for (String str : strs) {
            CustomFragment cf = new CustomFragment();
            Bundle bundle = new Bundle();
            bundle.putString(CustomFragment.TITLE, str);
            cf.setArguments(bundle);
            mFragments.add(cf);
        }
        indicator = (TopIndicator) findViewById(R.id.topindicator);
        pager = (ViewPager) findViewById(R.id.match_pager);
        mAdapter = new CustomPagerAdapter(this,getSupportFragmentManager(), mFragments,indicator);
        pager.setAdapter(mAdapter);
        indicator.init(strs,null);
    }
}
