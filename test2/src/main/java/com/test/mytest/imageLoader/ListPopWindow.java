package com.test.mytest.imageLoader;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.mytest.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class ListPopWindow extends PopupWindow {

    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private List<FolderBean> mDatas;

    private OnDirSeletorListener mListener;

    public void setOnDirSeletorListener(OnDirSeletorListener listener){
        this.mListener = listener;
    }

    public interface OnDirSeletorListener{
        void onSeleted(FolderBean folderBean);
    }

    public ListPopWindow(Context context, List<FolderBean> datas){
        calWidthAndHeight(context);
        this.mDatas = datas;
        mConvertView = LayoutInflater.from(context).inflate(R.layout.image_loader_list_popwindow,null);

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

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

        initView(context);
        initEvent();
    }

    private void initView(Context context) {
        mListView = (ListView) mConvertView.findViewById(R.id.lv_image_loader_popWindow);
        mListView.setAdapter(new ListDirAdapter(context,mDatas));
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mListener != null){
                    mListener.onSeleted(mDatas.get(i));
                }
            }
        });
    }

    private void calWidthAndHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        mHeight = (int) (displayMetrics.heightPixels * 0.7);

    }

    private class ListDirAdapter extends ArrayAdapter<FolderBean>{

        private List<FolderBean> mDatas;
        private LayoutInflater inflater;

        public ListDirAdapter(Context context, List<FolderBean> datas) {
            super(context, 0, datas);// 因为重写了getView，所以构造方法中的resource没有作业直接传入0 即可
            this.mDatas = datas;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.image_loader_list_pop_item,parent,false);
                holder.mImg = (ImageView) convertView.findViewById(R.id.iv_imageLoader_list);
                holder.tvDirName = (TextView) convertView.findViewById(R.id.tvFilePath_imageLoader_item);
                holder.tvDirCount = (TextView) convertView.findViewById(R.id.tvFileCount_imageLoader_item);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            FolderBean folderBean = mDatas.get(position);
            // 重置
            holder.mImg.setImageResource(R.drawable.pictures_no);
            // 加载图片
            ImageLoader.getmInstance(3,ImageLoader.TYPE_LOAD_LIFO).loadImage(folderBean.getFirstImgPath(),holder.mImg);
            holder.tvDirName.setText(folderBean.getName());
            holder.tvDirCount.setText(folderBean.getCount() + "");
            return convertView;
        }

        private class ViewHolder{
            ImageView mImg;
            TextView tvDirName;
            TextView tvDirCount;
        }
    }

}
