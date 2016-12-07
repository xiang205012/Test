package com.xiang.viewanim.anim.ballBeatIndicator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;

import com.xiang.viewanim.R;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class BallBeatIndicatorActivity extends Activity {

    boolean isPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball_beat_indicator);

        Button playAnim = (Button) findViewById(R.id.btn_play_ballBeatInticatorView);

        final BallBeatInticatorView inticatorView = (BallBeatInticatorView) findViewById(R.id.ballBeatInticatorView);

        playAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlay) {
                    inticatorView.startAnima();
                }else {
                    inticatorView.clearAnimation();
                }
                isPlay = !isPlay;
            }
        });

    }
}
