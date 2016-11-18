package com.xiang.viewanim.view.comm.progressbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xiang.viewanim.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gordon on 2016/10/26.
 */

public class CircleDotProgressActivity extends Activity {


    private CircleDotProgressBar bar_null;

    private int max = 100;
    private int progress;
    private boolean isProgressGoing;

    private Timer mTimer;
    private TimerTask mTimerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_dot_progress);

        initData();

        bar_null = fvbi(R.id.circleDotProgressBar);

        bar_null.setOnButtonClickListener(new CircleDotProgressBar.OnButtonClickListener() {
            @Override
            public void onButtonClick(View view) {
                if (!isProgressGoing) {
                    if (progress == max) {
                        progress = 0;
                        bar_null.setProgress(progress);
                    }
                    startProgress();
                } else {
                    stopProgress();
                }
            }
        });
    }

    private void initData() {
        max = 100;
        readyProgress();
    }

    private void readyProgress() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!isProgressGoing) {
                        return;
                    }
                    if (++progress >= max) {
                        progress = max;
                        bar_null.setProgress(progress);
                        stopProgress();
                        return;
                    }
                    bar_null.setProgress(progress);
                }
            };
        }
    }

    private void startProgress(){
        isProgressGoing = true;
        stopTimerTask();
        readyProgress();
        mTimer.schedule(mTimerTask,1000,100);
    }

    private void stopProgress() {
        isProgressGoing = false;
        stopTimerTask();
    }

    private void stopTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
        mTimerTask = null;
    }

    private <V extends View> V fvbi(int id){
        return (V) findViewById(id);
    }
}
