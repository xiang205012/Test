package com.test.mytest.testRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.mytest.R;

import java.util.List;

/**
 * Created by Administrator on 2016/1/16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<String> mData;

    public MyRecyclerAdapter(Context context,List<String> mDatas){
        this.mContext = context;
        this.mData = mDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**初始化布局，并创建ViewHolder返回*/
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    /**接收onCreateViewHolder返回的ViewHolder绑定mData的相关数据*/
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.recyclerview_textview.setText(mData.get(position));
        //holder.itemView是整个item的布局
        //设置item的监听事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    //获取item在布局上的位置，因为它是局部刷新所以当增加item时再点击就可能出现位置错误，所以不要直接用position
                    int itemPosition = holder.getLayoutPosition();
                    listener.onItemClick(holder.itemView,itemPosition);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取item在布局上的位置，因为它是局部刷新所以当增加item时再点击就可能出现位置错误，所以不要直接用position
                int itemPosition = holder.getLayoutPosition();
                listener.onItemLongClick(holder.itemView,itemPosition);
                return false;
            }
        });
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    /**增加一个item*/
    public void insertItem(int position){
        mData.add(position,"你好");
        //注意这里刷新是不要调用notifyDataSetChange()
        notifyItemInserted(position);
    }
    /**删除一个item*/
    public void removeItem(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        //初始化itemView中各个控件
        TextView recyclerview_textview;

        public MyViewHolder(View itemView) {
            super(itemView);
            recyclerview_textview = (TextView) itemView.findViewById(R.id.recyclerview_textview);
            recyclerview_textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


}
