package com.gordon.rrod.Dagger2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gordon.rrod.R;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by gordon on 2016/5/26
 */
public class Dagger2Activity extends AppCompatActivity implements HasComponent<TestHasComponet> {

    @Inject
    Fruit fruit;//注意写完这个时是不会有DaggerFruitComponent的，必须编译过后才有,Build-->>Rebuild Project

//    @BindViews({R.id.tv_dagger_type,R.id.tv_dagger_type2})
//    TextView tv_type,tv_type2;

    TestHasComponet testHasComponet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger2);

        ButterKnife.bind(this);
        initComponent();

        Log.d("dagger2输出 ",fruit.color+"    "+fruit.size);

        Log.d("dagger2","-----------------------------");

        initTestHasComponent();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Dagger2Fragment fragment = new Dagger2Fragment();
        transaction.add(R.id.test_hasComponent,fragment).commit();

    }

    private void initTestHasComponent() {
        // 创建一个连接池对象
        testHasComponet = DaggerTestHasComponet.builder()
                .activityModule(new ActivityModule(this))
                .fruitModule(new FruitModule())
                .build();
    }

    private void initComponent() {
        // 创建并注入
        DaggerFruitComponent.create().inject(this);
    }

    @Override
    public TestHasComponet getComponent() {
        return testHasComponet;
    }
}
