package com.gordon.rrod.Sample.data;

/**
 * Created by Steven on 16/3/28.
 */
public class HttpResultDtoEntity<T> {
    boolean success;
    String message;
    T data;
    int total;
    RoleActionEntity roleActionEntity;
    String id;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public RoleActionEntity getRoleActionEntity() {
        return roleActionEntity;
    }

    public void setRoleActionEntity(RoleActionEntity roleActionEntity) {
        this.roleActionEntity = roleActionEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
