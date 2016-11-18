package com.gordon.rrod;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gordon.rrod.Dagger2.Dagger2Activity;
import com.gordon.rrod.Okhttp3.OkHttp3Activity;
import com.gordon.rrod.Retrofit.Retrofit2Activity;
import com.gordon.rrod.RxJava.RxjavaActivity;
import com.gordon.rrod.Sample.SampleActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.btn_dagger2,R.id.btn_okhttp3,
            R.id.btn_retrofit2,R.id.btn_rxjava,R.id.btn_simple})
    public void Click(View view){
        switch(view.getId()){
            case R.id.btn_dagger2:
                startActivity(new Intent(MainActivity.this,Dagger2Activity.class));
                break;
            case R.id.btn_okhttp3:
                startActivity(new Intent(MainActivity.this,OkHttp3Activity.class));
                break;
            case R.id.btn_retrofit2:
                startActivity(new Intent(MainActivity.this,Retrofit2Activity.class));
                break;
            case R.id.btn_rxjava:
                startActivity(new Intent(MainActivity.this,RxjavaActivity.class));
                break;
            case R.id.btn_simple:
                startActivity(new Intent(MainActivity.this,SampleActivity.class));
                break;
        }
    }

}
