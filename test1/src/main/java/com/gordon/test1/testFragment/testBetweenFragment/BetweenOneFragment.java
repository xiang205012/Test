package com.gordon.test1.testFragment.testBetweenFragment;

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
public class BetweenOneFragment extends Fragment {

    private TextView textView;
    private EditText editText;
    private Button button;

    /**启动第二个fragment的请求码*/
    private static final int REQUEST_CODE = 0X43;

    private BetweenFragmentActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BetweenFragmentActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one,container,false);
        textView = (TextView) view.findViewById(R.id.tv_onefragment);
        editText = (EditText) view.findViewById(R.id.et_onefragment);
        button = (Button) view.findViewById(R.id.btn_onefragment);
        if(getArguments() != null){
            // 设置从第二个fragment反传回来的值
            textView.setText(getArguments().getString("two"));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString().trim();
                if(content != null && content.length()>0){
                    Bundle bundle = new Bundle();
                    bundle.putString("one", content);
                    mActivity.showWitchFragment(bundle,BetweenFragmentActivity.TWO_CODE);
                }
            }
        });
        return view;
    }

}
