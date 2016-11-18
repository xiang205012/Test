package cj.library.utils;

import android.graphics.Color;

/**
 * Created by gordon on 2016/6/18.
 */
public class ColorUtils {

    /**
     * 将指定的颜色转换成制定透明度的颜色
     * @param color
     * @param ratio
     * @return
     */
    public static int getTranspartColorByAlpha(int color, float ratio){
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }



}
