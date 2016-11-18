package com.test.mytest.testMain;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.test.mytest.R;
import com.test.mytest.testCamerahead.CameraActivity;
import com.test.mytest.testCustomCalendar.CustomCalendarActivity;
import com.test.mytest.testCustomView.CustomViewActivity;
import com.test.mytest.testORMLite.ORMLiteActivity;
import com.test.mytest.testObserver.ObserverActivity;
import com.test.mytest.testPopWindow.PopWindowActivity;
import com.test.mytest.testRecyclerView.RecyclerViewActivity;
import com.test.mytest.testTaobaoSortList.TaobaoSortListActivity;
import com.test.mytest.testViewPager.ViewPagerActivity;
import com.test.mytest.testViewPagerIndicator.ViewPagerIndicatorActivity;
import com.test.mytest.testVolley.ImageActivity;

/**
 * Created by Administrator on 2015/8/4.
 */
public class AFragment extends Fragment implements View.OnClickListener {

    private Button toRecyclerViewActivity;
    private Button toStickyScrollViewActivity;
    private Button to_VolleyImageActivity;
    private Button to_CamerActivity;
    private Button to_ormLite;
    private Button to_popwindow;
    private Button to_custom_calendar;
    private Button to_custom_view;
    private Button to_anim_viewpager;
    private Button to_observer;
    private Button to_viewPagerIndicator;
    private Button to_meituantagmenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.afragment,null);
        toRecyclerViewActivity = (Button) view.findViewById(R.id.to_recyclerviewactivity);
        toRecyclerViewActivity.setOnClickListener(this);
        toStickyScrollViewActivity = (Button) view.findViewById(R.id.to_StickyScrollViewActivity);
        toStickyScrollViewActivity.setOnClickListener(this);

        to_VolleyImageActivity = (Button) view.findViewById(R.id.to_VolleyImageActivity);
        to_VolleyImageActivity.setOnClickListener(this);

        to_CamerActivity = (Button) view.findViewById(R.id.to_CamerActivity);
        to_CamerActivity.setOnClickListener(this);

        to_ormLite = (Button) view.findViewById(R.id.to_ormLite);
        to_ormLite.setOnClickListener(this);

        to_popwindow = (Button) view.findViewById(R.id.to_popwindow);
        to_popwindow.setOnClickListener(this);

        to_custom_calendar = (Button) view.findViewById(R.id.to_custom_calendar);
        to_custom_calendar.setOnClickListener(this);

        to_custom_view = (Button) view.findViewById(R.id.to_custom_view);
        to_custom_view.setOnClickListener(this);

        to_anim_viewpager = (Button) view.findViewById(R.id.to_anim_viewpager);
        to_anim_viewpager.setOnClickListener(this);

        to_observer = (Button) view.findViewById(R.id.to_observer);
        to_observer.setOnClickListener(this);

        to_viewPagerIndicator = (Button) view.findViewById(R.id.to_viewPagerIndicator);
        to_viewPagerIndicator.setOnClickListener(this);

        to_meituantagmenu = (Button) view.findViewById(R.id.to_meituantagmenu);
        to_meituantagmenu.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()){
            case R.id.to_recyclerviewactivity:
                intent = new Intent(getActivity(), RecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.to_StickyScrollViewActivity:
//                intent = new Intent(getActivity(), StickyScrollViewActivity.class);
//                startActivity(intent);
                break;
            case R.id.to_VolleyImageActivity:
                intent = new Intent(getActivity(), ImageActivity.class);
                startActivity(intent);
                break;
            case R.id.to_CamerActivity:
                intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.to_ormLite:
                intent = new Intent(getActivity(), ORMLiteActivity.class);
                startActivity(intent);
                break;
            case R.id.to_popwindow:
                intent = new Intent(getActivity(), PopWindowActivity.class);
                startActivity(intent);
                break;
            case R.id.to_custom_calendar:
                intent = new Intent(getActivity(), CustomCalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.to_custom_view:
                intent = new Intent(getActivity(), CustomViewActivity.class);
                startActivity(intent);
                break;
            case R.id.to_anim_viewpager:
                intent = new Intent(getActivity(), ViewPagerActivity.class);
                startActivity(intent);
                break;
            case R.id.to_observer:
                intent = new Intent(getActivity(), ObserverActivity.class);
                startActivity(intent);
                break;
            case R.id.to_viewPagerIndicator:
                intent = new Intent(getActivity(), ViewPagerIndicatorActivity.class);
                startActivity(intent);
                break;
            case R.id.to_meituantagmenu:
                intent = new Intent(getActivity(), TaobaoSortListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
