package com.xiang.viewanim.view.comm.TreeView;

/**
 * 公司的人员结构
 * Created by gordon on 2016/6/15.
 */
public class OrgBean {

    @TreeNodeId
    private int _id;

    @TreeNodePid
    private int _pid;

    @TreeNodeLabel
    private String name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_pid() {
        return _pid;
    }

    public void set_pid(int _pid) {
        this._pid = _pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
