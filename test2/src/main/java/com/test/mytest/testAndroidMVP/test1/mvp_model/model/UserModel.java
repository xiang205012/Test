package com.test.mytest.testAndroidMVP.test1.mvp_model.model;


import com.test.mytest.testAndroidMVP.test1.mvp_model.Imodel.IUser;

/**
 * mvp之Model层(数据模型)
 * Created by Administrator on 2016/1/19.
 */
public class UserModel implements IUser {

    private String userName;
    private String password;

    public UserModel(String userName,String password){
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String getName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int checkUserValidity(String name, String password) {
        if(name == null || password == null
                ||!name.equals(getName()) || !password.equals(getPassword())){
            return -1;
        }
        return 0;
    }
}
