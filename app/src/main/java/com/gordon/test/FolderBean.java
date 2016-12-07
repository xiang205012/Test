package com.gordon.test;

/**
 * Created by gordon on 2016/11/23 0023
 */

public class FolderBean {


    private String dirPath;
    private String imgPath;
    private String dirName;
    private String name;
    private int count;

    public void setCount(int count){
        this.count = count;
    }

    public int getCount(){
        return count;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
        int index = dirName.lastIndexOf("/");
        this.name = dirName.substring(index + 1,dirName.length());
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
