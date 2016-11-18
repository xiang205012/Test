package com.xiang.viewanim.view.comm.testChatListView;

/**
 * 录音对象，记录录音的时长和录音文件的路径
 * Created by Administrator on 2015/12/27.
 */
public class Recorder {

    private float time;
    private String filePath;

    public Recorder(float time, String filePath) {
        this.time = time;
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
