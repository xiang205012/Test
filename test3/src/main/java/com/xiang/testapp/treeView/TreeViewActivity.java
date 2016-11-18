package com.xiang.testapp.treeView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/10/19.
 */

public class TreeViewActivity extends Activity {

    private List<FileBean> mDatas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = new ListView(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1,-1);
        listView.setLayoutParams(params);
        setContentView(listView);

        initDatas();

        try {
            MyTreeAdapter adapter = new MyTreeAdapter(listView,mDatas,1,1);
            listView.setAdapter(adapter);
            adapter.setOnTreeItemClickListener(new TreeAdapter.OnTreeItemClickListener() {
                @Override
                public void onTreeItemClick(Node node) {
                    Log.d("tag"," click : " + node.getLabel());
                }
            });


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    private void initDatas() {
        FileBean fileBean = new FileBean(1,0,"根目录1");
        mDatas.add(fileBean);
        FileBean fileBean1 = new FileBean(2,0,"根目录2");
        mDatas.add(fileBean1);
        FileBean fileBean2 = new FileBean(3,0,"根目录3");
        mDatas.add(fileBean2);

        FileBean fileBean3 = new FileBean(4,1,"子目录1-1");
        mDatas.add(fileBean3);
        FileBean fileBean4 = new FileBean(5,1,"子目录1-2");
        mDatas.add(fileBean4);
        FileBean fileBean5 = new FileBean(6,2,"子目录2-1");
        mDatas.add(fileBean5);
        FileBean fileBean6 = new FileBean(7,2,"子目录2-2");
        mDatas.add(fileBean6);
        FileBean fileBean7 = new FileBean(8,2,"子目录2-3");
        mDatas.add(fileBean7);
        FileBean fileBean8 = new FileBean(9,8,"子目录2-3-1");
        mDatas.add(fileBean8);
        FileBean fileBean9 = new FileBean(10,8,"子目录2-3-2");
        mDatas.add(fileBean9);
        FileBean fileBean10 = new FileBean(11,3,"子目录3-1");
        mDatas.add(fileBean10);
        FileBean fileBean11 = new FileBean(12,3,"子目录3-2");
        mDatas.add(fileBean11);
        FileBean fileBean12 = new FileBean(13,3,"子目录3-3");
        mDatas.add(fileBean12);
        FileBean fileBean13 = new FileBean(14,3,"子目录3-4");
        mDatas.add(fileBean13);
        FileBean fileBean14 = new FileBean(15,14,"子目录3-4-1");
        mDatas.add(fileBean14);
        FileBean fileBean15 = new FileBean(16,14,"子目录3-4-2");
        mDatas.add(fileBean15);
        FileBean fileBean16 = new FileBean(17,14,"子目录3-4-3");
        mDatas.add(fileBean16);
        FileBean fileBean17 = new FileBean(18,14,"子目录3-4-4");
        mDatas.add(fileBean17);

    }
}
