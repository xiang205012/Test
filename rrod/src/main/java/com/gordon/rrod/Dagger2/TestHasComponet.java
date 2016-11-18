package com.gordon.rrod.Dagger2;

import com.gordon.rrod.Sample.custom.PerActivity;

import dagger.Component;

/**
 * Created by gordon on 2016/6/15.
 */
@PerActivity
@Component(modules = {FruitModule.class ,ActivityModule.class})
public interface TestHasComponet {

    void inject(Dagger2Fragment dagger2Fragment);

}
