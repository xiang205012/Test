package com.gordon.test1.testGridView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gordon on 2016/9/22.
 */

public class MyView extends HorizontalScrollView {

    private LinearLayout itemLayout;
    private List<CircleImageView> childs = new ArrayList<>();

    private OnItemClick mListener;

    public void setOnItemClick(OnItemClick listener){
        this.mListener = listener;
    }

    public interface OnItemClick{
        void onItemClick(int position);
    }

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayout = new LinearLayout(getContext());
        itemLayout.setGravity(Gravity.CENTER);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(itemLayout);
    }


    public void createItem(List<Integer> paths){
        for (int i = 0; i < paths.size(); i++) {
            final int clickIndex = i;
            CircleImageView imageView = new CircleImageView(getContext());
            LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(100,100);
            ivParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(ivParams);
            imageView.setImageResource(paths.get(i));
            itemLayout.addView(imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        setClick(clickIndex);
                        mListener.onItemClick(clickIndex);
                    }
                }
            });
            childs.add(imageView);

        }
    }

    public void setClick(int index) {
//        for (int i = 0; i < childs.size(); i++) {
//            if (index == i) {
//                childs.get(index).setBackgroundColor(Color.BLACK);
//            }else {
//                childs.get(i).setBackgroundColor(Color.BLUE);
//            }
//        }
    }


}
