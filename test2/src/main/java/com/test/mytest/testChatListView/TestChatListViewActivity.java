package com.test.mytest.testChatListView;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/11.
 */
public class TestChatListViewActivity extends Activity {

    private ListView listview;
    private AudioRecorderButton audioButton;
    private ArrayAdapter<Recorder> adapter;
    private List<Recorder> mDatas = new ArrayList<>();

    private View mAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testchatlistview);

        listview = (ListView) findViewById(R.id.chat_audio_listview);
        audioButton = (AudioRecorderButton) findViewById(R.id.audioButton);
        audioButton.setOnAudioFinishRecorderListener(new AudioRecorderButton.OnAudioFinishRecorderListener() {
            @Override
            public void onFinishRecord(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds,filePath);
                mDatas.add(recorder);
                adapter.notifyDataSetChanged();
                //listView移动到最后一个条
                listview.setSelection(mDatas.size() - 1);
            }
        });

        adapter = new AudioAdapter(this,mDatas);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mAnimView != null){
                    //当第一个点击时，但是还没播放完就点击第二个，此时就将第一个还原
                    mAnimView.setBackgroundResource(R.drawable.adj);
                }

                //播发动画
                mAnimView = view.findViewById(R.id.recorder_anim);
                mAnimView.setBackgroundResource(R.drawable.chat_audioplay_anim);
                AnimationDrawable anim = (AnimationDrawable) mAnimView.getBackground();
                anim.start();
                //播发音频
                MediaPlayerManager.playSound(mDatas.get(position).getFilePath(),
                        new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //播放完成后将mAnimView的背景还原
                        mAnimView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
    }
}
