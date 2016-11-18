package com.test.mytest.testObserver;

import java.util.Observable;

/**
 * 被观察者（数据有变化时通知MyObserver）
 * Created by Administrator on 2016/1/22.
 */
public class MyObservable extends Observable{

    private static MyObservable observable;

    public static MyObservable getInstance(){
        if(observable == null){
            observable = new MyObservable();
        }
        return observable;
    }

    public void notifyDataChange(Number number){
        //被观察者怎么通知观察者数据有改变了呢？？这里的两个方法是关键。
        setChanged();
        notifyObservers(number);
    }

}
