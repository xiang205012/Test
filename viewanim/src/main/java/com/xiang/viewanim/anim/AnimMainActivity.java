package com.xiang.viewanim.anim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiang.viewanim.R;
import com.xiang.viewanim.anim.ballBeatIndicator.BallBeatIndicatorActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/one.
 */
public class AnimMainActivity extends Activity {

    @InjectView(R.id.anim_btn_1)
    Button anim_btn_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.anim_btn_1})
    public void click(View view){
        Intent intent = null;
        switch(view.getId()){
            case R.id.anim_btn_1:
                intent = new Intent(AnimMainActivity.this, BallBeatIndicatorActivity.class);
                break;
        }
        startActivity(intent);
    }


}
