package com.xiang.viewanim.view.comm.TreeView;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点
 * Created by gordon on 2016/6/18.
 */
public class Node {

    /**当前节点的id*/
    private int id;
    /**跟节点的pid为0(一般从1开始)*/
    private int pid = 0;
    /**当前节点的名称*/
    private String name;
    /**当前节点的层级*/
    private int level;
    /**是否展开状态*/
    private boolean isExpand;
    /**状态图标*/
    private int icon;
    /**当前节点的父节点*/
    private Node parent;
    /**所有子节点*/
    private List<Node> children = new ArrayList<>();

    /**你自己的真实数据*/
    private Object object;

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public Node(int id,int pid,String label){
        this.id = id;
        this.pid = pid;
        this.name = label;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**获取当前节点的层级*/
    public int getLevel() {
        // parent是空说明当前节点是根节点，
        // 否则通过parent.getLevel得到父节点的层级再加1得到该节点的层级
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
        // 如果当前节点是父节点，而且是要将该父节点设置为关闭展开的状态，
        // 此时应该将其所有的字节点也关闭
        if(!expand){
            for(Node node : children){
                node.setExpand(expand);
            }
        }
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    /**判断当前节点是否是根节点*/
    public boolean isRoot(){
        return parent == null;
    }

    /**判断当前父节点是否展开*/
    public boolean isParentExpand(){
        if(parent == null){
            return false;
        }
        return parent.isExpand();
    }

    /**判断当前节点是否是叶子节点*/
    public boolean isLeaf(){
        return children.size() == 0;
    }



}
