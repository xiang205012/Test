package com.gordon.test1.testFragment.testBetweenFragment;

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
public class BetweenTwoFragment extends Fragment {

    private TextView textView;
    private EditText editText;
    private Button button;

    private BetweenFragmentActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BetweenFragmentActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two,container,false);
        textView = (TextView) view.findViewById(R.id.tv_twofragment);
        editText = (EditText) view.findViewById(R.id.et_twofragment);
        button = (Button) view.findViewById(R.id.cancel_two);
        if(getArguments() != null){
            Log.i("TAG-->>", "值是： " + getArguments().getString("one"));
            textView.setText(getArguments().getString("one"));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mActivity.transaction.show(mActivity.oneFragment);
                String response = editText.getText().toString().trim();
                if(response != null && response.length()>0){
                    Bundle bundle = new Bundle();
                    bundle.putString("two",response);
                    mActivity.showWitchFragment(bundle,BetweenFragmentActivity.ONE_CODE);

                }
            }
        });

        return view;
    }
}
