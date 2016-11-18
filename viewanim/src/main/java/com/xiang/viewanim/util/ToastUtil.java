package com.xiang.viewanim.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/2/17.
 */
public class ToastUtil {

    private static Toast mToast = null;
    public static void showToast(Context context,String msg){
        if(mToast == null){
            mToast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }


}
