package com.test.mytest.testChatListView;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.mytest.R;

import java.util.List;

/**
 * Created by Administrator on 2015/12/27.
 */
public class AudioAdapter extends ArrayAdapter<Recorder> {

    private List<Recorder> mDatas;
    private Context mContext;

    private int minItemWidth;//item在屏幕中最小宽度(即每0.5s或1s所占的宽度)
    private int maxItemWidth;//item在屏幕中最大宽度

    public AudioAdapter(Context context, List<Recorder> data) {
        super(context, -1 ,data);
        mContext = context;
        mDatas = data;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        minItemWidth = (int) (displayMetrics.widthPixels * 0.15f);
        maxItemWidth = (int) (displayMetrics.widthPixels * 0.7f);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.chat_audio_recorder_item,null);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.chat_audio_time);
            holder.length = convertView.findViewById(R.id.chat_audio_length);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //四舍五入取时间值
        holder.time.setText(Math.round(getItem(position).getTime()) + "\"");
        ViewGroup.LayoutParams params = holder.length.getLayoutParams();
        // 60f:表示最多可录音时长为60s,也可以在AudioRecorderManager中设定最大时长，这里只为测试
        params.width = (int) (minItemWidth + (maxItemWidth / 60f * getItem(position).getTime()));

        return convertView;
    }

    class ViewHolder{
        TextView time;
        View length;
    }

}
