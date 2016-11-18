package com.xiang.weixin60.crop_photo;

import android.app.Activity;
import android.os.Bundle;

import com.xiang.weixin60.R;
import com.xiang.weixin60.zoomImageView.ZoomImageView;

/**
 * Created by gordon on 2016/8/16.
 */
public class CropActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ZoomImageView zoomImageView = (ZoomImageView) findViewById(R.id.zoomImageView);
        zoomImageView.setImageResource(R.drawable.t4);
    }
}
