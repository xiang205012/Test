package com.xiang.viewanim.view.QQ.swipeListView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.xiang.viewanim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class SwipeListViewActivity extends Activity {

    private ListView listView;
    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.swipe_list_item);
        //testSwipeLayout();
        setContentView(R.layout.activity_swipelistview);
        listView = (ListView) findViewById(R.id.lv_swipe);
        for(int i = 'A';i <= 'z';i++){
            datas.add((char)i+"");
        }
        listView.setAdapter(new SwipeAdapter(datas));

    }

    private void testSwipeLayout() {
        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.QQ_swipeLayout);
        swipeLayout.setOnSwipeLayoutListener(new SwipeLayout.OnSwipeLayoutListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                Log.i("TAG", "onClose");
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                Log.i("TAG", "onOpen");
            }

            @Override
            public void onDraging(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                Log.i("TAG", "onStartClose");
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.i("TAG", "onStartOpen");
            }
        });
    }
}
