package com.xiang.viewanim.view.comm.testChatListView;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.xiang.viewanim.R;
import com.xiang.viewanim.util.ScreenUtil;

/**
 * Created by Administrator on 2015/12/11.
 */
public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {

    private static final String TAG = "AudioButton--->>";
    private static final int STATE_NORMAL = 1;//默认状态
    private static final int STATE_RECORDING = 2;//正在录音状态
    private static final int STATE_WANT_TO_CANCEL = 3;//取消录音状态
    private int currentState = STATE_NORMAL;
    /**是否正在录音*/
    private boolean isRecording;//
    private static int DISTANCE_Y_CANCEL = -1;//Y方向移动的距离，离开了该控件的范围超过了该值
    private DialogManager dialogManager;
    private AudioManager audioManager;

    //handler 消息类型
    private static final int MSG_AUDIO_PREPARE = 0x11;//录音准备完成
    private static final int MSG_VOICE_CHANGE = 0x12;//音量变化
    private static final int MSG_DIALOG_DISMISS = 0x13;//dialog dismiss

    //录音记时
    private float time = 0;
    /**是否进入了OnLongClick事件，如果进入了就可能要关闭Audio和释放资源，没有就直接返回*/
    private boolean mReady;

    private OnAudioFinishRecorderListener mListener;

    /**录音完成后的回调（给activity回调）*/
    public interface OnAudioFinishRecorderListener{
        //参数：录音时间和录音文件的路径
        void onFinishRecord(float seconds, String filePath);
    }

    public void setOnAudioFinishRecorderListener(OnAudioFinishRecorderListener listener){
        this.mListener = listener;
    }

    public AudioRecorderButton(Context context) {
        this(context, null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DISTANCE_Y_CANCEL = ScreenUtil.dpTopx(context, 50);
        dialogManager = new DialogManager(context);
        String dir = Environment.getExternalStorageDirectory() + "/xiang/audio";
        audioManager = AudioManager.getmInstance(dir);
        audioManager.setAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //测试(正真显示应该在audio end prepared以后，就是录音准备完毕后(wellPrepared()方法里))
                //dialogManager.showRecordingDialog();
                //isRecording = true;
                mReady = true;
                audioManager.prepareAudio();
                Log.i(TAG, "Button长按了");
                return false;
            }
        });
    }

    /**获取音量等级*/
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while(isRecording){
                try {
                    Thread.sleep(100);//每隔0.1秒获取一次
                    time += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MSG_AUDIO_PREPARE:
                    dialogManager.showRecordingDialog();
                    isRecording = true;
                    //开启线程获取音量等级
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGE:
                    //更新dialog音量等级显示
                    dialogManager.updateVoiceLevel(audioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DISMISS:
                    dialogManager.dimissDialog();
                    break;
            }
        }
    };


    //录音准备完毕回调
    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                //如果一直是down就会触发OnLongClickListener
                //isRecording = true;
                //dialogManager.showRecordingDialog();
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                //move时，肯定是先按下了该控件
                Log.i(TAG, "进入move  isRecording"+isRecording);
                if(isRecording) {
                    Log.i(TAG, "进入move");
                    //根据x,y的坐标，判断是否想要取消录音(即判断x,y的坐标是否在该控件大小的范围内，不在就取消)
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                        Log.i(TAG, "move取消");
                    } else {
                        changeState(STATE_RECORDING);
                        Log.i(TAG, "move录音");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!mReady){//还没有准备好（就是没有进入OnLongClick事件）
                    reset();
                    return true;
                }
                if(!isRecording){//进入了OnLongClick事件但是AudioManager还没有prepare完成
                    dialogManager.tooShort();
                    audioManager.cancel();
                    mHandler.sendEmptyMessage(MSG_DIALOG_DISMISS);
                }else if(time < 0.6f){//录音时间过短，小于预定值 0.6s
                    dialogManager.tooShort();
                    audioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS,1200);
                }else if(currentState == STATE_RECORDING){
                    //正常结束
                    dialogManager.dimissDialog();
                    //release
                    audioManager.release();
                    //callbackToActivity
                    if(mListener != null){
                        mListener.onFinishRecord(time,audioManager.getCurrentFilePath());
                    }

                }else{
                    dialogManager.dimissDialog();
                    //cancel
                    audioManager.cancel();
                }
                reset();
                Log.i(TAG, "up恢复状态");
                break;
        }
        //必须返回true，否则只执行down而不会执行move和up
        boolean isTrue = super.onTouchEvent(event);
        Log.i(TAG,"isTrue:?  "+isTrue);
        //当返回super.onTouchEvent(evnet)时，虽然也是true，但不知为何会出现两个dialog
        //当指明true时就正常
        return true;
    }

    /**恢复状态和标志*/
    private void reset() {
        Log.i(TAG, "进入reset");
        isRecording = false;
        time = 0;
        mReady = false;
        changeState(STATE_NORMAL);
    }

    /**是否想取消，根据x和y在屏幕上的位置*/
    private boolean wantToCancel(int x, int y) {
        // x不在该控件所在范围内
        if(x < 0 || x > getWidth()){
            Log.i(TAG,"X : "+x);
            return true;
        }
        // y不在范围内，并且向下大于该控件的高度加上指定的值，向上小于指定的值
        if(y < -DISTANCE_Y_CANCEL || y > getHeight()+DISTANCE_Y_CANCEL){
            Log.i(TAG,"Y : "+y);
            return true;
        }

        return false;
    }

    private void changeState(int state) {
        //当前状态和设置的状态不同时才需要改变
        if(currentState != state){
            currentState = state;
            switch(state){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_say_vioce_normal);
                    setText(R.string.str_recorder_normal);
                    Log.i(TAG, "state normal : " );
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_say_vioce_recording);
                    setText(R.string.str_recorder_recording);
                    Log.i(TAG, "state recording : ");
                    if(isRecording){
                        //同时更新 dialog
                        dialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_say_vioce_recording);
                    setText(R.string.str_recorder_want_cancel);
                    Log.i(TAG, "state cancel : ");
                    //关闭dialog
                    dialogManager.wantToCancel();
                    break;
            }
        }

    }


}
