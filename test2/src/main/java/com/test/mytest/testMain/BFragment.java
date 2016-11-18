package com.test.mytest.testMain;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.test.mytest.MaterialDesign.dialog.MyBaseDialogActivity;
import com.test.mytest.R;
import com.test.mytest.propertyAnimation.PropertyAnimationActivity;
import com.test.mytest.testA_Z_ListView.AZListViewActivity;
import com.test.mytest.testChatListView.TestChatListViewActivity;
import com.test.mytest.testCustomCarmer.CustomCamerActivity;
import com.test.mytest.testDialog.DialogActivity;
import com.test.mytest.testSwipeMenuListview.SwipeMenuListViewActivity;
import com.test.mytest.testTopIndicator.TopIndicatorActivity;
import com.test.mytest.testTopNavigationBar.TopNavigationBarActivity;
import com.test.mytest.testWebView.WebViewActivity;

/**
 * Created by Administrator on 2015/8/4.
 */
public class BFragment extends Fragment {

    private View view;
    Button bt_test_pop;
    Button bt_test_azlistview;
    Button bt_test_topindicator;
    Button bt_test_topnavigetionbar;
    Button bt_test_MaterialDialog;
    Button bt_test_ChatListView;
    Button bt_test_face_discern;
    Button bt_test_swipe_listview;
    Button bt_test_property_animation;
    Button bt_test_webview;
    Button bt_test_customcamera;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.bfragment,null);
        bt_test_pop = (Button) view.findViewById(R.id.bt_test_pop);
        bt_test_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DialogActivity.class));
                startActivity(new Intent(getActivity(), DialogActivity.class));
            }
        });
        bt_test_azlistview = (Button) view.findViewById(R.id.bt_test_azlistview);
        bt_test_azlistview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AZListViewActivity.class));
            }
        });
        bt_test_topindicator = (Button) view.findViewById(R.id.bt_test_topindicator);
        bt_test_topindicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TopIndicatorActivity.class));
            }
        });
        bt_test_topnavigetionbar = (Button) view.findViewById(R.id.bt_test_topnavigetionbar);
        bt_test_topnavigetionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TopNavigationBarActivity.class));
            }
        });
        bt_test_MaterialDialog = (Button) view.findViewById(R.id.bt_test_MaterialDialog);
        bt_test_MaterialDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyBaseDialogActivity.class));
            }
        });
        bt_test_ChatListView = (Button) view.findViewById(R.id.bt_test_ChatListView);
        bt_test_ChatListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TestChatListViewActivity.class));
            }
        });
        bt_test_swipe_listview = (Button) view.findViewById(R.id.bt_test_swipe_listview);
        bt_test_swipe_listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SwipeMenuListViewActivity.class));
            }
        });
        bt_test_property_animation = (Button) view.findViewById(R.id.bt_test_property_animation);
        bt_test_property_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PropertyAnimationActivity.class));
            }
        });
        bt_test_webview = (Button) view.findViewById(R.id.bt_test_webview);
        bt_test_webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WebViewActivity.class));
            }
        });
        bt_test_customcamera = (Button) view.findViewById(R.id.bt_test_customcamera);
        bt_test_customcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomCamerActivity.class));
            }
        });

        return view;
    }

}
