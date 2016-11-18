package com.xiang.testapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.xiang.testapp.R;
import com.xiang.testapp.ZoomImageView;

/**
 * Created by gordon on 2016/10/9.
 */

public class ZoomImageViewActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1,-1);
        ZoomImageView imageView = new ZoomImageView(this);
        imageView.setLayoutParams(params);
        imageView.setImageResource(R.drawable.test_zoomimageview);
        setContentView(imageView);

    }
}
