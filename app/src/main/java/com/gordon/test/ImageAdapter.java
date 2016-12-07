package com.gordon.test;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/11/23 0023
 */

public class ImageAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private String dirPath;
    private List<String> imgList;
    private static List<String> selectorImgList = new ArrayList<>();

    public ImageAdapter(Context context,String dirPath,List<String> imgList){
        this.dirPath = dirPath;
        this.imgList = imgList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder ;
        if(view == null){
            view = inflater.inflate(R.layout.adapter_item,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.iv_picture);
            holder.checkBox = (CheckBox) view.findViewById(R.id.cb_selector);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        holder.imageView.setColorFilter(null);
        holder.checkBox.setChecked(false);

        final String path = dirPath + "/" + imgList.get(i);
//        Log.d("tag","   图片路径： " + path);

        ImageLoader.getmInstance(3,ImageLoader.TYPE_LIFO)
                .loadImage(path,holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectorImgList.contains(path)){
                    holder.imageView.setColorFilter(null);
                    holder.checkBox.setChecked(false);
                    selectorImgList.remove(holder.imageView);
                }else {
                    holder.imageView.setColorFilter(Color.parseColor("#77000000"));
                    holder.checkBox.setChecked(true);
                    selectorImgList.add(path);
                }
            }
        });

        if(selectorImgList.contains(path)){
            holder.imageView.setColorFilter(Color.parseColor("#77000000"));
            holder.checkBox.setChecked(true);
        }

        return view;
    }

    class ViewHolder{
        ImageView imageView;
        CheckBox checkBox;
    }
}
