package com.xiang.viewanim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.xiang.viewanim.anim.AnimMainActivity;
import com.xiang.viewanim.view.view_main.ViewMainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.btn_view)
    Button btn_view;
    @InjectView(R.id.btn_anim)
    Button btn_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_view,R.id.btn_anim})
    public void Click(View view){
        switch(view.getId()){
            case R.id.btn_view:
                startActivity(new Intent(this, ViewMainActivity.class));
                break;
            case R.id.btn_anim:
                startActivity(new Intent(this, AnimMainActivity.class));
                break;
        }
    }

}
