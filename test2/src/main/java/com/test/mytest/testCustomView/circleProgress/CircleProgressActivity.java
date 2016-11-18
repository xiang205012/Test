package com.test.mytest.testCustomView.circleProgress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.test.mytest.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/1/13.
 */
public class CircleProgressActivity extends Activity {

    @InjectView(R.id.circleprogress1)
    CircleProgressBar circleProgressBar1;
    @InjectView(R.id.circleprogress2)
    CircleProgressBar circleProgressBar2;

    @InjectView(R.id.test_circleProgress)
    Button testCircleProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        ButterKnife.inject(this);

        testCircleProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int progress = 0;
                        while(progress <= 100){
                            progress += 3;
                            circleProgressBar1.setProgress(progress);
                            circleProgressBar2.setProgress(progress);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });


    }



}
