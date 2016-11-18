package com.test.mytest.testDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.test.mytest.R;

/**
 * Created by Administrator on 2015/11/10.
 */
public class DialogActivity extends Activity {

    Button bt_showpop;

    private MyDialog dialog;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);

        bt_showpop = (Button) findViewById(R.id.bt_showpop);

        dialog = new MyDialog(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminateDrawable(this.getResources().getDrawable(R.drawable.loading_progress));

        bt_showpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.show();
                progressDialog.show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
//            dialog.dismiss();
            progressDialog.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
}
