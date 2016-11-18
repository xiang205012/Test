package com.gordon.rrod.Dagger2;

import javax.inject.Inject;

/**
 * Created by gordon on 2016/5/26.
 *
 * 水果中的苹果
 */
public class Apple extends Fruit {

    @Inject
    public Apple(String color,String size){
        this.color = color;
        this.size = size;
    }

}
