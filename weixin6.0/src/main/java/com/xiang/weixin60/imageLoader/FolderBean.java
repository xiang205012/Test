package com.xiang.weixin60.imageLoader;

/**
 * ListView的Item所需的变量
 * Created by Administrator on 2016/5/5.
 */
public class FolderBean {

    /**当前文件夹的路径*/
    private String dir;
    /**当前文件夹的名称*/
    private String name;
    /**当前文件夹下的第一张图片的路径*/
    private String firstImgPath;
    /**当前文件夹下的文件数量*/
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        // sdcard/images/photo : photo
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
