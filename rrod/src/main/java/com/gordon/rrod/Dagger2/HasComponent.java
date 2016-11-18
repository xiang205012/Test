package com.gordon.rrod.Dagger2;

/**
 * fragment通过此接口得到一个Component
 * fragment要获取Component是要在activity里面获取
 * Created by gordon on 2016/6/15.
 */
public interface HasComponent<C> {

    C getComponent();

}
