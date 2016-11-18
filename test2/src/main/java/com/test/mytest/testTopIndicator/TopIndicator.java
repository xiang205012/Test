package com.test.mytest.testTopIndicator;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 顶部indicator,滚动的导航条
 * @author dewyze
 *
 */
public class TopIndicator extends LinearLayout {
	private Context context;
	private static final String TAG = "TopIndicator";
	private static final int UNDER_LINE_HEIGHT=5;
	private List<CheckedTextView> mCheckedList = new ArrayList<CheckedTextView>();
	private List<View> mViewList = new ArrayList<View>();

	private int mScreenWidth;
	private int mUnderLineWidth;
	private View mUnderLine;
	// 底部线条移动初始位置
	private int mUnderLineFromX = 0;

	public TopIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	public TopIndicator(Context context) {
		super(context);
		this.context=context;
	}

	/**
	 * 必须先初始化，才能使用,加上左边的图片
	 * @param mLabels
	 * @param mDrawableIds
	 */
	public void init(String[] mLabels, int[] mDrawableIds){
		setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(250, 250, 250));

		mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
		mUnderLineWidth = mScreenWidth / mLabels.length;

		mUnderLine = new View(context);
		mUnderLine.setBackgroundColor(Color.BLUE);
		LayoutParams underLineParams = new LayoutParams(
				mUnderLineWidth, UNDER_LINE_HEIGHT);

		// 标题layout
		LinearLayout topLayout = new LinearLayout(context);
		LayoutParams topLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		topLayout.setOrientation(LinearLayout.HORIZONTAL);
		topLayoutParams.gravity=Gravity.CENTER;
		topLayoutParams.weight=1.0f;
		LayoutInflater inflater = LayoutInflater.from(context);

		LayoutParams params = new LayoutParams(
				0, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;

		int size = mLabels.length;
		for (int i = 0; i < size; i++) {
			final int index = i;

			final View view = inflater.inflate(R.layout.top_indicator_item,
					null);

			final CheckedTextView itemName = (CheckedTextView) view
					.findViewById(R.id.item_name);
			if(mDrawableIds!=null){
				itemName.setCompoundDrawablesWithIntrinsicBounds(context
								.getResources().getDrawable(mDrawableIds[i]), null, null,
						null);
				itemName.setCompoundDrawablePadding(10);
			}
			itemName.setText(mLabels[i]);

			topLayout.addView(view, params);

			itemName.setTag(index);
			itemName.setTextSize(15);
			itemName.setPadding(18,18,18,18);
			mCheckedList.add(itemName);
			mViewList.add(view);

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != mTabListener) {
						mTabListener.onIndicatorSelected(index);
					}
				}
			});
			mViewList.get(i).setBackgroundResource(R.drawable.comm_btn_selector);
			// 初始化 底部菜单选中状态,默认第一个选中
			if (i == 0) {
				itemName.setChecked(true);
				itemName.setTextColor(Color.BLUE);
				//	mViewList.get(i).setBackgroundColor(Color.rgb(240, 241, 242));
			} else {
				itemName.setChecked(false);
				itemName.setTextColor(Color.rgb(19, 12, 14));
				//	mViewList.get(i).setBackgroundColor(Color.rgb(255, 255, 255));
			}

		}
		this.addView(topLayout, topLayoutParams);
		this.addView(mUnderLine, underLineParams);
	}

	/**
	 * 设置底部导航中图片显示状态和字体颜色
	 */
	public void setTabsDisplay(Context context, int index) {
		int size = mCheckedList.size();
		for (int i = 0; i < size; i++) {
			CheckedTextView checkedTextView = mCheckedList.get(i);
			if ((Integer) (checkedTextView.getTag()) == index) {
				//	LogUtils.i(TAG, mLabels[index] + " is selected...");
				checkedTextView.setChecked(true);
				checkedTextView.setTextColor(Color.BLUE);
				//	mViewList.get(i).setBackgroundColor(Color.rgb(240, 241, 242));

			} else {
				checkedTextView.setChecked(false);
				checkedTextView.setTextColor(Color.rgb(19, 12, 14));
				//	mViewList.get(i).setBackgroundColor(Color.rgb(255, 255, 255));
			}
		}
		// 下划线动画
		doUnderLineAnimation(index);
	}

	private void doUnderLineAnimation(int index) {
		TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX,
				index * mUnderLineWidth, 0, 0);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setFillAfter(true);
		animation.setDuration(150);
		mUnderLine.startAnimation(animation);
		mUnderLineFromX = index * mUnderLineWidth;
	}

	// 回调接口
	private OnTopIndicatorListener mTabListener;

	public interface OnTopIndicatorListener {
		void onIndicatorSelected(int index);
	}

	public void setOnTopIndicatorListener(OnTopIndicatorListener listener) {
		this.mTabListener = listener;
	}

}

