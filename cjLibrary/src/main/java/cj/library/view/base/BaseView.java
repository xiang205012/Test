package cj.library.view.base;

/**
 * Created by Administrator on 2016/one/26.
 */
public interface BaseView {

    /**加载对话框*/
    void showLoading(String msg);

    /**关闭加载对话框*/
    void hideLoading();

    /**错误信息提示*/
    void showError(String msg);

    /**异常提示*/
    void showException(String msg);

    /**网络错误提示*/
    void showNetError();

}
