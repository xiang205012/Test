package com.test.mytest.testDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.test.mytest.R;

/**
 * Created by Administrator on 2015/11/10.
 */
public class MyDialog extends ProgressDialog {

    private Context context;

    public MyDialog(Context context) {
        super(context);
        this.context = context;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(context);
    }

    private void initView(Context context) {
        setContentView(R.layout.my_dialog);
//        ImageView imageView = (ImageView)findViewById(R.id.iv_retato_dialog);
//        Animation animation = AnimationUtils.loadAnimation(context,R.anim.iv_progress);
//        imageView.setAnimation(animation);
//        imageView.startAnimation(animation);

    }

    public void getProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.loading_progress));
        dialog.setMessage("正在加载...");
        dialog.show();
    }



}
