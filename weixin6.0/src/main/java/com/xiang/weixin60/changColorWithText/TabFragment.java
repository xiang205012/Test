package com.xiang.weixin60.changColorWithText;

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
 * Created by Administrator on 2016/1/20.
 */
public class TabFragment extends Fragment{

    private String title = "one";

    public TabFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView tv_title = new TextView(getActivity());
        tv_title.setGravity(Gravity.CENTER);
        tv_title.setTextSize(25);
        tv_title.setTextColor(Color.parseColor("#89899897"));
        if(getArguments().getString("title") != null) {
            tv_title.setText(getArguments().getString("title"));
        }
        return tv_title;
    }
}
