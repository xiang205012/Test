package com.gordon.rrod.Dagger2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * Created by gordon on 2016/6/15.
 */
public class Dagger2Fragment extends Dagger2BaseFragment {

    @Inject
    Fruit fruit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(TestHasComponet.class).inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("testHasComponent",fruit.color+"   "+fruit.size);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
