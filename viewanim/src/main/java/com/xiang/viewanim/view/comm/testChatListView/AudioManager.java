package com.xiang.viewanim.view.comm.testChatListView;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.util.UUID;

/**
 * Created by Administrator on 2015/12/23.
 */
public class AudioManager {

    private static final String TAG = "AudioManager-->>";
    private MediaRecorder mediaRecorder;//录音类
    private String mDir;//录音文件目录
    private String currentFilePath;//当前录音文件路径
    private boolean isPrepare;//是否开始录音


    private static AudioManager mInstance;

    private AudioManager(String dir){this.mDir = dir;}

    private AudioStateListener listener;

    public void setAudioStateListener(AudioStateListener listener){
        this.listener = listener;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public interface AudioStateListener{
        //开始录音时回调（显示录音dialog）
        void wellPrepared();
    }

    public static AudioManager getmInstance(String dir){
        if(mInstance == null){
            synchronized (AudioManager.class){
                if(mInstance == null) mInstance = new AudioManager(dir);
            }
        }
        return mInstance;
    }

    /**准备录音*/
    public void prepareAudio(){
        try {
            isPrepare = false;
            File dir = new File(mDir);
            if(!dir.exists()) dir.mkdirs();
            String fileName = generateFileName();
            File file = new File(dir,fileName);
            currentFilePath = file.getAbsolutePath();

            mediaRecorder = new MediaRecorder();
            //设置输出文件
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            //设置mediaRecorder的音频源为麦克风
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频的格式(sdk10以上用AMB_NB,如果要兼容10以下用RAW_AMR_)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //设置音频的编码为amr
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isPrepare = true;
            if(listener != null) listener.wellPrepared();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "录音异常 ： " + e.getMessage());
        }


    }
    /**随机生成文件名*/
    private String generateFileName() {
        //音频后缀名为：amr
        return UUID.randomUUID().toString() + ".amr";
    }

    /**释放资源*/
    public void release(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    /**取消*/
    public void cancel(){
        release();
        if(currentFilePath != null){
            File file = new File(currentFilePath);
            file.delete();
            currentFilePath = null;
        }
    }

    /**获取音量等级*/
    public int getVoiceLevel(int level){
        if(isPrepare){
            try {
                //mediaRecorder.getMaxAmplitude()的范围的是1~32767
                //音量等级 1~7
                if(mediaRecorder != null) {
                    //一定要判空，因为在AudioButton中用子线程获取音量，但是主线程中有将mediaRecorder置为空的操作（如：Button up时）
                    return level * mediaRecorder.getMaxAmplitude() / 32768 + 1;
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }



}
