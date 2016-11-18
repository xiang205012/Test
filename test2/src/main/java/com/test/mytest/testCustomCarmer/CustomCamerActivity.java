package com.test.mytest.testCustomCarmer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.test.mytest.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * android 5.0以后使用Camera2 API
 * Created by Administrator on 2016/1/3.
 */
public class CustomCamerActivity extends Activity implements SurfaceHolder.Callback{

    @InjectView(R.id.camera_surfaceview)
    SurfaceView surfaceView;

    private Camera mCamera;
    private SurfaceHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        ButterKnife.inject(this);

        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        //点击surfaceView时自动对焦
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });

    }

    @OnClick(R.id.take_picture)
    public void take_picture(){
        //设置Camera参数
        Camera.Parameters parameters = mCamera.getParameters();
        //设置拍照的图片格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        //设置拍照图片预览 大小
        parameters.setPreviewSize(800, 400);
        //设置自动对焦
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.autoFocus(new Camera.AutoFocusCallback(){
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    //如果对焦准确，就拍照
                    mCamera.takePicture(null,null,mPictureCallback);
                }
            }
        });

    }

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // data:图片数据
            File imageFile = new File("/sdcard/temp.png");
            try {
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
                Intent intent = new Intent(CustomCamerActivity.this,CustomCameraResultActivity.class);
                intent.putExtra("imageFile",imageFile.getAbsolutePath());
                startActivity(intent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera == null){
            mCamera = getCamera();
            if(mHolder != null){
                setStartPreview(mCamera,mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }


    /**
     * 获取系统Camera
     * @return
     */
    private Camera getCamera(){
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * 开始预览相机内容
     */
    private void setStartPreview(Camera camera,SurfaceHolder holder){
        try {
            camera.setPreviewDisplay(holder);
            //系统默认相机是横屏的，设置成90度为竖屏
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera(){
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //绑定Camera和SurfaceHolder
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
}
