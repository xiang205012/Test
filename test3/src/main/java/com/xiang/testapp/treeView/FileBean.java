package com.xiang.testapp.treeView;

/**
 * Created by gordon on 2016/10/19.
 */

public class FileBean {

    @TreeNodeId
    private int id;

    @TreeNodePid
    private int pid;

    @TreeNodeLabel
    private String label;

    public FileBean(int id,int pid,String label){
        this.id = id;
        this.pid = pid;
        this.label = label;
    }




}
