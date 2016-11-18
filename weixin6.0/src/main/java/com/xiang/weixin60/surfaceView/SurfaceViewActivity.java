package com.xiang.weixin60.surfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xiang.weixin60.R;

/**
 * Created by Administrator on 2016/3/1.
 */
public class SurfaceViewActivity extends Activity {

    private ImageView center_button;
    private LuckyPan mLuckyPan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfaceview);

        mLuckyPan = (LuckyPan) findViewById(R.id.luckypan);
        center_button = (ImageView) findViewById(R.id.center_button);

        center_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mLuckyPan.isStart()){
                    mLuckyPan.luckyStart();
                    center_button.setImageResource(R.drawable.stop);
                }else{
                    if(!mLuckyPan.isShouldEnd()){
                        mLuckyPan.luckyEnd();
                        center_button.setImageResource(R.drawable.start);
                    }
                }
            }
        });

    }
}
