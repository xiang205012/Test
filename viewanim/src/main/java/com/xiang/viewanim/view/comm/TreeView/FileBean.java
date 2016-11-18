package com.xiang.viewanim.view.comm.TreeView;

/**
 * 文件结构 如：
 *           FileDir文件夹
 *               |
 *  children文件   FileDir文件夹
 *                    |
 *          children     FileDir
 *                          |
 *               children       FileDir
 *                    ..............
 * Created by gordon on 2016/6/15.
 */
public class FileBean {

    /**当前id*/
    @TreeNodeId
    private int id;
    /**当前id的父id*/
    @TreeNodePid
    private int pid;

    /**标签*/
    @TreeNodeLabel
    private String label;

    /**信息*/
    private String desc;


    // ........其他属性

    public FileBean(int id,int pid,String name){
        this.id = id;
        this.pid = pid;
        this.label = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
