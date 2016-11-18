package com.test.mytest.testTopIndicator;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

public class CustomPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{
	
	private List<Fragment> mFragments;
	private Context context;
	private TopIndicator indicator;

	public CustomPagerAdapter(Context context,FragmentManager fm,List<Fragment> mFragments,TopIndicator indicator) {
		super(fm);
		this.context = context;
		this.indicator = indicator;
		this.mFragments = mFragments;
	}

	@Override
	public Fragment getItem(int item) {
		if(mFragments != null){
			return mFragments.get(item);
		}
		return null;
	}

	@Override
	public int getCount() {
		if(mFragments != null){
			return mFragments.size();
		}
		return 0;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		indicator.setTabsDisplay(context,position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
}
