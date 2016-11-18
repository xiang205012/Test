package cj.library.view.loadingview;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cj.library.R;


/**
 * 界面布局和加载提示布局切换
 * Created by cj on 2016/one/26.
 */
public class LoadingViewHelper implements ILoadingView{
    /**每个界面的根视图*/
    private View view;
    private ViewGroup parentView;
    private int viewIndex;
    private ViewGroup.LayoutParams params;
    /**当前视图*/
    private View currentView;

    private ProgressDialog loadingDialog;

    public LoadingViewHelper(){

    }

    public LoadingViewHelper(View view){
        super();
        this.view = view;
    }

    @Override
    public View getCurrentLayout() {
        return currentView;
    }

    @Override
    public void restoreView() {
        showLayout(view);
    }

    /**显示和隐藏loadingView*/
    @Override
    public void showLayout(View loadView) {
        if(parentView == null){
            init();
        }
        this.currentView = loadView;
        // 如果已经是那个view，那就不需要再进行替换操作了
        // 判断界面布局view和传进来的提示view是否同一个，不是同一个就显示提示view
        // init方法把界面布局view置为当前显示的currentView，
        // showLayout方法是将传入的view置为当前显示的view
        if(parentView.getChildAt(viewIndex) != loadView){
            ViewGroup parent = (ViewGroup) loadView.getParent();
            if(parent != null){
                parent.removeView(loadView);
            }
            parentView.removeViewAt(viewIndex);
            parentView.addView(loadView,viewIndex,params);
        }
    }

    private void init(){
        params = view.getLayoutParams();
        if(view.getParent() != null){
            parentView = (ViewGroup) view.getParent();
        }else{
            parentView = (ViewGroup) view.getRootView().findViewById(android.R.id.content);
        }
        int count = parentView.getChildCount();
        for(int index = 0; index < count; index++){
            if(view == parentView.getChildAt(index)){
                viewIndex = index;
                break;
            }
        }
        currentView = view;
    }

    @Override
    public View inflate(int layoutId) {
        return LayoutInflater.from(view.getContext()).inflate(layoutId,null);
    }

    @Override
    public Context getContext() {
        return view.getContext();
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void showLoading(String msg) {
        if(loadingDialog == null) {
            loadingDialog = new ProgressDialog(getContext());
            loadingDialog.setIndeterminate(true);
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingDialog.setIndeterminateDrawable(getContext().getResources().getDrawable(R.drawable.loading_progressbar));
        }
        if(msg.equals("") || msg == null) {
            loadingDialog.setMessage("正在加载...");
        }else{
            loadingDialog.setMessage(msg);
        }
        loadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }
}
