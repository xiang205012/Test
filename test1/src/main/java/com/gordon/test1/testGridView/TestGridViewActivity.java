package com.gordon.test1.testGridView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.gordon.test1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/9/22.
 */
public class TestGridViewActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testgridview);

        gridView = (GridView) findViewById(R.id.girdview);
        gridView.setNumColumns(3);
        gridView.setHorizontalSpacing(20);
        gridView.setVerticalSpacing(20);

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher);


        gridView.setAdapter(new MyAdapter(list));
    }


    private class MyAdapter extends BaseAdapter{

        private List<Integer> lists;

        public MyAdapter(List<Integer> list){
            this.lists = list;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1,-1);
            LinearLayout linearLayout = new LinearLayout(TestGridViewActivity.this);
            linearLayout.setBackgroundColor(Color.BLUE);
            linearLayout.setLayoutParams(params);
            LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(-1,-1);
            ImageView imageView = new ImageView(TestGridViewActivity.this);
            imageView.setLayoutParams(ivParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            linearLayout.addView(imageView);
            imageView.setImageResource(lists.get(position));
            return linearLayout;
        }
    }

    private class ViewHolder{
        ImageView imageView;
    }

}
