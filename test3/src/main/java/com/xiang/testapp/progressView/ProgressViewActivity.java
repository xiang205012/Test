package com.xiang.testapp.progressView;

import android.app.Activity;
import android.os.Bundle;

import com.xiang.testapp.R;

/**
 * Created by gordon on 2016/10/25.
 */

public class ProgressViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_view);


        final ProgressCircleView view = (ProgressCircleView) findViewById(R.id.progressView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int pecent = 0;
                while(pecent < 100){
                    pecent += 2;

                    try {
                        Thread.sleep(500);
                        view.setProgress(pecent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();



    }
}
