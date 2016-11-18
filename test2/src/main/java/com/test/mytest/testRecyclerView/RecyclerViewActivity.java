package com.test.mytest.testRecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/8/11.
 */
public class RecyclerViewActivity extends AppCompatActivity {

    @InjectView(R.id.my_recyclerview)
    RecyclerView recyclerView;
    /**  A ~ z所有字母 */
    private List<String> mDatas = new ArrayList<>();
    private MyRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_layout);
        ButterKnife.inject(this);

        initData();

        adapter = new MyRecyclerAdapter(this,mDatas);
        recyclerView.setAdapter(adapter);
        //设置recyclerView显示的样式(通过不同的LayoutManager实现listview,gridview,瀑布流...)
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);//true表示从最后开始显示，fasle从第一个开始显示
        recyclerView.setLayoutManager(linearLayoutManager);
        //添加分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        //添加item增加删除时的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    /**  A ~ z所有字母 */
    private void initData() {
        for (int i = 'A';i <= 'z';i++){
            mDatas.add(""+(char)i);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单布局
        getMenuInflater().inflate(R.menu.menu_recyclerview,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //菜单点击事件
        switch(item.getItemId()){
            case R.id.recycler_item_add:
                adapter.insertItem(1);
                break;
            case R.id.recycler_item_delete:
                adapter.removeItem(1);
                break;
            case R.id.recycler_vertical_listview:
                recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                break;
            case R.id.recycler_horizontal_listview:
                recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                break;
            case R.id.recycler_vertical_gridview:
                recyclerView.setLayoutManager(new GridLayoutManager(this,3));//3列
                break;
            case R.id.recycler_horizontal_gridview:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5,//5列
                        StaggeredGridLayoutManager.HORIZONTAL));
                break;
            case R.id.recycler_vertical_staggeredgridview://瀑布流(改变item的布局，因为瀑布流的item高度是不一样的)
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5,//5列
                        StaggeredGridLayoutManager.VERTICAL));
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
