package com.gordon.test1.testUcrop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gordon.test1.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/5/9.
 *
 *  UCrop详解：http://www.jianshu.com/p/523e77a10321
 *            http://www.tuicool.com/articles/faq22i
 */
public class CropActivity extends Activity implements View.OnClickListener {

    private ImageView iv_ucrop;
    private TextView take_photo;
    private TextView take_xiangce;
    private TextView take_cancel;

    /** 拍照临时图片路径*/
    private String mPhotoPath;
    /**剪切后图像文件*/
    private Uri mDestinationUri;

    /**拍照*/
    private static final int CAMERA_REQUEST_CODE = 1;
    /**相册选取*/
    private static final int ACTION_PICK = 2;

    /**图片裁剪宽度*/
    private int width;
    /**图片裁剪高度*/
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucrop);

        mPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.png";
        mDestinationUri = Uri.fromFile(new File(CropActivity.this.getCacheDir(),"ucrop_photo.png"));

        iv_ucrop = (ImageView) findViewById(R.id.iv_ucrop);
        take_photo = (TextView) findViewById(R.id.take_photo);
        take_xiangce = (TextView) findViewById(R.id.take_xiangce);
        take_cancel = (TextView) findViewById(R.id.take_cancel);
        take_photo.setOnClickListener(this);
        take_xiangce.setOnClickListener(this);
        take_cancel.setOnClickListener(this);

        iv_ucrop.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                width = iv_ucrop.getWidth();
                height = iv_ucrop.getHeight();
                Log.i("TAG-->>>","图片的宽度："+width+"  图片的高度："+height);
                iv_ucrop.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }





    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.take_photo:
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(mPhotoPath)));
                startActivityForResult(takeIntent,CAMERA_REQUEST_CODE);
                break;
            case R.id.take_xiangce:
                Intent pickIntent = new Intent(Intent.ACTION_PICK,null);
                // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(pickIntent,ACTION_PICK);
                break;
            case R.id.take_cancel:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == this.RESULT_OK){
            switch(requestCode){
                case CAMERA_REQUEST_CODE:// 拍照
                    File temp = new File(mPhotoPath);
                    // 打开剪切
                    startCropActivity(Uri.fromFile(temp));
                    break;
                case ACTION_PICK://相册
                    startCropActivity(data.getData());
                    break;
                case UCrop.REQUEST_CROP:
                // 图片裁剪后再UCropActivity中又使用startActivityforResult传回来
                    handleCropResult(data);
                    break;
                case UCrop.RESULT_ERROR:
                    handleCropError(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**启动剪切图片Activity*/
    private void startCropActivity(Uri uri) {
        // uri：拍照后图片的路径，mDestinationUri:裁剪后保存路径
        UCrop.of(uri,mDestinationUri)
                .withAspectRatio(1,1)
                .withMaxResultSize(width,height)
                .start(this);
    }

    /**裁剪后的data*/
    private void handleCropResult(Intent data) {
        deleteTempPhotoFile();
        Uri resultUri = UCrop.getOutput(data);
        if(resultUri != null){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                iv_ucrop.setImageBitmap(bitmap);
                String filePath = resultUri.getEncodedPath();
                String imagePath = Uri.decode(filePath);
                Log.i("TAG-->>","图片已经保存到："+imagePath);
                Log.i("TAG -->>","剪切时图片保存的路径："+Uri.decode(mDestinationUri.getEncodedPath()));
                // 上传服务器：upLoadImageFile(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,"无法剪切图片",Toast.LENGTH_SHORT).show();
        }

    }

    /**删除拍照时的临时图片*/
    private void deleteTempPhotoFile() {
        File tempFile = new File(mPhotoPath);
        if(tempFile.exists() && tempFile.isFile()){
            tempFile.delete();
        }
    }


    private void handleCropError(Intent data) {
        deleteTempPhotoFile();
        Throwable cropError = UCrop.getError(data);
        if(cropError != null){
            Log.i("TAG-->>","handleCropError:"+cropError.getMessage());
            Toast.makeText(this,cropError.getMessage(),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"无法剪切图片",Toast.LENGTH_SHORT).show();
        }
    }
}
