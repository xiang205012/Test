package com.gordon.test1.testFragment.testBetweenActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gordon.test1.R;


/**
 * Created by Administrator on 2016/5/12.
 */
public class OneFragment extends Fragment {

 //   nflate(resId , null )不能正确处理宽和高是因为：
 //   layout_width,layout_height是相对了父级设置的，必须与父级的LayoutParams一致。
 //   而此temp的getLayoutParams为null
 //
 //   Inflate(resId , parent,false ) 可以正确处理，
 //           因为temp.setLayoutParams(params);
 //   这个params正是root.generateLayoutParams(attrs);得到的。
 //
 //   Inflate(resId , parent,true )不仅能够正确的处理，
 //   而且已经把resId这个view加入到了parent，并且返回的是parent，和以上两者返回值有绝对的区别

    private TextView textView;
    private EditText editText;
    private Button button;

    public static final int REQUEST_CODE = 0X233;
    public static final int RESPONSE_CODE = 0X233;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one,container,false);
        textView = (TextView) view.findViewById(R.id.tv_onefragment);
        editText = (EditText) view.findViewById(R.id.et_onefragment);
        button = (Button) view.findViewById(R.id.btn_onefragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString().trim();
                if(content != null && content.length()>0){
                    Intent intent = new Intent(getActivity(), TheTwoActivity.class);
                    intent.putExtra(TwoFragment.twoArgument,content);
                    startActivityForResult(intent,REQUEST_CODE);
                }
            }
        });
        return view;
    }

    /**接收TwoFragment返回的数据*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESPONSE_CODE){
            String response = data.getStringExtra("back_response");
            textView.setText(response);
        }
    }
}
