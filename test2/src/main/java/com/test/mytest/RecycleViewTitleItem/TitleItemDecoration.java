package com.test.mytest.RecycleViewTitleItem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

/**
 * Created by gordon on 2016/11/3.
 *
 * http://blog.csdn.net/zxt0601/article/details/52355199
 */

public class TitleItemDecoration extends RecyclerView.ItemDecoration{

    private List<CityBean> mDatas;
    private Paint mPaint;
    private Rect mBounds;
    private int mTitleHeight;
    private int mtitleBgColor = Color.parseColor("#FFDFDFDF");
    private int mtitleFontColor = Color.parseColor("#FF999999");
    private int mtitleFontSize;


    public TitleItemDecoration(Context context, List<CityBean> list){
        mDatas = list;
        mBounds = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,context.getResources().getDisplayMetrics());
        mtitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mtitleFontSize);


    }

    /**用于RecyclerView中的item通过outRect设置padding值*/
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        if (mDatas == null || mDatas.isEmpty() || position > mDatas.size() - 1){
            return;
        }

        if(position > -1){
            if(position == 0){
                outRect.set(0,mTitleHeight,0,0);
            }else {
                if (mDatas.get(position).getCityNamePinyinFirstLetter() != null
                        && !mDatas.get(position).getCityNamePinyinFirstLetter().equals(mDatas.get(position - 1).getCityNamePinyinFirstLetter())){
                    outRect.set(0,mTitleHeight,0,0);
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if(mDatas == null || mDatas.isEmpty() || position > mDatas.size() - 1){
                return;
            }

            if(position > -1){
                if(position == 0){
                    drawTitleArea(c,left,right,child,params,position);
                }else {
                    if (mDatas.get(position).getCityNamePinyinFirstLetter() != null
                            && !mDatas.get(position).getCityNamePinyinFirstLetter().equals(mDatas.get(position-1).getCityNamePinyinFirstLetter())){
                        drawTitleArea(c,left,right,child,params,position);
                    }
                }
            }

        }

    }

    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {
        mPaint.setColor(mtitleBgColor);
        c.drawRect(left,child.getTop() - params.topMargin - mTitleHeight,
                right,child.getTop() - params.topMargin,mPaint);

        mPaint.setColor(mtitleFontColor);
        mPaint.getTextBounds(mDatas.get(position).getCityNamePinyinFirstLetter(),
                0,mDatas.get(position).getCityNamePinyinFirstLetter().length(),mBounds);
        c.drawText(mDatas.get(position).getCityNamePinyinFirstLetter(),
                child.getPaddingLeft(),child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2),
                mPaint);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int position = ((LinearLayoutManager)(parent.getLayoutManager())).findFirstVisibleItemPosition();
        if(mDatas == null || mDatas.isEmpty() || position > mDatas.size() - 1){
            return;
        }

        String tag = mDatas.get(position).getCityNamePinyinFirstLetter();
        View child = parent.findViewHolderForLayoutPosition(position).itemView;
        boolean flag = false;
        if((position + 1) < mDatas.size()){
            if(tag != null && !tag.equals(mDatas.get(position + 1).getCityNamePinyinFirstLetter())){
                if(child.getHeight() + child.getTop() < mTitleHeight){
                    c.save();
                    flag = true;
                    c.translate(0,child.getHeight() + child.getTop() - mTitleHeight);
                }
            }
        }
        mPaint.setColor(mtitleBgColor);
        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(),
                parent.getRight() - parent.getPaddingRight(),
                parent.getPaddingTop() + mTitleHeight, mPaint);
        mPaint.setColor(mtitleFontColor);
        mPaint.getTextBounds(tag, 0, tag.length(), mBounds);
        c.drawText(tag, child.getPaddingLeft(),
                parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2),
                mPaint);
        if (flag)
            c.restore();//恢复画布到之前保存的状态

    }
}
