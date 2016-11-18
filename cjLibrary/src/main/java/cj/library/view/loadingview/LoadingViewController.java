package cj.library.view.loadingview;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cj.library.R;


/**
 * 显示网络错误，加载失败等界面
 * Created by cj on 2016/one/26.
 */
public class LoadingViewController {

    private ILoadingView iLoadingView;

    public LoadingViewController(){

    }

    /**
     * 需要加载后才能显示的view
     * 就是将加载后的view替换为loadingView
     * 加载完成后再替换回来
     * @param view
     */
    public LoadingViewController(View view){
        this(new LoadingViewHelper(view));
    }

    public LoadingViewController(ILoadingView iLoadingView){
        this.iLoadingView = iLoadingView;
    }

    /**给一开始不用view模式，后面有要用了的时候调用*/
    public void setLayoutView(View view){
        this.iLoadingView = new LoadingViewHelper(view);
    }

    public void showError(String msg,View.OnClickListener onClickListener){
        View layout = iLoadingView.inflate(R.layout.network_error);
        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.ll_message_error);
        TextView textView = (TextView) layout.findViewById(R.id.tv_message_error);
        if(msg.equals("")||msg != null){
            textView.setText(msg);
        }
        if(onClickListener != null){
            linearLayout.setOnClickListener(onClickListener);
        }
        //显示加载或错误布局界面
        iLoadingView.showLayout(layout);
    }

    /**
     * 显示加载对话框
     * @param isViewStyle 是否为view模式(view模式时，view的高度必须大于80dp)
     * @param msg 加载时的提示信息
     */
    public void showLoading(boolean isViewStyle,String msg){
        if(isViewStyle) {
            View layout = iLoadingView.inflate(R.layout.loading);
            TextView textView = (TextView) layout.findViewById(R.id.tv_loading_text);
            if (!(msg.equals("") || msg != null)) {
                textView.setText(msg);
            }else{
                textView.setText("正在加载...");
            }
            iLoadingView.showLayout(layout);
        }else{
            iLoadingView.showLoading(msg);
        }
    }

    /**恢复布局界面(view模式)*/
    public void restore(boolean isViewStyle){
        if(isViewStyle) {
            iLoadingView.restoreView();
        }else{
            iLoadingView.dismissLoading();
        }
    }

}
