package com.gordon.test1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.gordon.test1.RecycleViewRefresh.RecycleMainActivity;
import com.gordon.test1.testFragment.testBetweenActivity.TheOneActivity;
import com.gordon.test1.testFragment.testBetweenFragment.BetweenFragmentActivity;
import com.gordon.test1.testUcrop.CropActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        findViewById(R.id.toTwoActivity).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, RecycleViewListActivity.class);
//                startActivity(intent);
//            }
//        });

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#877978"));
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean toggleOverridePendingTrasition() {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTrasitionMode() {
        return TransitionMode.LEFT;
    }


    @OnClick({R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4})
    public void click(View view){
        Intent intent = null;
        switch(view.getId()){
            case R.id.btn1:
                intent = new Intent(this, RecycleMainActivity.class);
                break;
            case R.id.btn2:
                intent = new Intent(this, CropActivity.class);
                break;
            case R.id.btn3:
                intent = new Intent(this, TheOneActivity.class);
                break;
            case R.id.btn4:
                intent = new Intent(this, BetweenFragmentActivity.class);
                break;
        }
        startActivity(intent);
    }

}
