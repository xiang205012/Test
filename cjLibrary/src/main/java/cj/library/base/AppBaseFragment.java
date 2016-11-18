package cj.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import cj.library.view.loadingview.LoadingViewController;
import de.greenrobot.event.EventBus;

/**
 * Created by cj on 2016/one/26.
 */
public abstract class AppBaseFragment extends Fragment{

    /**
     * 测试loadingView参考facediscern项目下mvp包
     */
    private LoadingViewController loadingViewController;
    /**fragment中要使用的上下文*/
    protected Context mContext;

    /**Scren information*/
    /**屏幕宽度*/
    protected int mScreenWidth = 0;
    /**屏幕高度*/
    protected int mScreedHeight = 0;
    /**屏幕像素密度*/
    protected float mScreenDensity = 0.0f;
    /**fragment是否可见*/
    private boolean isFirstVisible = true;
    private boolean isPrepare;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isBindEventBusHere()){
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    /**
     * fragment懒加载方法
     * setUserVisibleHint是在onCreateView之前调用的
     * 是否为用户可见时才加载
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(isFirstVisible){
                isFirstVisible = false;
                initPrepare();
            }
        }
    }

    private synchronized void initPrepare() {
        if(isPrepare){
            onFirstVisibleToUser();
        }else{
            isPrepare = true;
        }
    }

    /** 懒加载  加载数据，更新界面*/
    protected abstract void onFirstVisibleToUser();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getContentViewLayoutId() != 0){
            return inflater.inflate(getContentViewLayoutId(),null);
        }else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    /**onViewCreated在onCreateView执行完后立即执行。*/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化ButterKnife
        ButterKnife.inject(this, view);
        if(getLoadingTargetView() == null){
            //(简单加载对话框)，也可以通过setLayoutView来设置成全屏模式
            loadingViewController = new LoadingViewController();
        }else {
            //view模式
            loadingViewController = new LoadingViewController(getLoadingTargetView());
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreedHeight = displayMetrics.heightPixels;
        mScreenDensity = displayMetrics.density;
        //view初始化，数据加载或事件处理
        initViewAndEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isBindEventBusHere()){
            EventBus.getDefault().unregister(this);
        }
    }


    /**view初始化，数据加载或事件处理*/
    protected abstract void initViewAndEvents();

    /**需要加载数据后才能显示的view(如：return listview)*/
    protected abstract View getLoadingTargetView();

    /**布局文件id(R.layout.xxx)*/
    protected abstract int getContentViewLayoutId();

    /**是否需要使用EventBus*/
    protected abstract boolean isBindEventBusHere();

    /**
     * Toast信息
     * @param isLong 是否为长时间提示
     * @param msg    提示信息
     */
    protected void showToast(boolean isLong,String msg){
        if(!(msg.equals("") || msg != null)){
            if(isLong) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 显示或隐藏loadingView
     * @param toggle  是否显示
     * @param isViewStyle  是否为view模式
     * @param msg 提示信息
     */
    public void toggleShowLoading(boolean toggle,boolean isViewStyle,String msg){
        if(loadingViewController == null){
            throw new IllegalArgumentException("loadingViewController is null");
        }
        if(toggle){
            loadingViewController.showLoading(isViewStyle,msg);
        }else{
            loadingViewController.restore(isViewStyle);
        }
    }

    /**
     * 显示隐藏加载失败或错误的界面
     * @param toggle  是否显示
     * @param msg     提示信息
     * @param onClickListener  点击后可做重新加载操作
     *                         LinearLayout的id为：R.id.ll_message_error
     */
    public void toggleShowError(boolean toggle,String msg,View.OnClickListener onClickListener){
        if(loadingViewController == null){
            throw new IllegalArgumentException("loadingViewController is null");
        }
        if(toggle){
            loadingViewController.showError(msg,onClickListener);
        }else{
            loadingViewController.restore(true);
        }
    }

}
