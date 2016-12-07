package com.gordon.test;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gordon on 2016/11/24 0024
 */

public class ListPopWindow extends PopupWindow {

    private int width;
    private int height;
    private List<FolderBean> list;

    public ListPopWindow(Context context, List<FolderBean> list){
        celWidthAndHeight(context);

        setWidth(width);
        setHeight(height);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1,-1);
        ListView listView = new ListView(context);
        listView.setLayoutParams(params);
        listView.setBackgroundColor(Color.parseColor("#ffffff"));
        listView.setAdapter(new PopAdapter(context,list));

        setContentView(listView);

    }

    private void celWidthAndHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = (int) (displayMetrics.heightPixels * 0.7);
    }

    private class PopAdapter extends ArrayAdapter<FolderBean>{
        private LayoutInflater inflater;
        private List<FolderBean> list;
        public PopAdapter(Context context, List<FolderBean> list) {
            super(context, 0, list);
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.pop_item,null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.iv_pop);
                holder.tvFileName = (TextView) convertView.findViewById(R.id.tv_fileName_pop);
                holder.tvFileCount = (TextView) convertView.findViewById(R.id.tv_fileCount_pop);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            FolderBean folderBean = list.get(position);
            holder.tvFileName.setText(folderBean.getName());
            holder.tvFileCount.setText(folderBean.getCount() + "");
            ImageLoader.getmInstance(3,ImageLoader.TYPE_LIFO).loadImage(folderBean.getImgPath(),holder.imageView);
            return convertView;
        }

        class ViewHolder{
            ImageView imageView;
            TextView tvFileName;
            TextView tvFileCount;
        }
    }

}
