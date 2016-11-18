package com.test.mytest.testRecyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * RecyclerView中item之间的分割线(与ListView的默认divider颜色一样的)
 * recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));
 * recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
 * 如果想自己定制：新建一个shape.xml画出分割线，然后在对应的values目录下的styles.xml项目引用的
 * Theme中添加<item name="android:listDivider">@drawable/shape</item>
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration
{

	private static final int[] ATTRS = new int[] { android.R.attr.listDivider };

	public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

	public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
	
	

	private Drawable mDivider;

	private int mOrientation;

	public DividerItemDecoration(Context context, int orientation)
	{
		final TypedArray a = context.obtainStyledAttributes(ATTRS);
		mDivider = a.getDrawable(0);
		a.recycle();
		setOrientation(orientation);
	}

	public void setOrientation(int orientation)
	{
		if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)
		{
			throw new IllegalArgumentException("invalid orientation");
		}
		mOrientation = orientation;
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent)
	{
		Log.v("itemdecoration", "onDraw()");
		 if (mOrientation == VERTICAL_LIST) {
	            drawVertical(c, parent);
	        } else {
	            drawHorizontal(c, parent);
	        }
	}

	public void drawVertical(Canvas c, RecyclerView parent)
	{
		final int left = parent.getPaddingLeft();
		final int right = parent.getWidth() - parent.getPaddingRight();

		final int childCount = parent.getChildCount();

		for (int i = 0; i < childCount; i++)
		{
			final View child = parent.getChildAt(i);
			RecyclerView v = new RecyclerView(
					parent.getContext());
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
					.getLayoutParams();
			final int top = child.getBottom() + params.bottomMargin;
			final int bottom = top + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	public void drawHorizontal(Canvas c, RecyclerView parent)
	{
		final int top = parent.getPaddingTop();
		final int bottom = parent.getHeight() - parent.getPaddingBottom();

		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++)
		{
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
					.getLayoutParams();
			final int left = child.getRight() + params.rightMargin;
			final int right = left + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	@Override
	public void getItemOffsets(Rect outRect, int itemPosition,
			RecyclerView parent)
	{
		if (mOrientation == VERTICAL_LIST)
		{
			outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
		} else
		{
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		}
	}
}
