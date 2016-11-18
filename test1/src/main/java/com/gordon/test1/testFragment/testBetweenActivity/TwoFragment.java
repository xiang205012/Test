package com.gordon.test1.testFragment.testBetweenActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class TwoFragment extends Fragment {

    public static final String twoArgument = "argument";

    private TextView textView;
    private EditText editText;
    private Button button;

    public static TwoFragment newInstance(String argument){
        Bundle bundle = new Bundle();
        bundle.putString("content", argument);
        TwoFragment fragment = new TwoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two,container,false);
        textView = (TextView) view.findViewById(R.id.tv_twofragment);
        editText = (EditText) view.findViewById(R.id.et_twofragment);
        button = (Button) view.findViewById(R.id.cancel_two);
        if(getArguments() != null){
            Log.i("TAG-->>","值是： "+getArguments().getString("content"));
            textView.setText(getArguments().getString("content"));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = editText.getText().toString().trim();
                if(response != null && response.length()>0){
                    Intent intent = new Intent();
                    intent.putExtra("back_response",response);
                    getActivity().setResult(OneFragment.RESPONSE_CODE,intent);
                    getActivity().finish();
                }
            }
        });

        return view;
    }
}
