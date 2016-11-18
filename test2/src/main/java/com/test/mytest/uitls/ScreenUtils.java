package com.test.mytest.uitls;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ScreenUtils {

    public static int pxTodp(Context context, int px){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,px,context.getResources().getDisplayMetrics());
    }

    public static int dpTopx(Context context, int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,dp,context.getResources().getDisplayMetrics());
    }

    public static int dpTosp(Context context, int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,dp,context.getResources().getDisplayMetrics());
    }

    public static int pxTosp(Context context, int px){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,px,context.getResources().getDisplayMetrics());
    }

}
