package com.gordon.rrod.Dagger2;

import javax.inject.Inject;

/**
 * Created by gordon on 2016/5/26.
 *
 * 创建一个水果类
 */
public class Fruit {

    public String color;
    public String size;

    // @Inject一般都是标记构造方法和成员变量
    // Dagger2在关联时会根据成员变量的Inject先去Module中查找，
    // 如果没有就去该变量带有@Inject的构造方法
    @Inject
    public Fruit(){

    }



}
