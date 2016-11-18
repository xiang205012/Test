package cj.library.view.loadingview;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2016/one/26.
 */
public interface ILoadingView {

    //view模式
    View getCurrentLayout();
    /**隐藏view模式加载框*/
    void restoreView();
    /**显示view模式加载框*/
    void showLayout(View view);
    /**当前显示的view的布局id*/
    View inflate(int layoutId);
    /**当前context*/
    Context getContext();
    /**当前显示view*/
    View getView();


    // 点击加载模式
    /**点击加载时显示的loading*/
    void showLoading(String msg);
    /**关闭点击加载显示的loading*/
    void dismissLoading();

}
