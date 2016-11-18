package cj.library.utils;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * view相关工具类
 * Created by cj on 2015/11/22.
 */
public class ViewUtils {

    /**
     * 通过父view移除子view
     * @param v
     */
    public static void removeParent(View v){
        //  先找到爹 在通过爹去移除孩子
        ViewParent parent = v.getParent();
        //所有的控件 都有爹  爹一般情况下 就是ViewGoup
        if(parent instanceof ViewGroup){
            ViewGroup group=(ViewGroup) parent;
            group.removeView(v);
        }
    }

    /**
     * 获取控件的实际高度
     * @return
     */
    private int getMeasureHeight(View view) {
        //由于是上下展开，所以宽度是不变的
        int width = view.getMeasuredWidth();
        //高度先设置成包裹内容
        view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        //制定测量规则
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);//精确值
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(3000, View.MeasureSpec.AT_MOST);//以实际测量为准
        //根据上面的宽度和高度测量规则进行实际测量
        view.measure(widthMeasureSpec,heightMeasureSpec);
        //返回实际测量的高度值
        return view.getMeasuredHeight();
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * 动态生成View ID
     * API LEVEL 17 以上View.generateViewId()生成
     * API LEVEL 17 以下需要手动生成
     */
    public static int generateViewId() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }



}

