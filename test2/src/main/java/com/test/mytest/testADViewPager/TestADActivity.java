package com.test.mytest.testADViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/12/7 0007
 */

public class TestADActivity extends Activity {

    BannerLayout adLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout layout = new RelativeLayout(this);
        adLayout = new BannerLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,300);
        adLayout.setLayoutParams(params);
        layout.addView(adLayout);
        setContentView(layout);

        List<Integer> imgList = new ArrayList<>();
        imgList.add(R.drawable.ic_launcher);
        imgList.add(R.drawable.ic_launcher);
        imgList.add(R.drawable.ic_launcher);
        imgList.add(R.drawable.ic_launcher);
        imgList.add(R.drawable.ic_launcher);

        adLayout.setImgList(imgList);

        adLayout.setOnBannerLayoutListener(new BannerLayout.OnClickBannerItemListener() {

            @Override
            public void onClickItem(int position) {
                Log.d("tag","  点击了第 " + position + "个");
            }
        });

        adLayout.setOnLastIndexBannerListener(new BannerLayout.OnLastIndexBannerListener() {
            @Override
            public void isLastItem() {
                Log.d("tag","最后一个了，你该干点什么");
            }
        });


        adLayout.setIndictorAlignType(BannerLayout.INDICTOR_ALIGN_LEFT);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adLayout.setLifeCycle(BannerViewPager.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        adLayout.setLifeCycle(BannerViewPager.PAUSE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adLayout.setLifeCycle(BannerViewPager.DESTROY);
    }
}
