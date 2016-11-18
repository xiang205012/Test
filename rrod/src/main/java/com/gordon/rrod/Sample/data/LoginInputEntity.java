package com.gordon.rrod.Sample.data;

/**
 * Created by Steven on 16/3/28.
 */
public class LoginInputEntity {
    String tenantName;
    String userName;
    String password;
    String checkCode;
    String secretProtectCode;

    public LoginInputEntity(String tenantName, String userName, String password) {
        this.tenantName = tenantName;
        this.userName = userName;
        this.password = password;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getSecretProtectCode() {
        return secretProtectCode;
    }

    public void setSecretProtectCode(String secretProtectCode) {
        this.secretProtectCode = secretProtectCode;
    }
}
