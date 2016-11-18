package com.test.mytest.testCustomCarmer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 此界面展示拍照的图片
 * Created by Administrator on 2016/1/3.
 */
public class CustomCameraResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1,-1);
        imageView.setLayoutParams(params);
        setContentView(imageView);

        String imagePath = getIntent().getStringExtra("imageFile");
        Bitmap bp = BitmapFactory.decodeFile(imagePath);
        Matrix matrix = new Matrix();//矩阵
        matrix.setRotate(90);//旋转90度
        Bitmap bitmap = Bitmap.createBitmap(bp,0,0,bp.getWidth(),bp.getHeight(),matrix,true);
        imageView.setImageBitmap(bitmap);

        //以下方式，虽然在Camera时做了竖屏的设置，显示出来的是横屏效果
        //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        //imageView.setImageBitmap(bitmap);




    }
}
