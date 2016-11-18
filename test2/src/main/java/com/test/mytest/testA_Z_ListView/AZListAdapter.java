package com.test.mytest.testA_Z_ListView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.mytest.R;

import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 */
public class AZListAdapter extends BaseAdapter {

    private Context context;
    private List<SortModel> list;

    public AZListAdapter(Context context,List<SortModel> list){
        this.context = context;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<SortModel> list){
        this.list = list;
        notifyDataSetChanged();
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
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.az_list_item,null);
            holder = new ViewHolder();
            holder.tv_letter_group = (TextView) convertView.findViewById(R.id.tv_letter_group);
            holder.tv_names = (TextView) convertView.findViewById(R.id.tv_names);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //根据position获取当前位置的首字母的char ascii值
        int section = list.get(position).getSortLetter().charAt(0);
        //如果当前位置等于该分类首字母的char的位置，则认为是第一次出现
        if(position == getPositionForSection(section)){
            holder.tv_letter_group.setVisibility(View.VISIBLE);
            holder.tv_letter_group.setText(list.get(position).getSortLetter());
        }else{
            holder.tv_letter_group.setVisibility(View.GONE);
        }
        holder.tv_names.setText(list.get(position).getName());
        return convertView;
    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for(int i = 0;i < getCount();i++){
            char firstChar = list.get(i).getSortLetter().charAt(0);
            if(firstChar == section){
                return i;
            }
        }
        return -1;
    }

    class ViewHolder{
        TextView tv_letter_group;
        TextView tv_names;
    }

}
