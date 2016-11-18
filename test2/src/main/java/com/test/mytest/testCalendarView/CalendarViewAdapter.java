package com.test.mytest.testCalendarView;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gordon on 2016/11/1.
 */

public class CalendarViewAdapter extends PagerAdapter {

    private CalendarCard[] views;

    public CalendarViewAdapter(CalendarCard[] views){
        this.views = views;
    }


    @Override
    public int getCount() {
//        return views.length;
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (((ViewPager)container).getChildCount() == views.length
                || views[position % views.length].getParent() != null){
            ((ViewPager)container).removeView(views[position % views.length]);
        }
        ((ViewPager)container).addView(views[position % views.length],0);
        return views[position % views.length];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View) container);
    }

    public CalendarCard[] getAllItems(){
        return views;
    }
}
