package com.xiang.testapp.progressView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xiang.testapp.R;

public class ProgressBar extends View{

	private int progress;
	private int progressMax;
	private int dotColor;
	private int dotBgColor;
	private int showMode;
	public static final int SHOW_MODE_NULL = 0;
	public static final int SHOW_MODE_PERCENT = 1;
	public static final int SHOW_MODE_ALL = 2;
	private float percentTextSize;
	private int percentTextColor;
	private String unitText;
	private int unitTextColor;
	private float unitTextSize;
	private boolean isPercentFontSystem;
	private int unitTextAlignMode;
	public static final int UNIT_TEXT_ALIGN_MODE_DEFAULT = 0;
	public static final int UNIT_TEXT_ALIGN_MODE_CN = 1;
	public static final int UNIT_TEXT_ALIGN_MODE_EN = 2;
	private String buttonText;
	private int buttonTextColor;
	private float buttonTextSize;
	private float buttonTopOffset;
	private int buttonBgColor;
	private int buttonClickTextColor;
	private int buttonClickBgColor;
	
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Paint mPaint;
	private float mSin_1;
	private float mCenterX;
	private float mCenterY;
	private PorterDuffXfermode mClearXfermode;
	private PorterDuffXfermode mPercentThinXfermode;
	private Typeface fontTypeface;

	private PointF start;
	private PointF end;

	private int percent;

	private boolean isTouchInButton;

	private OnClickButtonListener mListener;
	private float buttonRadius;

	public void setOnClickButtonListener(OnClickButtonListener listener){
		this.mListener = listener;
	}

	public interface OnClickButtonListener{
		void onClickButton(View view);
	}



	public ProgressBar(Context context) {
		this(context,null);
	}

	public ProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
		dotColor = array.getColor(R.styleable.ProgressBar_dotColor, Color.WHITE);
		dotBgColor = array.getColor(R.styleable.ProgressBar_dotBgColor, Color.GRAY);
		showMode = array.getInt(R.styleable.ProgressBar_showMode, SHOW_MODE_PERCENT);
		if (showMode != SHOW_MODE_NULL){
			percentTextColor = array.getColor(R.styleable.ProgressBar_percentTextColor, Color.WHITE);
			percentTextSize = array.getDimension(R.styleable.ProgressBar_percentTextSize, dp2px(30));
			isPercentFontSystem = array.getBoolean(R.styleable.ProgressBar_isPercentFontSystem, false);
			unitText = array.getString(R.styleable.ProgressBar_unitText);
			unitTextColor = array.getColor(R.styleable.ProgressBar_unitTextColor, percentTextColor);
			unitTextSize = array.getDimension(R.styleable.ProgressBar_unitTextSize, dp2px(16));
			unitTextAlignMode = array.getInt(R.styleable.ProgressBar_unitTextAlignMode, UNIT_TEXT_ALIGN_MODE_DEFAULT);
			if(TextUtils.isEmpty(unitText)){
				unitText = "%";
			}
		}

		if(showMode == SHOW_MODE_ALL){
			buttonText = array.getString(R.styleable.ProgressBar_buttonText);
			buttonTextColor = array.getColor(R.styleable.ProgressBar_buttonTextColor, Color.GRAY);
			buttonTextSize = array.getDimension(R.styleable.ProgressBar_buttonTextSize, dp2px(16));
			buttonBgColor = array.getColor(R.styleable.ProgressBar_buttonBgColor, Color.WHITE);
			buttonClickTextColor = array.getColor(R.styleable.ProgressBar_buttonClickColor, buttonBgColor);
			buttonClickBgColor = array.getColor(R.styleable.ProgressBar_buttonClickBgColor, buttonTextColor);
			buttonTopOffset = array.getDimension(R.styleable.ProgressBar_buttonTopOffset, dp2px(15));
			if(TextUtils.isEmpty(buttonText)){
				buttonText = "一键加速";
			}
		}

		array.recycle();

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mSin_1 = (float) Math.sin(Math.toRadians(1));
		mClearXfermode = new PorterDuffXfermode(Mode.CLEAR);
		mPercentThinXfermode = new PorterDuffXfermode(Mode.DST_OUT);
		fontTypeface = isPercentFontSystem ? Typeface.DEFAULT
				: Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeueLTPro.ttf");

	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mCenterX = w / 2;
		mCenterY = h / 2;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		drawProgress();
		if(showMode != SHOW_MODE_NULL){
			drawPercent();
			if(showMode == SHOW_MODE_ALL){
				drawButton();
			}
		}
		canvas.drawBitmap(mBitmap, 0,0, mPaint);
	}
	
	private void drawProgress() {
		mPaint.setXfermode(mClearXfermode);
		mCanvas.drawPaint(mPaint);// 清空上次的
		mPaint.setTypeface(Typeface.DEFAULT);
		mPaint.setXfermode(null);
		mPaint.setStyle(Style.FILL);
		
		float outerRadius = (getWidth() < getHeight() ? getHeight() : getWidth()) / 2f;
		float dotRadius = mSin_1 * outerRadius / (1 + mSin_1);
		
		mPaint.setColor(dotColor);
		int count = 0;
		while(count++ < progress){
			mCanvas.drawCircle(mCenterX, mCenterY - outerRadius + dotRadius, dotRadius, mPaint);
			mCanvas.rotate(3.6f,mCenterX,mCenterY);
		}
		mPaint.setColor(dotBgColor);
		count--;
		while(count++ < 100){
			mCanvas.drawCircle(mCenterX, mCenterY - outerRadius + dotRadius, dotRadius, mPaint);
			mCanvas.rotate(3.6f,mCenterX,mCenterY);
		}
	}

	private void drawPercent() {
		mPaint.setColor(percentTextColor);
		mPaint.setTextSize(percentTextSize);

		float percentWidth = mPaint.measureText(percent + "");
		float unitTextWidth = mPaint.measureText(unitText);
		float textWidth = percentWidth + unitTextWidth;

		Paint.FontMetrics percentFm = mPaint.getFontMetrics();
//		float baseline = mCenterY;
		float baseline = mCenterY - percentFm.descent + (percentFm.descent - percentFm.ascent) / 2;
		mCanvas.drawText(percent + "",mCenterX - textWidth / 2,baseline,mPaint);
		mCanvas.drawText(unitText,mCenterX - textWidth / 2 + percentWidth,baseline,mPaint);


	}

	private void drawButton() {
		mPaint.setColor(isTouchInButton ? buttonClickBgColor : buttonBgColor);
		mPaint.setTextSize(buttonTextSize);
		Paint.FontMetrics btnFm = mPaint.getFontMetrics();

		float buttonWidth = mPaint.measureText(buttonText);
		float buttonHeight = buttonTextSize * 2;
		start = new PointF(mCenterX - buttonWidth / 2,mCenterY + buttonTopOffset);
		end = new PointF(mCenterX + buttonWidth / 2,mCenterY + buttonTopOffset + buttonHeight);

		buttonRadius = buttonHeight / 2;
		float left = end.x - buttonRadius;
		float top = start.y;
		float right = left + buttonRadius * 2;
		float bottom = top + buttonRadius * 2;
		RectF rectF = new RectF(left,top,right,bottom);
		Path path = new Path();
		path.reset();
		path.moveTo(start.x,start.y);
		path.rLineTo(buttonWidth,0);
		path.arcTo(rectF,270,180);
		path.rLineTo(-buttonWidth,0);
		rectF.offset(-buttonWidth,0);
		path.arcTo(rectF,90,180);
		path.close();
		mCanvas.drawPath(path,mPaint);


		mPaint.setColor(isTouchInButton ? buttonClickTextColor : buttonTextColor);
		float buttonTextLeft = mCenterX - buttonWidth / 2;
		float buttonTextBottom = mCenterY + buttonTopOffset + buttonTextSize + (btnFm.descent - btnFm.ascent) /2 - btnFm.descent;
		mCanvas.drawText(buttonText,buttonTextLeft,buttonTextBottom,mPaint);

		mPaint.setColor(Color.RED);
		mCanvas.drawLine(0,mCenterY,getWidth(),mCenterY,mPaint);
		mPaint.setColor(Color.WHITE);
		mCanvas.drawLine(0,end.y - buttonRadius,getWidth(),end.y - buttonRadius,mPaint);
		mPaint.setColor(Color.BLACK);
		float baseline = mCenterY + buttonTopOffset + buttonTextSize + (btnFm.descent - btnFm.ascent) /2 - btnFm.descent;
		mCanvas.drawLine(0,baseline,getWidth(),baseline,mPaint);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if (isTouchButton(x,y)) {
					isTouchInButton = true;
					postInvalidate();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (isTouchInButton) {
					if (!isTouchButton(x,y)){
						isTouchInButton = false;
						postInvalidate();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (isTouchInButton && mListener != null) {
					mListener.onClickButton(this);
				}
				break;

		}

		if (isTouchInButton) {
			return true;
		}

		return super.onTouchEvent(event);
	}

	private boolean isTouchButton(float x, float y) {
		if(x > start.x && x < end.x && y > start.y && y < end.y){
			return true;
		}

		float rCenterX = start.x;
		float rCenterY = (end.y - start.y) / 2;
		float newX = x - rCenterX;
		float newY = y - rCenterY;
		if(newX * newX + newY * newY <= buttonRadius * buttonRadius){
			return true;
		}

		Log.d("tag"," centerX : " + rCenterX + " \ncenterY : " + rCenterY
				+ " \nx : " + x + " \ny : " + y + "\n newX : " + newX
				+ " \nnewY : " + newY + " \nnewX * newX + newY * newY : " + (newX*newX + newY*newY)
				+ " \nbuttonRadius : " + buttonRadius
				+ " \nbuttonRadius * buttonRadius : " + (buttonRadius * buttonRadius));

		rCenterX = end.x;
		newX = x - rCenterX;

		return newX * newX + newY * newY < buttonRadius * buttonRadius;
	}

	private int dp2px(int dp){
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dp * density + 0.5f);
	}
	
	public synchronized void setProgressMax(int max){
		this.progressMax = max;
	}
	
	public synchronized void setProgress(int progress){
		if(progress < 0 || progress > progressMax){
			throw new IllegalArgumentException("进度设置有误");
		}
		this.progress = progress;
		percent = progress * 100 / progressMax;
		if (progress == progressMax) {
			isTouchInButton = false;
		}
		postInvalidate();
	}
	
}
