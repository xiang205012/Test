package com.test.mytest.testChatListView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by Administrator on 2015/12/27.
 */
public class MediaPlayerManager {

    private static final String TAG = "MediaPlayer-->>";
    public static MediaPlayer player;
    /**是否为暂停状态*/
    public static boolean isPause;

    public static void playSound(String filePath,MediaPlayer.OnCompletionListener listener) {
        if(player == null){
            player = new MediaPlayer();
        }else{
            player.reset();
        }
        try {
            //设置播放类型
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置播放监听
            player.setOnCompletionListener(listener);
            //设置播放源
            player.setDataSource(filePath);
            //准备播放
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "播放时错误 ：" + e.getMessage());
        }

    }

    /**暂停时*/
    public static void pause(){
        if(player != null && player.isPlaying()){
            player.pause();
            isPause = true;
        }
    }

    /**恢复时*/
    public static void resume(){
        if(player != null && isPause){
            player.start();
            isPause = false;
        }
    }

    /**释放*/
    public static void release(){
        if(player != null){
            player.release();
            player = null;
        }
    }


}
