package com.xiang.testapp.treeView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gordon on 2016/10/19.
 */

public class MyTreeAdapter<T> extends TreeAdapter<T> {

    public static final String ICON = "icon";
    public static final String TITLE = "title";

    public MyTreeAdapter(ListView listView, List<T> datas, int defaultLevel, int currentLevel) throws IllegalAccessException {
        super(listView, datas, defaultLevel, currentLevel);
    }

    @Override
    protected View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LinearLayout content = new LinearLayout(parent.getContext());
            content.setOrientation(LinearLayout.HORIZONTAL);
            AbsListView.LayoutParams contentParams = new AbsListView.LayoutParams(-1,-2);
            content.setLayoutParams(contentParams);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2,-2);
            holder.ivIcon = new ImageView(parent.getContext());
            holder.ivIcon.setTag(ICON);
            holder.ivIcon.setLayoutParams(params);
            content.addView(holder.ivIcon);

            holder.tvTitle = new TextView(parent.getContext());
            holder.tvTitle.setTag(TITLE);
            holder.tvTitle.setLayoutParams(params);
            content.addView(holder.tvTitle);

            convertView = content;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivIcon.setImageResource(node.getIcon());
        holder.tvTitle.setText(node.getLabel());
        return convertView;
    }

    static class ViewHolder{
        TextView tvTitle;
        ImageView ivIcon;
    }

}
