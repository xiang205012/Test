package com.test.mytest.imageLoader;

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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.mytest.R;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class ImageLoaderActivity extends Activity {

    private GridView girdView;
    private List<String> mImgs;
    private ImageAdapter imgAdapter;
    private RelativeLayout rlBottom;
    private TextView tvFileDirName;
    private TextView tvFileCount;
    /**当前显示的文件夹*/
    private File mCurretnDir;
    /**当前文件夹中的文件数量*/
    private int mMaxCount;
    private ProgressDialog progressDialog;
    private List<FolderBean> folderBeanList = new ArrayList<>();
    private static final int DATA_LOADED = 0x786;// 图片扫描完成，handler发送消息

    private ListPopWindow popWindow;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == DATA_LOADED){
                progressDialog.dismiss();
                bindDataToView();
                initPopWindow();
            }
        }
    };

    private void bindDataToView() {
        if(mCurretnDir == null){
            Log.d("tag"," 未扫描到任何图片...");
            return;
        }
        mImgs = Arrays.asList(mCurretnDir.list());
        imgAdapter = new ImageAdapter(this,mCurretnDir.getAbsolutePath(),mImgs);
        girdView.setAdapter(imgAdapter);
        Log.d("tag"," mMaxCount : " + mMaxCount);
        tvFileCount.setText(mMaxCount + "");
        tvFileDirName.setText(mCurretnDir.getName());
    }

    private void initPopWindow() {
        popWindow = new ListPopWindow(this,folderBeanList);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
            }
        });
        popWindow.setOnDirSeletorListener(new ListPopWindow.OnDirSeletorListener() {
            @Override
            public void onSeleted(FolderBean folderBean) {
                mCurretnDir = new File(folderBean.getDir());
                String[] list = mCurretnDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        if (s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".png")) {
                            return true;
                        }
                        return false;
                    }
                });
                mImgs = Arrays.asList(list);
                mMaxCount = mImgs.size();

                imgAdapter = new ImageAdapter(ImageLoaderActivity.this,mCurretnDir.getAbsolutePath(),mImgs);
                girdView.setAdapter(imgAdapter);

                tvFileDirName.setText(mCurretnDir.getName());
                tvFileCount.setText(mMaxCount + "");

                popWindow.dismiss();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader);

        initView();
        initDatas();
        initEvent();

    }

    private void initView() {
        girdView = (GridView) findViewById(R.id.iv_gird);
        rlBottom = (RelativeLayout) findViewById(R.id.rl_file_bottom);
        tvFileDirName = (TextView) findViewById(R.id.tv_fileName);
        tvFileCount = (TextView) findViewById(R.id.tv_fileCount);
    }

    private void initDatas() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.d("tag","  当前存储卡不可用....");
            return;
        }
        progressDialog = ProgressDialog.show(this,null,"正在加载...");
        final Set<String> parentFilePaths = new HashSet<>();
        // 开启线程扫描图片
        new Thread(){
            @Override
            public void run() {
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = ImageLoaderActivity.this.getContentResolver();
                Cursor cursor = contentResolver.query(uri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                while(cursor.moveToNext()){
                    String path = cursor.getString(
                            cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if(parentFile == null){
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;
                    if(parentFilePaths.contains(dirPath)){
                        continue;
                    }else {
                        parentFilePaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }
                    if(parentFile.list() == null){
                        continue;
                    }

                    // 获取图片数量
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String fileName) {
                            if(fileName.endsWith(".JPG")
                                    || fileName.endsWith(".JPEG")
                                    || fileName.endsWith(".PNG")){
                                return true;
                            }
                            return false;
                        }
                    }).length;

                    folderBean.setCount(picSize);
                    folderBeanList.add(folderBean);
                    if(picSize > mMaxCount){
                        mMaxCount = picSize;
                        mCurretnDir = parentFile;
                    }
                    Log.d("tag","  picSize : " + picSize + "  path : " + path);
                }
                // 扫描完成
                cursor.close();
                // 通知handler扫描完成
                mHandler.sendEmptyMessage(DATA_LOADED);
            }
        }.start();

    }

    private void initEvent() {

        rlBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 设置进入退出的动画
                popWindow.setAnimationStyle(R.style.ImageLoader_dir_PopWindow);
                popWindow.showAsDropDown(rlBottom,0,0);

                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 0.6f;
                getWindow().setAttributes(layoutParams);
            }
        });
    }


}
