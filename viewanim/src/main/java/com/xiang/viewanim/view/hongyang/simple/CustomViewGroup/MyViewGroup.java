package com.xiang.viewanim.view.hongyang.simple.CustomViewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/2/2.
 */
public class MyViewGroup extends ViewGroup {

    public MyViewGroup(Context context) {
        this(context, null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //计算出所有childView的宽和高
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        /**记录如果是wrap_content时设置的宽和高*/
        int width = 0;
        int height = 0;

        int childCount = getChildCount();
        int childWidth = 0;
        int childHeight = 0;
        MarginLayoutParams childParams = null;

        //用于计算左边两个childView的高度
        int leftHeight = 0;
        //用于计算右边两个childView的高度，最终高度取二者间的大值
        int rightHeight = 0;
        //用于计算上边两个childView的宽度
        int topWidth = 0;
        //用于计算下边两个childView的宽度
        int bottomWidth = 0;

        /**根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时*/
        for(int i = 0; i < childCount;i++){
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            childParams = (MarginLayoutParams) childView.getLayoutParams();

            //上面两个childView的宽度
            if(i == 0 || i == 1){
                topWidth += childWidth + childParams.leftMargin + childParams.rightMargin;
            }
            //下面两个childView的宽度
            if(i == 2 || i == 3){
                bottomWidth += childWidth + childParams.leftMargin + childParams.rightMargin;
            }
            //左边两个childView的高度
            if(i == 0 || i == 2){
                leftHeight += childHeight + childParams.topMargin + childParams.bottomMargin;
            }
            //右边两个childView的高度
            if(i == 1 || i == 3){
                rightHeight += childHeight + childParams.topMargin + childParams.bottomMargin;
            }
        }

        width = Math.max(topWidth,bottomWidth);
        height = Math.max(leftHeight,rightHeight);

        /**如果是wrap_content 设置为我们计算的值，否则直接设置为父容器计算的值*/
        setMeasuredDimension((widthMode == MeasureSpec.AT_MOST ? width : widthSize),
                (heightMode == MeasureSpec.AT_MOST ? height : heightSize));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int childWidth = 0;
        int childHeight = 0;
        MarginLayoutParams params = null;

        /**遍历所有childView根据其宽和高，以及margin进行布局*/
        for(int i = 0;i<childCount ;i++){
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            params = (MarginLayoutParams) childView.getLayoutParams();

            int childLeft = 0,childTop = 0,childRight = 0,childBottom = 0;

            switch(i){
                case 0:
                    childLeft = params.leftMargin;
                    childTop = params.topMargin;
                    break;
                case 1:
                    childLeft = getWidth() - childWidth - params.leftMargin - params.rightMargin;
                    childTop = params.topMargin;
                    break;
                case 2:
                    childLeft = params.leftMargin;
                    childTop = getHeight() - childHeight - params.bottomMargin;
                    break;
                case 3:
                    childLeft = getWidth() - childWidth - params.leftMargin - params.rightMargin;
                    childTop = getHeight() - childHeight - params.bottomMargin;
                    break;
            }
            Log.i("TAG-->>","PARAMS = :"+ params.topMargin +"   高度 ："+getHeight() );
            childRight = childLeft + childWidth;
            childBottom = childTop + childHeight;
            childView.layout(childLeft,childTop,childRight,childBottom);
        }
    }

    /**
     * 指定返回MarginLayoutParams
     * 重写父类的该方法，返回MarginLayoutParams的实例，
     * 这样就为我们的ViewGroup指定了其LayoutParams为MarginLayoutParams。
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //return super.generateLayoutParams(attrs);
        return new MarginLayoutParams(getContext(),attrs);
    }
}
