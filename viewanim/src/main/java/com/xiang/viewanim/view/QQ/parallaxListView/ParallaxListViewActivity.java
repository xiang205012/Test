package com.xiang.viewanim.view.QQ.parallaxListView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.xiang.viewanim.R;
import com.xiang.viewanim.data.Cheeses;

/**
 * Created by Administrator on 2016/2/15.
 */
public class ParallaxListViewActivity extends Activity {

    private ParallaxListView listView;
    private View header;
    private ImageView iv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax_listview);

        listView = (ParallaxListView) findViewById(R.id.parallax_listview);
        header = View.inflate(this,R.layout.parallax_listview_header,null);
        iv_header = (ImageView) header.findViewById(R.id.iv_parallax_header);

        listView.addHeaderView(header);

        /**当视图填充完毕后调用*/
        header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获得图片在布局中的高度
                listView.getIvHeight(iv_header);
                header.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        listView.setAdapter(new ArrayAdapter<String>(ParallaxListViewActivity.this, android.R.layout.simple_list_item_1, Cheeses.NAMES));
    }
}
