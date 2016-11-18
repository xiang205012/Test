package com.test.mytest.MaterialDesign.toolbar;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.test.mytest.R;

import butterknife.ButterKnife;
import cj.library.base.AppBaseActivity;
import cj.library.utils.LogUtils;

/**
 * Created by Administrator on 2015/11/26.
 */
public class MyToolBarActivity extends AppBaseActivity implements View.OnClickListener {

//    @InjectView(R.id.toolbar)
//    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        ButterKnife.inject(this);
        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //隐藏标题
        //toolbar.setTitle("");
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //toolbar.setDisplayHomeAsUpEnabled(true);设置系统默认返回箭头，默认有点击事件
        //设置自定义返回图标，需要定义点击事件
        toolbar.setNavigationIcon(R.drawable.top_nav_icon_back);
        toolbar.setNavigationOnClickListener(this);

    }

    @Override
    protected boolean toggleOverridePendingTrasition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTrasitionMode() {
        return null;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }


    @Override
    public void onClick(View v) {
        LogUtils.d("fsjflsd");
        MyToolBarActivity.this.finish();
    }

    //如果想右边有东西就在相应的menu.xml中设置menu菜单并重写menu相关方法

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
