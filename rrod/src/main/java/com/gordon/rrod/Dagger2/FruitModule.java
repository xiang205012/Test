package com.gordon.rrod.Dagger2;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gordon on 2016/5/26.
 */

@Module // 注明这是个Module
public class FruitModule {

    @Provides
    Fruit provideFruit(){
        return new Apple("红色","小小的");
    }
    // 如果当业务变了时，改成橘子，直接返回new Orange("橙色","大的")
    // 这样其他的什么都不用改就可以达到效果

}
