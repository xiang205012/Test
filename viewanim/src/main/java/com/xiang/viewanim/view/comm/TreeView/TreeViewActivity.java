package com.xiang.viewanim.view.comm.TreeView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.xiang.viewanim.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gordon on 2016/6/15.
 */
public class TreeViewActivity extends Activity {


    private ListView listView;
    private MyTreeAdapter<FileBean> adapter;
    private List<FileBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = new ListView(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1,-1);
        setContentView(listView);

        initDatas();

        try {
            adapter = new MyTreeAdapter<FileBean>(listView,this,mDatas,1);
            listView.setAdapter(adapter);
            adapter.setOnTreeNodeClickListener(new TreeAdapter.OnTreeNodeClickListener() {
                @Override
                public void onTreeNodeClick(Node node, int position) {
                    if(node.isLeaf()){
                        ToastUtil.showToast(TreeViewActivity.this,node.getName());
                    }
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final EditText et = new EditText(TreeViewActivity.this);
                    AlertDialog dialog = new AlertDialog.Builder(TreeViewActivity.this)
                            .setTitle("动态添加item")
                            .setView(et)
                            .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.addItemNode(position,et.getText().toString());
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .show();

                    return true;
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        // 添加根目录
        FileBean bean = new FileBean(1,0,"根目录1");
        mDatas.add(bean);
        bean = new FileBean(2,0,"根目录2");
        mDatas.add(bean);
        bean = new FileBean(3,0,"根目录3");
        mDatas.add(bean);

        // 添加根目录下的子目录
        bean = new FileBean(4,1,"根目录1-1");
        mDatas.add(bean);
        bean = new FileBean(5,1,"根目录1-2");
        mDatas.add(bean);

        bean = new FileBean(6,2,"根目录2-1");
        mDatas.add(bean);
        bean = new FileBean(7,2,"根目录2-2");
        mDatas.add(bean);
        bean = new FileBean(8,2,"根目录2-3");
        mDatas.add(bean);
        bean = new FileBean(9,8,"根目录2-3-1");
        mDatas.add(bean);
        bean = new FileBean(10,8,"根目录2-3-2");
        mDatas.add(bean);

        bean = new FileBean(11,3,"根目录3-1");
        mDatas.add(bean);
        bean = new FileBean(12,3,"根目录3-2");
        mDatas.add(bean);
        bean = new FileBean(13,3,"根目录3-3");
        mDatas.add(bean);
        bean = new FileBean(14,3,"根目录3-4");
        mDatas.add(bean);

    }
}
