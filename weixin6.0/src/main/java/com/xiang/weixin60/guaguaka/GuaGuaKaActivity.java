package com.xiang.weixin60.guaguaka;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.xiang.weixin60.R;

/**
 * Created by Administrator on 2016/2/23.
 */
public class GuaGuaKaActivity extends Activity {

    private GuaGuaKaView guaGuaKaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guaguaka);

        guaGuaKaView = (GuaGuaKaView) findViewById(R.id.guaguaview);
        guaGuaKaView.setTextInfo("你");
        guaGuaKaView.setOnScratchOverListener(new GuaGuaKaView.OnScratchOverListener() {
            @Override
            public void onScratchComplete() {
                Toast.makeText(GuaGuaKaActivity.this,"刮开区域大于60%了",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
