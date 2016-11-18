
仿聊天语音界面

AudioRecorderButton控制录音的按钮
    三种状态State: STATE_NORMAL, STATE_RECORDING, STATE_WANT_TO_CANCEL
           分别为： 默认状态，      录音状态，        取消录音状态

DialogManager:点击按钮录音时对话框控制类
    三种显示风格Style: RECORDING, WANT_TO_CANCEL, TOO_SHORT
              分别为： 录音时       取消时           录音时间太短

AudioManager:录音控制类
    prepare();准备   -> end prepared -> callback
    cancel();取消
    release();释放  -> callbackToActivity
    getVoiceLevel();-> 录音时的音量控制，开子线程


class AudioRecorderButton{

    onTouchEvent(){
        DOWN:
            changeButtonState(STATE_RECORDING);
            onLongClick -> AudioManger.prepare() -> end prepared
                             -> DialogManger.showDialog(RECORDING)

        MOVE:
            if(wantCancel(x,y){
                //如果x,y超出了设定范围，就取消录音
                DialogManager.showDialog(WANT_TO_CANCEL);
                //dialog显示取消发送时，button显示取消
                changeButtonState(STATE_WANT_TO_CANCEL);
            }else{
                DialogManager.showDialog(RECORDING);
                changerButtonState(STATE_RECORDING);
            }

        UP:
            if(wantCancel == currentState){
                AudioManager.cancel();
            }else if(STATE_RECORDING == currentState){
                AudioManager.release();
                callBackToActivity(url,time);录音的完整路径和时长
            }
        return true;
    }
}













