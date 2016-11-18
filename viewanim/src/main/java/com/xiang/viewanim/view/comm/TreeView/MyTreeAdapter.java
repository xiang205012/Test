package com.xiang.viewanim.view.comm.TreeView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gordon on 2016/7/3.
 */
public class MyTreeAdapter<T> extends TreeAdapter<T>{

    private static final int ICON = 1;
    private static final int TITLE = 2;


    /**
     * @param tree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开层级
     * @throws IllegalAccessException
     */
    public MyTreeAdapter(ListView tree, Context context,
                         List<T> datas,
                         int defaultExpandLevel) throws IllegalAccessException {
        super(tree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = createView(parent);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewWithTag(ICON);
            holder.title = (TextView) convertView.findViewWithTag(TITLE);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(node.getIcon() == -1) {
            holder.icon.setVisibility(View.GONE);
        }else{
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setBackgroundResource(node.getIcon());
        }
        holder.title.setText(node.getName());

        return convertView;
    }

    @NonNull
    private LinearLayout createView(ViewGroup parent) {
        LinearLayout content = new LinearLayout(parent.getContext());
        AbsListView.LayoutParams contentPL = new AbsListView.LayoutParams(-1,-2);
        content.setLayoutParams(contentPL);
        content.setOrientation(LinearLayout.HORIZONTAL);

        ImageView imageView = new ImageView(parent.getContext());
        LinearLayout.LayoutParams ivPL = new LinearLayout.LayoutParams(-2,-2);
        ivPL.leftMargin = 15;
        ivPL.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(ivPL);
        imageView.setTag(ICON);
        content.addView(imageView);

        TextView textView = new TextView(parent.getContext());
        LinearLayout.LayoutParams tvPL = new LinearLayout.LayoutParams(-2,-2);
        tvPL.leftMargin = 10;
        tvPL.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(tvPL);
        textView.setTag(TITLE);
        content.addView(textView);

        return content;
    }

    class ViewHolder{

        ImageView icon;
        TextView title;
    }

    /**动态插入节点*/
    public void addItemNode(int position, String s) {
        // 获取到点击的node
        Node node = mVisibleNodes.get(position);
        // 找到当前node在整个tree中的位置
        int indexOf = mAllNodes.indexOf(node);
        // 创建新Node，真是情况应该是将此node添加到数据库或服务器，然后再从数据库或服务器获取到
        Node extraNode = new Node(-1,node.getId(),s);// 此处id对数据库或服务器很重要，但对于只是为了显示
        // 如果需要加入后展开
        //node.setExpand(true);

        extraNode.setParent(node);
        node.getChildren().add(extraNode);
        mAllNodes.add(indexOf + 1,extraNode);
        mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
        notifyDataSetChanged();
    }


}
