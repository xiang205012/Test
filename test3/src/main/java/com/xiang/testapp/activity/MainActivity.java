package com.xiang.testapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xiang.testapp.R;
import com.xiang.testapp.utils.LogUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

//    private ProgressCircleView progressCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String radio = "64:27";
        String[] items = radio.split(":");
        Double width = Double.valueOf(items[0]);
        Double height = Double.valueOf(items[1]);


        LogUtils.d("除的结果为 ： "+ (width / height));

        Double rad = width / height;
        DecimalFormat fromat = new DecimalFormat("0.00");
        fromat.setRoundingMode(RoundingMode.HALF_UP);
        Double result = Double.valueOf(fromat.format(rad));

        LogUtils.d("保留小数点后两位："+result);

        LogUtils.d("模的结果为 ： "+ (width % height));



//        progressCircleView = (ProgressCircleView) findViewById(R.id.progressCircleView);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int progress = 0;
//                while(progress < 100){
//                    progress += 3;
//                    progressCircleView.setProgress(progress);
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },1000);

    }
}
