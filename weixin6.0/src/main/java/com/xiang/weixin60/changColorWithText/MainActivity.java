package com.xiang.weixin60.changColorWithText;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.xiang.weixin60.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewpager;
    private String[] titles = {"FirstFragment","SecondFragment","ThreeFragment","FourFragment"};
    private List<Fragment> fragmentList = new ArrayList<>();
    private MyViewPager adapter;
    private List<ChangeColorIconWithText> tabList = new ArrayList<ChangeColorIconWithText>();
    private ChangeColorIconWithText tab_chat;
    private ChangeColorIconWithText tab_contact;
    private ChangeColorIconWithText tab_found;
    private ChangeColorIconWithText tab_me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverflowButtonAlways();//将overflowButton一直显示(并且是自己设定的图标)
        getSupportActionBar().setDisplayShowHomeEnabled(false);//不显示最左边图标

        initView();

        initData();

        setViewPagerListener();
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tab_chat = (ChangeColorIconWithText) findViewById(R.id.tab_chat);
        tabList.add(tab_chat);
        tab_contact = (ChangeColorIconWithText) findViewById(R.id.tab_contact);
        tabList.add(tab_contact);
        tab_found = (ChangeColorIconWithText) findViewById(R.id.tab_found);
        tabList.add(tab_found);
        tab_me = (ChangeColorIconWithText) findViewById(R.id.tab_me);
        tabList.add(tab_me);

        tab_chat.setOnClickListener(this);
        tab_contact.setOnClickListener(this);
        tab_found.setOnClickListener(this);
        tab_me.setOnClickListener(this);
        //默认第一个是选中状态
        tab_chat.setBitmapAlpha(1.0f);
    }

    private void initData() {
        for(int i = 0; i < titles.length ;i++){
            TabFragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title",titles[i]);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        adapter = new MyViewPager(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setPageTransformer(true,new CustomPageTransformer());
    }
    /**设置滑动时tab颜色不断变化*/
    private void setViewPagerListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("TAG-->>", "position : " + position + "  滑动梯度0 ~ 1之间 ：" + positionOffset + "  具体滑动距离(像素值) ：" + positionOffsetPixels);
                //从第一页到第二页：position = 0  positionOffset: 0.0f ~ 1.0f
                //从第二页到第一页：position = 0  positionOffset: 1.0f ~ 0.0f
                //由此规律得到viewpager中的左右两个页面：left = position   right = position + 1
                //得到左右对应的tab
                if (positionOffset > 0) {//大于0表示滑动了
                    ChangeColorIconWithText left = tabList.get(position);
                    ChangeColorIconWithText right = tabList.get(position + 1);
                    //当滑动时left就相当于从第二页向第一页滑动，所以left的透明度是由小到大
                    left.setBitmapAlpha(1 - positionOffset);
                    right.setBitmapAlpha(positionOffset);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setOverflowButtonAlways(){
        try {
            ViewConfiguration configuration = ViewConfiguration.get(this);
            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(configuration,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置menu显示icon
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {

        if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
        {
            if (menu.getClass().getSimpleName().equals("MenuBuilder"))
            {
                try
                {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        resetAlpha();
        switch(v.getId()){
            case R.id.tab_chat:
                tabList.get(0).setBitmapAlpha(1.0f);
                //设置滑动时不要自带的动画，否则出现不正常显示
                viewpager.setCurrentItem(0,false);
                break;
            case R.id.tab_contact:
                tabList.get(1).setBitmapAlpha(1.0f);
                viewpager.setCurrentItem(1,false);
                break;
            case R.id.tab_found:
                tabList.get(2).setBitmapAlpha(1.0f);
                viewpager.setCurrentItem(2,false);
                break;
            case R.id.tab_me:
                tabList.get(3).setBitmapAlpha(1.0f);
                viewpager.setCurrentItem(3,false);
                break;
        }
    }

    /**点击时先清除所有tab的alpha值*/
    private void resetAlpha() {
        for(int i = 0; i < tabList.size(); i++){
            tabList.get(i).setBitmapAlpha(0);
        }
    }

    class MyViewPager extends FragmentPagerAdapter{

        public MyViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


}
