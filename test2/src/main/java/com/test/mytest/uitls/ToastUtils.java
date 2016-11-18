package com.test.mytest.uitls;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ToastUtils {

    public static Toast mToast;
    public static void show(Context context,String msg){
        if(mToast == null){
            mToast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }
}
