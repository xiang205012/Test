package com.test.mytest.testTopNavigationBar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.test.mytest.R;

/**
 * Created by Administrator on 2015/11/23.
 */
public class TopNavigationBarActivity extends FragmentActivity {

    private TopLNAVBar topLNAVBar;
    private Hol fsfd;

    private String[] strs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topnavigetionbar_activity);

        fsfd = (Hol) findViewById(R.id.fsfd);

        strs = new String[]{ "热门", "推荐", "NBA", "国际足球", "中国足球", "CBA", "综合体育" };
        topLNAVBar = (TopLNAVBar) findViewById(R.id.topbar);
        topLNAVBar.setTitle(strs);
        ViewPager viewPager = new ViewPager(this);
        topLNAVBar.initView(this,viewPager);

    }
}
