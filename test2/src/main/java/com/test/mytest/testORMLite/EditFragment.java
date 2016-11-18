package com.test.mytest.testORMLite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.test.mytest.R;

import cj.library.utils.ViewUtils;

/**
 * Created by Administrator on 2015/9/11.
 */
public class EditFragment extends Fragment {

//    @ViewInject(R.id.et_consignee)
    private EditText et_consignee;
//    @ViewInject(R.id.et_consignee_phone)
    private EditText et_consignee_phone;
//    @ViewInject(R.id.et_consignee_address)
    private EditText et_consignee_address;
//    @ViewInject(R.id.et_consignee_postcode)
    private EditText et_consignee_postcode;
//    @ViewInject(R.id.bt_save_consignee_info)
    private Button bt_save_consignee_info;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.edit_address_layout,null);
//        ViewUtils.inject(this,view);

        initData();

        return view;


    }

    private void initData() {
        final String name = et_consignee.getText().toString().trim();
        final String phone = et_consignee_phone.getText().toString().trim();
        final String address = et_consignee_address.getText().toString().trim();
        final String postCode = et_consignee_postcode.getText().toString().trim();

        bt_save_consignee_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((TextUtils.isEmpty(name)&&TextUtils.isEmpty(phone))
                        &&TextUtils.isEmpty(address)&&TextUtils.isEmpty(postCode)){

                }
            }
        });

    }
}
