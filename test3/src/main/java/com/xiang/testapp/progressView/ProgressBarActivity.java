package com.xiang.testapp.progressView;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xiang.testapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class ProgressBarActivity extends Activity{
	
	private ProgressBar barView;
	private Timer mTimer;
	private TimerTask mTask;
	private boolean isProgressGoing;
	private int max;
	private int progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progressbar);

		initData();

		barView = (ProgressBar) findViewById(R.id.progressBar);
		barView.setProgressMax(max);
		barView.setOnClickButtonListener(new ProgressBar.OnClickButtonListener() {
			@Override
			public void onClickButton(View view) {
				if (!isProgressGoing) {
					if (progress == max) {
						progress = 0;
						barView.setProgress(progress);
					}
					startTask();
				}else {
					stopTask();
				}
				Log.d("tag"," isProgressGoing : " + isProgressGoing);
			}
		});
		
	}

	private void startTask() {
		isProgressGoing = true;
		readyProgress();
		mTimer.schedule(mTask,1000,100);
	}

	private void initData() {
		isProgressGoing = false;
		max = 100;
		readyProgress();
	}

	private void readyProgress() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTask == null) {
			mTask = new TimerTask() {
				@Override
				public void run() {
					if (!isProgressGoing) {
						return;
					}
					if (++progress >= max) {
						progress = max;
						barView.setProgress(progress);
						stopTask();
						return;
					}
					Log.d("tag"," mTask run");
					barView.setProgress(progress);
				}
			};
		}

	}

	private void stopTask() {
		if (mTimer != null) {
			mTimer.cancel();
		}
		if (mTask != null) {
			mTask.cancel();
		}
		mTimer = null;
		mTask = null;
		isProgressGoing = false;
	}


}
