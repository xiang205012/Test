package com.test.mytest.imageLoader;

import android.util.Log;

/**
 * Created by Administrator on 2016/11/15 0015.
 */

public class FolderBean {
    /**当前文件夹的路径*/
    private String dir;
    /**当前文件夹下第一张图片*/
    private String firstImgPath;
    /**文件夹名*/
    private String name;
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
        Log.d("tag"," dir : " + dir
                + " \nlastIndexOf : " + lastIndexOf
                + "  \nname : " + name);
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
