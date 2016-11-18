package com.gordon.test1.zoomImageView;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.gordon.test1.R;

/**
 * Created by gordon on 2016/9/11.
 */
public class CropLayout extends RelativeLayout {


    public CropLayout(Context context) {
        this(context,null);
    }

    public CropLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CropLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ZoomImageView imageView = new ZoomImageView(context);
        imageView.setImageResource(R.drawable.test_zoomimageview);
        imageView.setLayoutParams(params);

        CropView cropView = new CropView(context);
        cropView.setLayoutParams(params);

        RectF borderRectF = cropView.getBorderRectF();

        Log.d("tag","borderRectF : " + (borderRectF == null));

        imageView.setmBorderRectF(borderRectF);

        addView(imageView);
        addView(cropView);

    }




}
