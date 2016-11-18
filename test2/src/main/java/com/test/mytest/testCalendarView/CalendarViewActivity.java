package com.test.mytest.testCalendarView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.mytest.R;

/**
 * Created by gordon on 2016/10/31.
 */

public class CalendarViewActivity extends Activity implements CalendarCard.OnCalendarCardListener {

    private CalendarCard[] calendarView = new CalendarCard[5];
    private int currentIndex = 1000;
    private ViewPager viewPager;
    private CalendarViewAdapter adapter;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        tvTitle = (TextView) findViewById(R.id.tv_calendar_date);
        tvTitle.setVisibility(View.GONE);

        viewPager = (ViewPager) findViewById(R.id.calendar_viewPager);
        viewPager.setPageTransformer(false,new AlphaAndScaleTransformer());
        for(int i = 0 ;i < 5;i++){
            calendarView[i] = new CalendarCard(this,this,true,false);
        }

        adapter = new CalendarViewAdapter(calendarView);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentIndex);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CalendarCard[] calendarView = adapter.getAllItems();
                CalendarCard calendarCard = calendarView[position % calendarView.length];
                if (position > currentIndex){
                    calendarCard.toPageRight();
                } else if (position < currentIndex){
                    calendarCard.toPageLeft();
                }
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onItemClick(CustomDate date) {
        Toast.makeText(this,""+date.getDay(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageChanged(CustomDate date) {
//        tvTitle.setText(date.getYear() + "-" + date.getMonth());
    }
}
