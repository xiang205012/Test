package com.test.mytest.testORMLite;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.mytest.R;

/**
 * Created by Administrator on 2015/9/11.
 */
public class ORMLiteActivity extends FragmentActivity{

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private EditFragment editFragment;
    private SelectorFragment selectorFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ormlite_actiivty);
        ViewUtils.inject(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        editFragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("address",null);
        editFragment.setArguments(bundle);
        /**
         * 在使用Fragment保存参数的时候，可能是因为需要保存的参数比较大或者比较多，这种情况下页会引起异常。
         * 比如代码
         * b.putParcelable("bitmap", bitmap2);
           imageRecognitionFragment.setArguments(b);
         设置好参数，并且添加hide(),add(),方法之后，需要commit()，来实现两个Fragment跳转的时候，这种情形下参数需要进行系统保存，
         但是这个时候你已经实现了跳转，系统参数却没有保存。此时就会报
         java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
         异常。分析原因：你并不需要系统保存的参数，只要你自己设置的参数能够传递过去，在另外一个Fragment里能够顺利接受就行了，
         现在android里提供了另外一种形式的提交方式commitAllowingStateLoss()，
         从名字上就能看出，这种提交是允许状态值丢失的。到此问题得到完美解决，值的传递是你自己控制的。
         */
        fragmentTransaction.add(R.id.address_content,editFragment,null).commitAllowingStateLoss();





    }

}
