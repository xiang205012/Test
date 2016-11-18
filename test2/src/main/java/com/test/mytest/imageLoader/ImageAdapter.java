package com.test.mytest.imageLoader;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.test.mytest.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private String dirPath;// 文件夹路径
    private List<String> imgs;// 图片文件名
    /**保存选中的图片*/
    private static Set<String> saveSelecterImg = new HashSet<>();

    public ImageAdapter(Context context,String dirPath,List<String> imgPaths){
        this.context = context;
        this.dirPath = dirPath;
        this.imgs = imgPaths;
    }
    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int i) {
        return imgs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(view == null){
            view = View.inflate(context, R.layout.image_loader_grid_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.iv_imageLoader);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.cb_imageLoader);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // 重置状态
        viewHolder.imageView.setImageResource(R.drawable.pictures_no);
        viewHolder.checkBox.setButtonDrawable(R.drawable.picture_unselected);
        viewHolder.imageView.setColorFilter(null);

        final String imgPath = dirPath + "/" + imgs.get(i);
        ImageLoader.getmInstance(3,ImageLoader.TYPE_LOAD_LIFO).loadImage(
                imgPath,viewHolder.imageView);

        if(saveSelecterImg.contains(imgPath)){
            viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.checkBox.setButtonDrawable(R.drawable.pictures_selected);
        }

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveSelecterImg.contains(imgPath)){
                    saveSelecterImg.remove(imgPath);
                    viewHolder.imageView.setColorFilter(null);
                    viewHolder.checkBox.setButtonDrawable(R.drawable.picture_unselected);
                }else{
                    saveSelecterImg.add(imgPath);
                    viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.checkBox.setButtonDrawable(R.drawable.pictures_selected);
                }
            }
        });

        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }
}