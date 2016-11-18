package cj.library.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by cj on 2016/2/19.
 */
public class ToastUtil {

    private static Toast mToast;

    public static void show(Context context,String msg){
        if(mToast == null){
            mToast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    public static void longShow(Context context,String msg){
        if(mToast == null){
            mToast = Toast.makeText(context,"",Toast.LENGTH_LONG);
        }
        mToast.setText(msg);
        mToast.show();
    }

}
