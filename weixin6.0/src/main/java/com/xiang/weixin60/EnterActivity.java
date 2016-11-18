package com.xiang.weixin60;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiang.weixin60.Face_Recognition.FaceActivity;
import com.xiang.weixin60.changColorWithText.MainActivity;
import com.xiang.weixin60.crop_photo.CropActivity;
import com.xiang.weixin60.guaguaka.GuaGuaKaActivity;
import com.xiang.weixin60.imageLoader.ImageLoaderActivity;
import com.xiang.weixin60.satelliteMenu.SatelliteMenuActivity;
import com.xiang.weixin60.surfaceView.SurfaceViewActivity;
import com.xiang.weixin60.zoomImageView.ZoomImageViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/3.
 */
public class EnterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,
            R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8})
    public void click(View view){
        switch(view.getId()){
            case R.id.btn1:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this, SatelliteMenuActivity.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(this, GuaGuaKaActivity.class));
                break;
            case R.id.btn4:
                startActivity(new Intent(this, SurfaceViewActivity.class));
                break;
            case R.id.btn5:
                startActivity(new Intent(this, ZoomImageViewActivity.class));
                break;
            case R.id.btn6:
                startActivity(new Intent(this, ImageLoaderActivity.class));
                break;
            case R.id.btn7:
                startActivity(new Intent(this, FaceActivity.class));
                break;
            case R.id.btn8:
                startActivity(new Intent(this, CropActivity.class));
                break;
        }
    }

}
