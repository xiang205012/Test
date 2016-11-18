package com.xiang.viewanim.view.QQ.swipeListView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiang.viewanim.R;

import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class SwipeAdapter extends BaseAdapter {

    private List<String> datas;

    public SwipeAdapter(List<String> datas){
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(parent.getContext(), R.layout.swipe_list_item,null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(datas.get(position));

        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
    }
}
