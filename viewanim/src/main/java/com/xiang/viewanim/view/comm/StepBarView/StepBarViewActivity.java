package com.xiang.viewanim.view.comm.StepBarView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiang.viewanim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/6/5.
 */
public class StepBarViewActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepbarview);

        final StepBarView stepBarView = (StepBarView) findViewById(R.id.stepbar_view);

        List<String> list = new ArrayList<>();
        list.add(0,"已下单");
        list.add(1,"去支付");
        list.add(2,"已发货");
        list.add(3,"确定收货");
        stepBarView.setSteps(list);

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepBarView.nextStep();
            }
        });
        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepBarView.reset();
            }
        });


    }
}
