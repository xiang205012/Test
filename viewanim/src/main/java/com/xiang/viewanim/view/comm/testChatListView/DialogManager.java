package com.xiang.viewanim.view.comm.testChatListView;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiang.viewanim.R;


/**
 * Created by Administrator on 2015/12/23.
 */
public class DialogManager {

    private static final String TAG = "DialogManager-->>";
    private Dialog mDialog;
    private ImageView iv_voice;//话筒
    private ImageView iv_level;//音量
    private TextView mLable;//变化的文字
    private Context mContext;

    public DialogManager(Context context){
        this.mContext = context;
    }

    /**显示录音的对话框*/
    public void showRecordingDialog(){
        mDialog = new Dialog(mContext, R.style.Theme_Autio_Dialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.audio_dialog,null);
        mDialog.setContentView(view);

        iv_voice = (ImageView) mDialog.findViewById(R.id.iv_recorder);
        iv_level = (ImageView) mDialog.findViewById(R.id.iv_voice_level);
        mLable = (TextView) mDialog.findViewById(R.id.tv_voice_title);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    /**正在录音的对话框*/
    public void recording(){
        if(mDialog != null && mDialog.isShowing()){
            iv_voice.setVisibility(View.VISIBLE);
            iv_level.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);
            iv_voice.setBackgroundResource(R.drawable.recorder);
            mLable.setText(R.string.str_recorder_recording);
        }
    }

    /**显示取消录音的对话框*/
    public void wantToCancel(){
        if(mDialog != null && mDialog.isShowing()){
            iv_voice.setVisibility(View.VISIBLE);
            iv_level.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);
            iv_voice.setBackgroundResource(R.drawable.cancel);
            mLable.setText(R.string.str_recorder_want_cancel);
        }
    }

    /**显示录音时间过短的对话框*/
    public void tooShort(){
        if(mDialog != null && mDialog.isShowing()){
            iv_voice.setVisibility(View.VISIBLE);
            iv_level.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);
            iv_voice.setBackgroundResource(R.drawable.voice_to_short);
            mLable.setText(R.string.str_recorder_to_short);
        }
    }

    /**关闭对话框*/
    public void dimissDialog(){
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 更新音量等级变化
     * @param level 音量等级(1 ~ 7)
     */
    public void updateVoiceLevel(int level){
        if(mDialog != null && mDialog.isShowing()){
//            iv_voice.setVisibility(View.VISIBLE);
//            iv_level.setVisibility(View.VISIBLE);
//            mLable.setVisibility(View.VISIBLE);
            //快捷方式获取drawable目录下名为 v 开头的文件ID
            int resId = mContext.getResources().getIdentifier("v" + level,"drawable",mContext.getPackageName());
//            iv_level.setImageResource(resId);
            iv_level.setBackgroundResource(resId);
            Log.i(TAG, "当前音量是: " + resId);
        }
    }


}
