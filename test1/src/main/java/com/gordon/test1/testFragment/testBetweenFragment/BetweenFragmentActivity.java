package com.gordon.test1.testFragment.testBetweenFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.gordon.test1.R;

/**
 * Created by Administrator on 2016/5/12.
 */
public class BetweenFragmentActivity extends AppCompatActivity {

    protected FragmentManager fragmentManager;
    protected FragmentTransaction transaction;

    protected BetweenOneFragment oneFragment;
    protected BetweenTwoFragment twoFragment;

    public static final int ONE_CODE = 0X11;
    public static final int TWO_CODE = 0X12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sample);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        showWitchFragment(null,ONE_CODE);

    }

    public void showWitchFragment(Bundle bundle,int id){
        Fragment fragment = null;
        String tag = null;
        if(id == ONE_CODE){
            tag = BetweenOneFragment.class.getSimpleName();
            fragment = fragmentManager.findFragmentByTag(tag);
            if(fragment == null){
                fragment = new BetweenOneFragment();
            }
        }else if(id == TWO_CODE){
            tag = BetweenTwoFragment.class.getSimpleName();
            fragment = fragmentManager.findFragmentByTag(tag);
            if(fragment == null){
                fragment = new BetweenTwoFragment();
            }
        }
        fragment.setArguments(bundle);
        // 不能使用这样方式，transaction.replace(R.id.fl_singleFragment,fragment,tag).commit();
        // 如果使用上述方式：java.lang.IllegalStateException: commit already called
        // 该错误，是因为你的ft事务是全局的变量，只能commit一次。
        // 所以用局部ft事务去做commit即可。
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_singleFragment, fragment, tag).commit();
    }

}
