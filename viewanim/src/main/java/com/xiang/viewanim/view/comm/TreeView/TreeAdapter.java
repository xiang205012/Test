package com.xiang.viewanim.view.comm.TreeView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by gordon on 2016/6/29.
 */
public abstract class TreeAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    /**将用户的数据T转换为Node*/
    protected List<Node> mAllNodes;
    /**可见的所有Node*/
    protected List<Node> mVisibleNodes;
    /**显示数据的ListView*/
    protected ListView mTree;

    public interface OnTreeNodeClickListener{
        void onTreeNodeClick(Node node, int position);
    }

    private OnTreeNodeClickListener mListener;

    public void setOnTreeNodeClickListener(OnTreeNodeClickListener listener){
        this.mListener = listener;
    }

    /**
     * @param tree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开层级
     * @throws IllegalAccessException
     */
    public TreeAdapter(ListView tree,Context context,List<T> datas,int defaultExpandLevel)
            throws IllegalAccessException {

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mAllNodes = TreeHelper.getSortedNodes(datas,defaultExpandLevel);
        mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);

        mTree = tree;
        // 所有的第一级父节点都是可以被点击的，所以在此处添加点击事件
        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);
                if (mListener != null){
                    mListener.onTreeNodeClick(mVisibleNodes.get(position),position);
                }
            }
        });

    }

    /**点击收缩或展开*/
    private void expandOrCollapse(int position) {
        // 点击的肯定是可见的节点，所以从mVisibleNodes里面拿到当前点击的Node
        Node node = mVisibleNodes.get(position);
        if(node != null){
            if(node.isLeaf()) return;
            // 展开或收缩
            node.setExpand(!node.isExpand());
            // 重新过滤
            mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        // 也可以返回当前节点的ID(在调用的地方就可以直接通过id获取到当前对象)
        //return mVisibleNodes.get(position).getId();
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNodes.get(position);
        convertView = getConvertView(node,position,convertView,parent);
        // 设置节点层级缩进
        convertView.setPadding(node.getLevel() * 30,3,3,3);


        return convertView;
    }

    public abstract View getConvertView(Node node,int position,View convertView,ViewGroup parent);

}
