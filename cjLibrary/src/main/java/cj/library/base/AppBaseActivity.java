package cj.library.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import cj.library.R;
import cj.library.http.RequestManager;
import cj.library.manager.AppManager;
import cj.library.utils.NetStateUtil;
import cj.library.utils.SmartBarUtils;
import cj.library.utils.ToastUtil;
import cj.library.view.loadingview.LoadingViewController;
import de.greenrobot.event.EventBus;

/**
 * Created by cj on 2015/11/24.
 */
public abstract class AppBaseActivity extends AppCompatActivity {

    protected Activity mActivity;

    /**Scren information*/
    /**屏幕宽度*/
    protected int mScreenWidth = 0;
    /**屏幕高度*/
    protected int mScreedHeight = 0;
    /**屏幕像素密度*/
    protected float mScreenDensity = 0.0f;

    /**异步消息处理线程*/
    protected HandlerThread mHandlerThread;
    /**后台消息处理的Handler*/
    protected BackgroundHandler mBackgroundHandler;

    /**loadingView*/
    protected LoadingViewController loadingViewController = null;

    /**Activity之间跳转的方式*/
    public enum TransitionMode{
        /**当前activity从左边进入，上一个activity往右边退出*/
        LEFT,
        /**当前activity从右边进入，上一个activity往左边退出*/
        RIGHT,
        /**当前activity从上进入，上一个activity往下退出*/
        TOP,
        /**当前activity从下进入，上一个activity往上退出*/
        BOTTOM,
        /**当前activity放大进入，上一个activity缩小退出*/
        SCALE,
        /**当前activity 透明度渐渐变小进入，上一个activity透明度渐渐变大退出*/
        FADE
    }

    /** 自定义异步消息处理类，用于处理后台消息 */
    protected class BackgroundHandler extends Handler {
        public BackgroundHandler(Looper looper){
            super(looper);
        }
        /**消息处理*/
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleBackgroundMessage(msg);
        }
    }

    /** UI线程的handler */
    protected Handler mUiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleUiMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(toggleOverridePendingTrasition()){
            switch(getOverridePendingTrasitionMode()){
                case LEFT:
                    // 当前activity执行left_in  上一个activity执行right_out
//                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    break;
                case RIGHT:
                    // 当前activity执行left_in  上一个activity执行right_out
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in,R.anim.bottom_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in,R.anim.top_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in,R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        mActivity = this;
        if(isBindEventBusHere()){
            EventBus.getDefault().register(this);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreedHeight = displayMetrics.heightPixels;
        mScreenDensity = displayMetrics.density;
        SmartBarUtils.hide(getWindow().getDecorView());
        setTranslucentStatus(isApplyStatusBarTranslucency());
        if(getContentViewLayoutID() != 0){
            setContentView(getContentViewLayoutID());
        }else{
            throw new IllegalArgumentException("You must return a right contentView layout resource id");
        }
        //初始化网络请求管理器
        RequestManager.init(this);
        //管理每个activity
        AppManager.getAppManager().addActivity(this);

        // 初始化后台消息处理类
        mHandlerThread = new HandlerThread("app base activity :"+getClass().getSimpleName());
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());



        //view初始化，数据加载或事件处理
        initViewsAndEvents();

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
        if(getLoadingTargetView() != null){
            loadingViewController = new LoadingViewController(getLoadingTargetView());
        }else{
            loadingViewController = new LoadingViewController();
        }
    }

    /**activity之间跳转是否要动画显示*/
    protected abstract boolean toggleOverridePendingTrasition();

    /**activity之间跳转动画的方式*/
    protected abstract TransitionMode getOverridePendingTrasitionMode();

    /**返回需要加载才能显示的那个view*/
    protected abstract View getLoadingTargetView();

    /**view初始化，数据加载或事件处理*/
    protected abstract void initViewsAndEvents();

    /**是否需要使用EventBus*/
    protected abstract boolean isBindEventBusHere();

    /**布局文件的id（R.layout.xxx）*/
    protected abstract int getContentViewLayoutID();

    /**状态栏是否为半透明形式*/
    protected abstract boolean isApplyStatusBarTranslucency();


    /**处理后台操作*/
    protected void handleBackgroundMessage(Message msg){};
    /**发送后台消息 sendMessage()允许你处理Message对象(Message里可以包含数据)*/
    protected void sendBackgroundMessage(Message msg){
        mBackgroundHandler.sendMessage(msg);
    }
    /**发送后台消息 sendEmptyMessage()只能放数据*/
    protected void sendEmptyBackgroundMessage(int what){
        mBackgroundHandler.sendEmptyMessage(what);
    }

    /**处理UI线程的消息*/
    protected void handleUiMessage(Message msg){}
    /**发送消息给UI线程*/
    protected void sendUiMessage(Message msg){
        mUiHandler.sendMessage(msg);
    }
    /**发送消息给UI线程*/
    protected void sendEmptyMessage(int what){
        mUiHandler.sendEmptyMessage(what);
    }


//    BackgroundHandler 和 UIHandler使用示例
// /**
//     * 执行工作
//     * @param msg显示加载信息
//     * @param taskId需要执行工作的编号
//     * @param obj可选参数
//     */
//    public void doWorking(String msg, int taskId, Object obj) {
//        if (msg.length() > 0) {
//            loadingWorker.showLoading(msg);
//        }
//        Message taskMsg = new Message();
//        taskMsg.what = taskId;
//        taskMsg.obj = obj;
//        mBackgroundHandler.sendMessage(taskMsg);
//    }
//    /*
//         * 后端线程
//         */
//    @Override
//    protected void handleBackgroundMessage(Message msg) {
//
//        Message resultMsg = new Message();
//        resultMsg.what = msg.what;
//        switch (msg.what) {
//            case TASK_EVENT_SHUTTER:
//                  .....
//                break;
//            case TASK_EVENT_THUMBNAIL:
//                  .....
//                break;
//        }
//        mUiHandler.sendMessage(resultMsg);
//    }
//
//    /*
//	 * UI线程
//	 */
//    @Override
//    protected void handleUiMessage(Message msg) {
//        super.handleUiMessage(msg);
//        switch (msg.what) {
//            case TASK_EVENT_SHUTTER:
//                mCameraShutterButton.setClickable(false);
//                mContainer.takePicture(this);
//                break;
//            case TASK_EVENT_THUMBNAIL:
//                finish();
//                break;
//        }
//    }

//        String url = "http://www.baidu.com";
//        executeRequest(
//                new StringRequest(Request.Method.GET, url,
//                        responseListener(), errorHttpListener()));
//        executeRequest(new StringRequest(Method.POST, VolleyApi.POST_TEST,
//                responseListener(),
//                errorListener()) {
//            重写getParams()返回值为：需要提交的参数
//            protected Map<String, String> getParams() {
//                return new ApiParams().with("param1", "02").with("param2", "14");
//            }
//        });
//    private Response.Listener<String> responseListener() {
//        return new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //mTvResult.setText(response);
//                Log.i("TAG", response);
//            }
//        };
//    }

    protected void executeRequest(Request<?> request) {
        //设置连接超时时间 10s
        request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RequestManager.cancelAllRequest(mActivity);//断开连接
                if(!NetStateUtil.checkNet(mActivity)){//没有活动的网络
//                    showDialog();弹出网络设置对话框
                }
                Toast.makeText(mActivity,mActivity.getResources().getString(R.string.httpError) , Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * 销毁当前activity并从堆栈中移除
     */
    protected void finishActivity(){
        AppManager.getAppManager().finishActivity();
    }

    @Override
    protected void onStop() {
        //取消所有网络请求
        RequestManager.cancelAllRequest(mActivity);
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
        AppManager.getAppManager().finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        if(isBindEventBusHere()){
            EventBus.getDefault().unregister(this);
        }
        // 退出后台线程的handler
        if(mBackgroundHandler != null && mBackgroundHandler.getLooper() != null){
            mBackgroundHandler.getLooper().quit();
        }
    }

    /**
     * activity之间不带参数的跳转
     * @param clazz 指定activity
     */
    protected void readyGo(Class<?> clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }
    /**
     * activity之间带参数的跳转
     * @param clazz 指定activity
     */
    protected void readyGo(Class<?> clazz,Bundle bundle){
        Intent intent = new Intent(this,clazz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 不带参数
     * activity跳转后finish当前的activity
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * 带参数
     * activity跳转后finish当前的activity
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * toast提示
     * @param isLong 是否为长时间提示
     * @param msg 提示信息
     */
    protected void showToast(boolean isLong,String msg){
        if(isLong){
            ToastUtil.longShow(this,msg);
        }else{
            ToastUtil.show(this, msg);
        }
    }

    /**
     * 加载界面或加载对话框的显示和隐藏
     * @param toggle  是否显示
     * @param isViewStyle 是否为view模式
     * @param msg 加载中的提示信息
     */
    protected void toggleShowLoading(boolean toggle,boolean isViewStyle,String msg){
        if(loadingViewController == null){
            throw new IllegalArgumentException("You must return a right target view for loading");
        }
        if(toggle){
            loadingViewController.showLoading(isViewStyle, msg);
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
    protected void toggleShowError(boolean toggle,String msg,View.OnClickListener onClickListener){
        if(loadingViewController == null){
            throw new IllegalArgumentException("You must return a right target view for loading");
        }
        if(toggle){
            loadingViewController.showError(msg, onClickListener);
        }else{
            loadingViewController.restore(true);
        }
    }

    /**
     * use SytemBarTintManager
     * 使用并设置沉侵式状态栏的颜色(一般与toolbar颜色一样)(将颜色转为drawable)
     * 如：新建drawables.xml可用于将颜色转为drawable
     * <drawable name="sr_primary">@color/sr_color_primary</drawable>
     * @param tintDrawable
     */
    protected void setSystemBarTintDrawable(Drawable tintDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            if (tintDrawable != null) {
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintDrawable(tintDrawable);
            } else {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }

    }

    /**
     * 是否设置状态栏为半透明的形式
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

}
