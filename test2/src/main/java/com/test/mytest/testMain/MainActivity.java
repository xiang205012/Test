package com.test.mytest.testMain;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.List;

import cj.library.utils.LogUtils;

//import android.support.v4.app.FragmentTransaction;


public class MainActivity extends FragmentActivity implements MyTabWidget.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    //如果不需要滑动来切换，就不需要使用viewpager这个控件，直接在onTabSelected()方法中切换不同Fragment即可。如文件最后一样的写法
    private ViewPager viewpager;
    private MyTabWidget tabWidget;
    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabWidget = (MyTabWidget) findViewById(R.id.my_tab_widget);

        list = new ArrayList<Fragment>();
        list.add(new AFragment());
        list.add(new BFragment());
        list.add(new CFragment());

        tabWidget.setOnTabSelectedListener(this);

        viewpager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewpager.setOnPageChangeListener(this);
    }


    @Override
    public void onTabSelected(int index) {
        switch (index){
            case 0:
                viewpager.setCurrentItem(0);
                LogUtils.d("viewpager切换了1");
                break;
            case 1:
                viewpager.setCurrentItem(1);
                LogUtils.d("viewpager切换了2");
                break;
            case 2:
                viewpager.setCurrentItem(2);
                LogUtils.d("viewpager切换了3");
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabWidget.setTabsDisplay(this,position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}

   /* @Override
    public void onTabSelected(int index) {
        transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case ConstantsValue.HOME_FRAGMENT_INDEX:
                if (null == mHomeFragment5) {

                    mHomeFragment5 = new HomeFragment5();
                    transaction.add(R.id.center_layout, mHomeFragment5, "TAG"
                            + index);
                } else {
                    transaction.show(mHomeFragment5);
                }
                break;
            case ConstantsValue.ProblemList_FRAGMENT_INDEX:
                if (null == mProblemListFragment) {
                    mProblemListFragment = new ProblemListFragment();
                    transaction.add(R.id.center_layout, mProblemListFragment, "TAG"
                            + index);
                } else {
                    mProblemListFragment.isShowProblemlist();
                    transaction.show(mProblemListFragment);
                }
                break;
            case ConstantsValue.PERSON_FRAGMENT_INDEX:


                if (null == mCenterFragment) {
                    mCenterFragment = new MyCenterFragment();

                    transaction.add(R.id.center_layout, mCenterFragment, "TAG"
                            + index);
                } else {
                    transaction.show(mCenterFragment);
                }
                break;

            default:
                break;
        }
        mIndex = index;
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != mHomeFragment5) {
            transaction.hide(mHomeFragment5);
        }
        if (null != mProblemListFragment) {
            transaction.hide(mProblemListFragment);
        }
        if (null != mCenterFragment) {
            transaction.hide(mCenterFragment);
        }

    }
*/