package com.xiang.testapp.treeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/10/19.
 */

public class Node {

    private int id;
    private int pid;
    private String label;
    private int level;
    private int icon;
    private Node parent;
    private List<Node> childList = new ArrayList<>();
    private boolean isExpand;

    public Node(int id, int pid, String label) {
        this.id = id;
        this.pid = pid;
        this.label = label;
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

    public void setLabel(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public int getIcon(){
        return icon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildList() {
        return childList;
    }

    public void setChildList(List<Node> childList) {
        this.childList = childList;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
        if (!expand) {
            for (int i = 0; i < childList.size(); i++) {
                childList.get(i).setExpand(parent.isExpand());
            }
        }
    }

    public boolean isParent(){
        return parent == null;
    }

    public boolean isLeaf(){
        return childList.size() == 0;
    }

    public boolean isParentExpand(){
        if (parent == null) {
            return false;
        }
        return parent.isExpand();
    }
}
