package cj.library.view.comm;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带菜单选项的Layout，具体用法看最后
 * Created by gordon on 2016/9/13.
 */
public class SelectMenuLayout extends RelativeLayout {

    /**所有菜单项*/
    private List<TextView> menuItems = new ArrayList<>();
    /**所有菜单项文字*/
    private List<String> menuTitles = new ArrayList<>();
    /**所有菜单项所在的父容器*/
    private LinearLayout menuLayout;
    /**弹出框所在的父容器*/
    private FrameLayout framePopLayout;
    /**主布局所在的父容器*/
    private FrameLayout contentMainLayout;

    private final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private int textSize = 16;

    private int textColor = Color.parseColor("#786786");

    private int textSelectColor = Color.parseColor("#000000");

    private int popMainLayoutHeight = pxTodp(250);

    private boolean isPopVisible = false;

    private OnMenuSelectListener mListener;

    public void setOnMenuSelectListener(OnMenuSelectListener listener){
        this.mListener = listener;
    }

    public interface OnMenuSelectListener{
        View onMenuSelect(int position, String menuTitle);
    }

    public SelectMenuLayout(Context context) {
        this(context,null);
    }

    public SelectMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();

    }

    public void initView() {

        LayoutParams menuLayoutParmas = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,pxTodp(50));
        menuLayout = new LinearLayout(getContext());
        menuLayout.setId(createViewId());
        menuLayout.setOrientation(LinearLayout.HORIZONTAL);
        menuLayout.setGravity(Gravity.CENTER);
        menuLayout.setLayoutParams(menuLayoutParmas);
        addView(menuLayout);

        LayoutParams frameContentLayoutParmas = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        contentMainLayout = new MainContentFrameLayout(getContext());
        contentMainLayout.setId(createViewId());
        contentMainLayout.setBackgroundColor(Color.WHITE);
        frameContentLayoutParmas.addRule(RelativeLayout.BELOW,menuLayout.getId());
        contentMainLayout.setLayoutParams(frameContentLayoutParmas);
        addView(contentMainLayout);

        LayoutParams framePopLayoutParmas = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,popMainLayoutHeight);
        framePopLayout = new FrameLayout(getContext());
        framePopLayout.setId(createViewId());
        framePopLayout.setBackgroundColor(Color.BLACK);
        framePopLayout.setAlpha(0.5f);
        framePopLayoutParmas.addRule(RelativeLayout.BELOW,menuLayout.getId());
        framePopLayout.setLayoutParams(framePopLayoutParmas);
        framePopLayout.setVisibility(GONE);
        addView(framePopLayout);

    }

    /**创建菜单项*/
    public void createMenuItems(final List<String> itemTitles){
        if (itemTitles.size() <= 0) {
            throw new IllegalArgumentException("itemTitles is null");
        }
        menuTitles.clear();
        menuTitles.addAll(itemTitles);
        for (int i = 0; i < itemTitles.size(); i++) {
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tvParams.weight = 1;
            TextView textView = new TextView(getContext());
            textView.setTextSize(textSize);
            textView.setTextColor(textColor);
            textView.setText(itemTitles.get(i));
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(tvParams);
            menuItems.add(textView);
            menuLayout.addView(textView);

        }
        setMenuItemClickListener();
    }

    private void setMenuItemClickListener() {
        if(menuItems.size() > 0){
            for (int i = 0; i < menuItems.size(); i++) {
                final int position = i;
                TextView textView = menuItems.get(i);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeMenuItemsColor(position);
                        framePopLayout.getLayoutParams().height = popMainLayoutHeight;
                        if(mListener != null){
                            View view = mListener.onMenuSelect(position,menuTitles.get(position));
                            if (view != null) {
                                setPopContentView(view);
                            }
                        }
                    }
                });
            }
        }

    }

    private void changeMenuItemsColor(int position) {
        for (int i = 0; i < menuItems.size(); i++) {
            TextView textView = menuItems.get(i);
            if(position == i){
                textView.setTextColor(textSelectColor);
            }else {
                textView.setTextColor(textColor);
            }
        }
    }

    public void setContentMainView(View view){
        contentMainLayout.addView(view);
    }

    /**传入弹出的view,不需要直接使用，这里是通过设置监听返回view即可*/
    private void setPopContentView(View view){
        isPopVisible = true;
        framePopLayout.removeAllViews();
        framePopLayout.addView(view);
        framePopLayout.setVisibility(VISIBLE);
    }

    /**隐藏弹出框*/
    public void dismissPopContentView(){
        if (isPopVisible) {
            framePopLayout.removeAllViews();
            framePopLayout.setVisibility(GONE);
            isPopVisible = false;
            changeMenuItemsColor(-1);
        }
    }

    public boolean isShowing(){
        return framePopLayout.getVisibility() == VISIBLE;
    }

    public void setMenuTextSize(int size){
        this.textSize = size;
    }

    public void setMenuTextColor(int color){
        this.textColor = color;
    }

    public void setMenuTextSelectColor(int selectColor){
        this.textSelectColor = selectColor;
    }

    public void setPopMainLayoutHeight(int height){
        framePopLayout.getLayoutParams().height = height;
    }

    private int pxTodp(int px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,px,getResources().getDisplayMetrics());
    }

    private int createViewId(){
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


    private class MainContentFrameLayout extends FrameLayout{

        public MainContentFrameLayout(Context context) {
            this(context,null);
        }

        public MainContentFrameLayout(Context context, AttributeSet attrs) {
            this(context, attrs,0);
        }

        public MainContentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (isPopVisible) {
                dismissPopContentView();
                return true;
            }
            return super.onInterceptTouchEvent(ev);
        }
    }


    public void destory(){
        menuItems.clear();
        menuTitles.clear();
        menuLayout.removeAllViews();
        menuLayout = null;
        framePopLayout.removeAllViews();
        framePopLayout = null;
        contentMainLayout.removeAllViews();
        contentMainLayout = null;
        this.removeAllViews();
    }

}

/**
 * private SelectMenuLayout selectMenuView;

 private int count = 1;

 @Override
 protected void onCreate(@Nullable Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_select_menu);

 selectMenuView = (SelectMenuLayout) findViewById(R.id.select_menu);

 selectMenuView.initView();

 List<String> menuTitles = new ArrayList<>();
 menuTitles.add("java");
 menuTitles.add("php");
 menuTitles.add("objectc");
 menuTitles.add("mysql");
 menuTitles.add("android");

 selectMenuView.createMenuItems(menuTitles);

 RelativeLayout relativeLayout = new RelativeLayout(this);
 RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
 ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
 relativeLayout.setLayoutParams(params);

 Button button = new Button(this);
 RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
 ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
 btnParams.addRule(RelativeLayout.CENTER_IN_PARENT);
 button.setText("隐藏弹出框");
 button.setTextSize(30);
 button.setPadding(8,8,8,8);
 button.setLayoutParams(btnParams);
 relativeLayout.addView(button);

 button.setOnClickListener(new View.OnClickListener() {
 @Override
 public void onClick(View v) {
 Log.d("tag-->>","button click");
 }
 });

 selectMenuView.setContentMainView(relativeLayout);

 selectMenuView.setOnMenuSelectListener(new SelectMenuLayout.OnMenuSelectListener() {
 @Override
 public View onMenuSelect(int position, String menuTitle) {
 final RelativeLayout relativeLayout2 = new RelativeLayout(SelectMenuActivity.this);
 relativeLayout2.setBackgroundColor(Color.WHITE);
 RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
 ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
 relativeLayout2.setLayoutParams(params2);

 Button button2 = new Button(SelectMenuActivity.this);
 RelativeLayout.LayoutParams btnParams2 = new RelativeLayout.LayoutParams(
 ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
 btnParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
 button2.setText("弹出框上的按钮  " + count);
 button2.setTextSize(30);
 button2.setPadding(18,18,18,18);
 button2.setLayoutParams(btnParams2);
 relativeLayout2.addView(button2);
 if(count == 3){
 selectMenuView.setPopMainLayoutHeight(50);
 }
 count++;
 return relativeLayout2;
 }
 });

 }

 @Override
 public void onBackPressed() {
 if(selectMenuView.isShowing()){
 selectMenuView.dismissPopContentView();
 return;
 }
 super.onBackPressed();
 Log.d("tag-->>","onBackPressed");
 selectMenuView.destory();
 }
 }


 activity 的布局：
 <?xml version="1.0" encoding="utf-8"?>
 <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
 android:orientation="vertical"
 android:layout_width="match_parent"
 android:layout_height="match_parent">

 <com.xiang.test.testSelectMenu.SelectMenuLayout
 android:id="@+id/select_menu"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 />

 </LinearLayout>


 */

