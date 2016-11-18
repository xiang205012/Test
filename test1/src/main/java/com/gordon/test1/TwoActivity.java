package com.gordon.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/2/19.
 */
public class TwoActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        findViewById(R.id.toMainActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwoActivity.this,MainActivity.class);
                startActivity(intent);

//                overridePendingTransition();
            }
        });

    }

    @Override
    protected boolean toggleOverridePendingTrasition() {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTrasitionMode() {
        return TransitionMode.RIGHT;
    }
}
