package com.xiang.viewanim.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Administrator on 2016/2/1.
 */
public class ScreenUtil {

    /**
     * 转换后都是 px单位
     */

    public static int pxTodp(Context context,float px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,px,context.getResources().getDisplayMetrics());
    }

    public static int dpTopx(Context context,float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }

    public static int dpTosp(Context context,float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,dp,context.getResources().getDisplayMetrics());
    }

    public static int pxTosp(Context context, float px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,px,context.getResources().getDisplayMetrics());
    }

    /**
     * 只有Activity才能得到windowManager
     * @param context
     * @return 返回宽度的像素值
     */
    public static int getWindowWidth(Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度像素值
     * @param context
     * @return
     */
    public static int getWindowHeight(Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕像素密度
     * @param context
     * @return
     */
    public static float getWindowDensity(Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    /**
     * //转换dip为px
     2   public static int dpTopx(Context context, int dip) {
     3       float scale = context.getResources().getDisplayMetrics().density;
     4       return (int)(dip*scale + 0.5f*(dip>=0?1:-1));
     5   }
     6
     7   //转换px为dip
     8   public static int pxTodp(Context context, int px) {
     9       float scale = context.getResources().getDisplayMetrics().density;
     10      return (int)(px/scale + 0.5f*(px>=0?1:-1));
     11  }
     12
     13   public static int spTopx(Context context, float spValue) {
     14         float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
     15         return (int) (spValue * fontScale + 0.5f);
     16     }
     17
     18     public static int pxTosp(Context context, float pxValue) {
     19         float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
     20         return (int) (pxValue / fontScale + 0.5f);
     21     }
     */

}
