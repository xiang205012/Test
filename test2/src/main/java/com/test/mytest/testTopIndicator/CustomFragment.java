package com.test.mytest.testTopIndicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomFragment extends Fragment{
	
	private String mTitle = "theme";
	public static String TITLE = "TITLT";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(getArguments() != null){
			mTitle = getArguments().getString(TITLE);
		}
		
		TextView textView = new TextView(getActivity());
		textView.setText(mTitle);
		textView.setTextSize(45);
		textView.setTextColor(Color.parseColor("#ff00ffff"));
		textView.setGravity(Gravity.CENTER);
		return textView;
	}
	
}
