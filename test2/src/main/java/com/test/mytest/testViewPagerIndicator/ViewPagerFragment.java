package com.test.mytest.testViewPagerIndicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ViewPagerFragment extends Fragment {

    public static final String BUNDLE_TITLE = "title";

    public static ViewPagerFragment newInstance(String title){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        if(getArguments() != null){
            Bundle bundle = getArguments();
            String title = bundle.getString(BUNDLE_TITLE);
            textView.setText(title);
        }

        return textView;
    }
}
