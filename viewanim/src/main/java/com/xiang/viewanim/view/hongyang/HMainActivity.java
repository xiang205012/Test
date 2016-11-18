package com.xiang.viewanim.view.hongyang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiang.viewanim.R;
import com.xiang.viewanim.view.hongyang.simple.CustomViewGroup.MyTestViewGroupActivity;
import com.xiang.viewanim.view.hongyang.simple.switchButton.SwitchButtonActivity;
import com.xiang.viewanim.view.hongyang.simple.textview.MyTextViewActivity;
import com.xiang.viewanim.view.hongyang.simple.volumControlBar.CustomVolumControlBarActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/one.
 */
public class HMainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hmain);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4})
    public void click(View view){
        Intent intent = null;
        switch(view.getId()){
            case R.id.btn1:
                intent = new Intent(this, MyTextViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                intent = new Intent(this, CustomVolumControlBarActivity.class);
                startActivity(intent);
                break;
            case R.id.btn3:
                intent = new Intent(this, MyTestViewGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.btn4:
                intent = new Intent(this, SwitchButtonActivity.class);
                startActivity(intent);
                break;
        }


    }

}
