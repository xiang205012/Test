package com.xiang.weixin60.imageLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ImageLoaderActivity extends Activity {

    private GridView gridView;
    /** gridView需要的数据集 */
    private List<String> imgs;
    private ImageAdapter imageAdapter;

    private RelativeLayout bottomLy;
    private TextView mDirName;
    private TextView mDirCount;
    /**当前文件夹*/
    private File mCurrentDir;
    /**当前文件夹中文件数量*/
    private int mMaxCount;

    /** listView所需的数据集 */
    private List<FolderBean> lists = new ArrayList<FolderBean>();

    private ProgressDialog progressDialog;

    private static final int SEND_MESSAGE = 0X111;

    private Handler mHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == SEND_MESSAGE){
                progressDialog.dismiss();
                // 绑定数据到view中
                dataToView();
            }
        }
    };


    private void dataToView() {
        if(mCurrentDir == null){
            Toast.makeText(this,"未扫描到任何图片",Toast.LENGTH_SHORT).show();
            return;
        }
        imgs = Arrays.asList(mCurrentDir.list());
        imageAdapter = new ImageAdapter(this,imgs,mCurrentDir.getAbsolutePath());
        gridView.setAdapter(imageAdapter);
        mDirCount.setText(mMaxCount + "");
        mDirName.setText(mCurrentDir.getName());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader);
        initView();
        initData();
        initEvent();

    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gv_imageLoader);
        bottomLy = (RelativeLayout) findViewById(R.id.rl_imageLoder);
        mDirName = (TextView) findViewById(R.id.mDirName);
        mDirCount = (TextView) findViewById(R.id.mDirCount);


    }

    /**利用ContentProvider扫描图片*/
    private void initData() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this,"当前存储卡不可用",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = ProgressDialog.show(this,null,"正在加载...");

        new Thread(){
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = ImageLoaderActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor cursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED);
                Set<String> mDirPaths = new HashSet<String>();// 避免重复扫描文件夹
                while(cursor.moveToNext()){
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if(parentFile == null) continue;
                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;
                    if(mDirPaths.contains(dirPath)){
                        continue;
                    }else{
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }
                    if(parentFile.list() == null)continue;
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if(filename.endsWith(".jpg")
                                    ||filename.endsWith(".jpeg")
                                    ||filename.endsWith(".png")){
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    lists.add(folderBean);
                    if(picSize > mMaxCount){
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }

                }
                cursor.close();
                // 通知Handler扫描图片完成
                mHanlder.sendEmptyMessage(SEND_MESSAGE);
            }
        }.start();


    }

    private void initEvent() {

    }

    private class ImageAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private String dirPath;//当前文件夹路径
        private List<String> mDatas;//当前文件夹下所有文件名

        public ImageAdapter(Context context,List<String> mDatas,String dirPath){
            this.mDatas = mDatas;
            this.dirPath = dirPath;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.loaderimage_gv_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.select_image_button);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_show);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 重置状态
            viewHolder.imageButton.setImageResource(R.drawable.ic_menu_allfriends);
            viewHolder.imageView.setImageResource(R.drawable.composer_camera);
            ImageLoader.newInstance(3, ImageLoader.Type.LIFO).loadImage(dirPath +"/"+mDatas.get(position),viewHolder.imageView);

            return convertView;
        }

        class ViewHolder{
            
            ImageView imageView;
            ImageButton imageButton;
        }

    }


}
