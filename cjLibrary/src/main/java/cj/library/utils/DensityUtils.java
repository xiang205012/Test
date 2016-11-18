package cj.library.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 常用单位转换的辅助类
 * dp2px，sp2px，px2dp，px2sp
 * Created by cj on 2015/11/22.
 */
public class DensityUtils{

    private DensityUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
//    public static int dp2px(Context context, float dpVal)
//    {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                dpVal, context.getResources().getDisplayMetrics());
//    }

    /**
     * sp转px
     *
     * @param context
     * @return
     */
//    public static int sp2px(Context context, float spVal)
//    {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//                spVal, context.getResources().getDisplayMetrics());
//    }

    /**转换dip为px*/
    public static int dpTopx(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dip*scale + 0.5f*(dip>=0?1:-1));
    }

    /**转换px为dip*/
    public static int pxTodp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(px/scale + 0.5f*(px>=0?1:-1));
    }
    /**转换sp为px*/
    public static int spTopx(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    /**转换px为sp*/
    public static int pxTosp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
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

}
