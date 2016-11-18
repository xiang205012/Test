package com.xiang.weixin60.satelliteMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xiang.weixin60.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/3.
 */
public class SatelliteMenuActivity extends Activity {

    private ListView listView;
    private MySatelliteMenu menu;
    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satallitemenu);
        listView = (ListView) findViewById(R.id.listview);
        menu = (MySatelliteMenu) findViewById(R.id.satemenu2);
        for(int i = 'A' ; i <= 'z';i++){
            data.add(""+(char)i);
        }
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data));

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (menu.getCurrentState()) {
                    menu.ItemAnim(300);
                }
            }
        });

        menu.setOnMenuClickListener(new MySatelliteMenu.onMenuClickListener() {
            @Override
            public void menuClick(View view, int position) {
                Toast.makeText(SatelliteMenuActivity.this,"position : " + position + " view tag : "+ view.getTag(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
