package com.xiang.viewanim.view.QQ;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiang.viewanim.R;
import com.xiang.viewanim.view.QQ.dragLayout.DragLayoutActivity;
import com.xiang.viewanim.view.QQ.goo.GooActivity;
import com.xiang.viewanim.view.QQ.parallaxListView.ParallaxListViewActivity;
import com.xiang.viewanim.view.QQ.swipeListView.SwipeListViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/one.
 */
public class QQMainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqmain);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.qq_btn1,R.id.qq_btn2,R.id.qq_btn3,R.id.qq_btn4})
    public void click(View view){
        Intent intent = null;
        switch(view.getId()){
            case R.id.qq_btn1:
                intent = new Intent(this, ParallaxListViewActivity.class);
                startActivity(intent);
                break;
            case R.id.qq_btn2:
                intent = new Intent(this, SwipeListViewActivity.class);
                startActivity(intent);
                break;
            case R.id.qq_btn3:
                intent = new Intent(this, GooActivity.class);
                startActivity(intent);
                break;
            case R.id.qq_btn4:
                intent = new Intent(this, DragLayoutActivity.class);
                startActivity(intent);
                break;
        }


    }

}
