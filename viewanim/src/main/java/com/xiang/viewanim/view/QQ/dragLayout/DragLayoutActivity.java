package com.xiang.viewanim.view.QQ.dragLayout;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.xiang.viewanim.R;
import com.xiang.viewanim.data.Cheeses;
import com.xiang.viewanim.util.ToastUtil;

import java.util.Random;

/**
 * Created by Administrator on 2016/2/16.
 */
public class DragLayoutActivity extends Activity {

    private DragLayout dragLayout;
    private ListView left_listview;
    private ListView main_listview;
    private ImageView left_iv;
    private ImageView main_iv;
    private MyLinearLayout myLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draglayout);

        dragLayout = (DragLayout) findViewById(R.id.qq_draglayout);
        left_iv = (ImageView) findViewById(R.id.left_iv);
        left_listview = (ListView) findViewById(R.id.left_listview);
        main_iv = (ImageView) findViewById(R.id.main_iv);
        main_listview = (ListView) findViewById(R.id.main_listview);
        myLinearLayout = (MyLinearLayout) findViewById(R.id.my_linearlayout);

        left_listview.setAdapter(new ArrayAdapter<String>(DragLayoutActivity.this, android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(Color.WHITE);
                return view;
            }
        });

        main_listview.setAdapter(new ArrayAdapter<String>(DragLayoutActivity.this,android.R.layout.simple_list_item_1,Cheeses.NAMES));

        myLinearLayout.setDragLayout(dragLayout);

        dragLayout.setDragStatusListener(new DragLayout.OnDragStatusChangeListener() {
            @Override
            public void onClose() {
                ToastUtil.showToast(DragLayoutActivity.this, "onClose");
                // 让主面板imageview晃动
                ObjectAnimator animator = ObjectAnimator.ofFloat(main_iv, "translationX", 15.0f);
                animator.setInterpolator(new CycleInterpolator(4));//来回晃动4次
                animator.setDuration(500);
                animator.start();

            }

            @Override
            public void onOpen() {
                ToastUtil.showToast(DragLayoutActivity.this, "onOpen");
                // 测试回调
                int nextInt = new Random().nextInt(100);
                // 左面板listview 移动到一个随机位置
                left_listview.smoothScrollToPosition(nextInt);
            }

            @Override
            public void onDraging(float percent) {
                // percent:0.0f - 1.0f
                // 主面板 imageView的透明度 从看得见到看不见(1.0f - 0.0f),而percent的变化是从0.0-1.0f
                ViewHelper.setAlpha(main_iv, 1 - percent);
            }
        });


    }
}
