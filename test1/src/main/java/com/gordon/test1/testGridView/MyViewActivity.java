package com.gordon.test1.testGridView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import com.gordon.test1.R;
/**
 * Created by gordon on 2016/9/23.
 */

public class MyViewActivity extends AppCompatActivity implements MyView.OnItemClick {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150);
        MyView view = new MyView(this);
        view.setLayoutParams(params);
        view.setOnItemClick(this);
        setContentView(view);

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);

        view.createItem(list);
        view.setClick(0);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"点击了第" + position + "张图片",Toast.LENGTH_SHORT).show();
    }
}
