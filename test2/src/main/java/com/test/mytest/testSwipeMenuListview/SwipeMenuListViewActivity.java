package com.test.mytest.testSwipeMenuListview;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.mytest.R;

import java.util.List;

import cj.library.view.refreshView.swipeMenuListView.SwipeMenu;
import cj.library.view.refreshView.swipeMenuListView.SwipeMenuCreator;
import cj.library.view.refreshView.swipeMenuListView.SwipeMenuItem;
import cj.library.view.refreshView.swipeMenuListView.SwipeMenuListView;

/**
 * Created by Administrator on 2015/12/16.
 */
public class SwipeMenuListViewActivity extends Activity {

    private static final String TAG = "SwipeListView--->>>";
    SwipeMenuListView swipe_listview;

    private List<ApplicationInfo> appList;
    private AppAdapter adapter;
    /**侧滑菜单构建器*/
    private SwipeMenuCreator menuCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_swipelistview);

        swipe_listview = (SwipeMenuListView) findViewById(R.id.swipe_listview);
        appList = getPackageManager().getInstalledApplications(0);
        adapter = new AppAdapter(appList);

        swipe_listview.setAdapter(adapter);

        //创建侧滑菜单
        menuCreator = createSwipeMenu();
        //设置菜单
        swipe_listview.setMenuCreator(menuCreator);
        //侧滑菜单的点击事件
        swipe_listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                SwipeMenuItem item = menu.getMenuItem(position);
                String itemName = item.getTitle();
                Log.d(TAG, "你选中的是:" + itemName);
                Log.d(TAG,"你选中的位置是:"+ position);
                Toast.makeText(getApplicationContext(),itemName,Toast.LENGTH_SHORT);
            }
        });

    }

    private SwipeMenuCreator createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("Open");
                openItem.setTitleSize(20);
                openItem.setTitleColor(Color.WHITE);
                //添加第一个menu
                menu.addMenuItem(openItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete);
                //deleteItem.setTitle("删除");文本和图片一起会在高度上不充满listview的item的高度
                deleteItem.setTitleSize(20);
                deleteItem.setTitleColor(Color.BLUE);
                //添加第二个menu
                menu.addMenuItem(deleteItem);
            }
        };

        return creator;
    }

    class AppAdapter extends BaseAdapter {

        private List<ApplicationInfo> list;

        public AppAdapter(List<ApplicationInfo> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ApplicationInfo appInfo = list.get(position);
            ViewHolder holder = null;
            if(convertView == null){
                convertView = View.inflate(parent.getContext(),R.layout.swipe_listview_item,null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_icon.setImageDrawable(appInfo.loadIcon(getPackageManager()));
            holder.tv_name.setText(appInfo.loadLabel(getPackageManager()));

            return convertView;
        }
    }

    class ViewHolder{
        TextView tv_name;
        ImageView iv_icon;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }

}
