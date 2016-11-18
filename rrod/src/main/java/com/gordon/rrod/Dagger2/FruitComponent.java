package com.gordon.rrod.Dagger2;

import dagger.Component;

/**
 * Created by gordon on 2016/5/26.
 */

@Component(modules={FruitModule.class})// 将module连接起来
public interface FruitComponent { // Component必须是个接口类型

    void inject(Dagger2Activity activity);// 注入到activity


}
