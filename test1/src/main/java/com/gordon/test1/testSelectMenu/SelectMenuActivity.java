package com.gordon.test1.testSelectMenu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.gordon.test1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/9/13.
 */
public class SelectMenuActivity extends AppCompatActivity {

    private SelectMenuLayout selectMenuView;

    private int count = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_menu);

        selectMenuView = (SelectMenuLayout) findViewById(R.id.select_menu);

        selectMenuView.initView();

        List<String> menuTitles = new ArrayList<>();
        menuTitles.add("java");
        menuTitles.add("php");
        menuTitles.add("objectc");
        menuTitles.add("mysql");
        menuTitles.add("android");

        selectMenuView.createMenuItems(menuTitles);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(params);

        Button button = new Button(this);
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        button.setText("隐藏弹出框");
        button.setTextSize(30);
        button.setPadding(8,8,8,8);
        button.setLayoutParams(btnParams);
        relativeLayout.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag-->>","button click");
            }
        });

        selectMenuView.setContentMainView(relativeLayout);

        selectMenuView.setOnMenuSelectListener(new SelectMenuLayout.OnMenuSelectListener() {
            @Override
            public View onMenuSelect(int position, String menuTitle) {
                final RelativeLayout relativeLayout2 = new RelativeLayout(SelectMenuActivity.this);
                relativeLayout2.setBackgroundColor(Color.WHITE);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                relativeLayout2.setLayoutParams(params2);

                Button button2 = new Button(SelectMenuActivity.this);
                RelativeLayout.LayoutParams btnParams2 = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                btnParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
                button2.setText("弹出框上的按钮  " + count);
                button2.setTextSize(30);
                button2.setPadding(18,18,18,18);
                button2.setLayoutParams(btnParams2);
                relativeLayout2.addView(button2);
                if(count == 3){
                    selectMenuView.setPopMainLayoutHeight(50);
                }
                count++;
                return relativeLayout2;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(selectMenuView.isShowing()){
            selectMenuView.dismissPopContentView();
            return;
        }
        super.onBackPressed();
        Log.d("tag-->>","onBackPressed");
        selectMenuView.destory();
    }
}
