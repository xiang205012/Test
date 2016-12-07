package com.gordon.test;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private View rootView;
    private GridView girdView;
    private RelativeLayout rlBottom;
    private TextView tvFileName;
    private TextView tvFileCount;

    private int fileCount;
    private String fileName;
    private List<String> imgPaths = new ArrayList<>();
    private ImageAdapter adapter;
    private File dirFile;
    private List<FolderBean> folderBeanList = new ArrayList<>();
    private static final int DATA_LOADED = 0X11;

    private ListPopWindow popWindow;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(dirFile == null){
                return;
            }
            imgPaths = Arrays.asList(dirFile.list());
            adapter = new ImageAdapter(MainActivity.this,dirFile.getAbsolutePath(),imgPaths);

            girdView.setAdapter(adapter);

            tvFileName.setText(fileName);
            tvFileCount.setText(fileCount + "");

            popWindow = new ListPopWindow(MainActivity.this,folderBeanList);
            popWindow.setAnimationStyle(R.style.ListPopWindow);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this,R.layout.activity_main,null);
        setContentView(rootView);

        girdView = (GridView) findViewById(R.id.gridView);
        rlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        tvFileName = (TextView) findViewById(R.id.tv_fileName);
        tvFileCount = (TextView) findViewById(R.id.tv_fileCount);

        initDatas();
        rlBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(rootView,"scaleX",1.0f,0.9f);
//                scaleXAnim.setDuration(1000);
                ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(rootView,"scaleY",1.0f,0.9f);
//                scaleYAnim.setDuration(1000);

                ObjectAnimator rotationYanim = ObjectAnimator.ofFloat(rootView,"rotationX",0,20,0);
//                rotationYanim.setDuration(1000);
                rootView.setPivotY(rootView.getHeight() / 2);
                rootView.setPivotX(rootView.getWidth() / 2);

                animatorSet.play(scaleXAnim).with(scaleYAnim).with(rotationYanim);
                animatorSet.setDuration(1000);
                animatorSet.start();


                popWindow.showAtLocation(rlBottom, Gravity.BOTTOM,0,0);
            }
        });

    }

    private void initDatas() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.d("tag","当前存储卡不可用");
            return;
        }
        final Set<String> parentFilePaths = new HashSet<>();
        new Thread(){
            @Override
            public void run() {
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = MainActivity.this.getContentResolver();
                Cursor cursor = contentResolver.query(uri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                while(cursor.moveToNext()){
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if(parentFile == null){
                        continue;
                    }
                    String parentPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;
                    if(parentFilePaths.contains(parentPath)){
                        continue;
                    }else{
                        if(parentFile.list() == null){
                            continue;
                        }
                        int length = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File file, String fileName) {
                                if(fileName.endsWith(".JPEG")
                                        || fileName.endsWith(".JPG")
                                        || fileName.endsWith(".PNG")){
                                    return true;
                                }
                                return false;
                            }
                        }).length;
//                        Log.d("tag", "  图片数量 ： " + length);
                        folderBean = new FolderBean();
                        folderBean.setDirName(parentPath);
                        folderBean.setImgPath(path);
                        folderBean.setCount(length);
                        parentFilePaths.add(parentPath);
                        dirFile = parentFile;
                        fileCount = length;
                        fileName = folderBean.getName();
                        folderBeanList.add(folderBean);
                    }

                }
                cursor.close();
                handler.sendEmptyMessage(DATA_LOADED);
            }
        }.start();
    }
}
