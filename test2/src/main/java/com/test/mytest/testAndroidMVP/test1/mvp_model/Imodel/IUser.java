package com.test.mytest.testAndroidMVP.test1.mvp_model.Imodel;

/**
 * 数据模型操作接口，避免view层直接操作model
 * Created by Administrator on 2016/1/19.
 */
public interface IUser {
    /**
     * 此接口公布对User对象的一些数据操作接口
     */

    String getName();

    String getPassword();

    /**检查是否为当前user*/
    int checkUserValidity(String name, String password);



}



