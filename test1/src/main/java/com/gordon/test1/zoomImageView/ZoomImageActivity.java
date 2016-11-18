package com.gordon.test1.zoomImageView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import com.gordon.test1.R;

/**
 * Created by gordon on 2016/8/29.
 */
public class ZoomImageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1,-1);
        relativeLayout.setLayoutParams(params);

        ZoomImageView imageView = new ZoomImageView(this);
        imageView.setImageResource(R.drawable.test_zoomimageview);
        imageView.setLayoutParams(params);
////        imageView.setBackgroundResource(R.drawable.test_zoomimageview);此方法在getDrawable()时拿不到图片,需通过getBackground();
////        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2,-2);
////        imageView.setLayoutParams(layoutParams);
        relativeLayout.addView(imageView);
        setContentView(relativeLayout);
//
//        CropView clopView = new CropView(this);
//        clopView.setLayoutParams(params);
//        relativeLayout.addView(clopView);
//        CropLayout cropLayout = new CropLayout(this);
//        setContentView(cropLayout);

    }
}
