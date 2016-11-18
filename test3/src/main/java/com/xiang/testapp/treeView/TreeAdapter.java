package com.xiang.testapp.treeView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by gordon on 2016/10/19.
 */

public abstract class TreeAdapter<T> extends BaseAdapter {

    protected List<Node> mAllNode;
    protected List<Node> mVisibleNode;

    private OnTreeItemClickListener mListener;

    public void setOnTreeItemClickListener(OnTreeItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnTreeItemClickListener{
        void onTreeItemClick(Node node);
    }


    public TreeAdapter(ListView listView, List<T> datas, int defaultLevel, int currentLevel) throws IllegalAccessException {
        mAllNode = TreeHelper.getSortData(datas,defaultLevel,currentLevel);
        mVisibleNode = TreeHelper.filterVisibleNode(mAllNode);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Node node = mVisibleNode.get(position);
                expandOrClose(node);
                if (mListener != null) {
                    mListener.onTreeItemClick(node);
                }
            }
        });
    }

    private void expandOrClose(Node node) {
        node.setExpand(!node.isExpand());
        if (node.getChildList().size() > 0) {
            for (int i = 0; i < node.getChildList().size(); i++) {
                Node child = node.getChildList().get(i);
                child.setExpand(node.isExpand());
            }
        }
        mVisibleNode = TreeHelper.filterVisibleNode(mAllNode);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mVisibleNode.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNode.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNode.get(position);
        convertView = getConvertView(node,position,convertView,parent);
        convertView.setPadding(node.getLevel() * 30,3,3,3);
        return convertView;
    }

    protected abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);
}
