package com.test.mytest.testObserver;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.mytest.R;

import java.util.Observable;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 测试观察者
 * Created by Administrator on 2016/1/22.
 */
public class ObserverActivity extends Activity {
    @InjectView(R.id.tv_observer)
    TextView tv_observer;
    @InjectView(R.id.btn_start_one_or_two)
    Button btn_start_one_or_two;
    @InjectView(R.id.et_number)
    EditText et_number;

    private MyObserver observer = new MyObserver(){
        @Override
        public void update(Observable observable, Object data) {
            Number number = (Number) data;
            if(tv_observer != null) {
                tv_observer.setText("当前number ： " + number.getCount());
            }
            Log.i("观察到了数据变化TAG-->>", "number : " + number);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //观察者往被观察者中添加订阅事件
        MyObservable.getInstance().addObserver(observer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //观察者从被观察者队列中移除
        MyObservable.getInstance().deleteObserver(observer);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer);
        ButterKnife.inject(this);

        btn_start_one_or_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_number.getText().toString().trim();
                if(str != null) {
                    int number = Integer.valueOf(str);
                    Number num = new Number();
                    num.setCount(number);
                    //通知观察者数据变化了
                    MyObservable.getInstance().notifyDataChange(num);
                }
            }
        });

    }
}
