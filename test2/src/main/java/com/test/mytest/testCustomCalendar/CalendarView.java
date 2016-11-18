package com.test.mytest.testCustomCalendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.test.mytest.MyApplication;
import com.test.mytest.R;

/**
 * 自定义日历view
 * Created by cj on 2016/1/6.
 */
public class CalendarView extends View {

	/**列数*/
	public static final int TOTAL_COL = 7;
	/**行数*/
	public static final int TOTAL_ROW = 6;
	/**画圆的笔*/
	private Paint mCirclePaint;
	/**画字的笔*/
	private Paint mTextPaint;
	/**画图片的笔*/
	private Paint bitmapPaint;
	/**view的宽度*/
	private int viewWidth;
	/**view的高度*/
	private int viewHeight;
	/**字之间的间距*/
	private int mCellSpace;
	/**一共6行*/
	private Row rows[] = new Row[TOTAL_ROW];
	/**自定义的日期  包括year month day*/
	private CustomDate mShowDate;
	/**当前单元格*/
	private Cell mClickCell;
	/**按下时 x 坐标*/
	private float downX;
	/**按下时 y 坐标*/
	private float downY;
	private int touchSlop;
	/**回调接口*/
	private CalendarCallBack mCallBack;


	public interface CalendarCallBack{
		void currentDay(CustomDate date);
	}

	public void setCallBack(CalendarCallBack callBack){
		this.mCallBack = callBack;
	}

	public CalendarView(Context context) {
		super(context);
		init(context);
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public CalendarView(Context context,CalendarCallBack callBack){
		super(context, null);
		this.mCallBack = callBack;
		init(context);
	}

	private void init(Context context) {
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint.setStyle(Paint.Style.FILL);//实心
		mCirclePaint.setColor(Color.parseColor("#F24949"));
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mShowDate = new CustomDate();
		//填充日期数据
		fillMonthDate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for(int i = 0; i < TOTAL_ROW; i++){
			if(rows[i] != null){
				//按6行7列的形式画出该月所有天数
				rows[i].drawCells(canvas);
			}
		}
	}

	private float x;
	private float y;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = w;
		viewHeight = h;
		mCellSpace = Math.min(viewWidth / TOTAL_ROW,viewHeight / TOTAL_COL);

		mTextPaint.setTextSize(mCellSpace / 3);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				//判断 downX,downY所属在哪个单元格内
				float disX = event.getX() - downX;
				float disY = event.getY() - downY;
				if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
					int col = (int) (downX / mCellSpace);
					int row = (int) (downY / mCellSpace);
					measureClickCell(col, row);
				}
				break;
		}
		return true;
	}

	private void measureClickCell(int col, int row) {
		if (col >= TOTAL_COL || row >= TOTAL_ROW)
			return;
		if (mClickCell != null) {
			rows[mClickCell.j].cells[mClickCell.i] = mClickCell;
		}
		if (rows[row] != null) {
			mClickCell = new Cell(rows[row].cells[col].date,
					rows[row].cells[col].state, rows[row].cells[col].i,
					rows[row].cells[col].j);
			rows[row].cells[col].state = CalendarState.CLICK_DAY;
			CustomDate date = rows[row].cells[col].date;
			date.week = col;
			if(mCallBack != null)mCallBack.currentDay(date);
			invalidate();
		}
	}


	/**一组数据，一行 (自定义总共6行)*/
	class Row {
		public int j;//第几行
		Row(int j) {
			this.j = j;
		}

		public Cell[] cells = new Cell[CalendarView.TOTAL_COL];

		public void drawCells(Canvas canvas) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null) {
					cells[i].drawSelf(canvas);
				}
			}
		}
	}

	/**一个单元格*/
	class Cell{
		public CustomDate date;
		public CalendarState state;
		public int j;//所在的哪一行
		public int i;//在行数中所在哪一列

		/**
		 * 单元格的位置，日期，该显示的状态
		 * @param date 哪一天(是要画出来的数)
		 * @param state 显示的状态
		 * @param i 列数
		 * @param j 行数
		 */
		public Cell(CustomDate date,CalendarState state,int i,int j){
			super();
			this.date = date;
			this.state = state;
			this.i = i;
			this.j = j;
		}

		// 绘制一个单元格
		public void drawSelf(Canvas canvas) {

			switch (state) {
				case NORMAL:
					mTextPaint.setColor(Color.parseColor("#80000000"));
					break;
				case TODAY:
					mTextPaint.setColor(Color.parseColor("#F24949"));
					break;
				case CLICK_DAY:
					mTextPaint.setColor(Color.parseColor("#fffffe"));
					canvas.drawCircle((float) (mCellSpace * (i + 0.5)),
							(float) ((j + 0.5) * mCellSpace), mCellSpace / 3,
							mCirclePaint);
					break;
			}
			// 绘制文字
			String content = date.day + "";
			canvas.drawText(content,
					(float) ((i + 0.5) * mCellSpace - mTextPaint.measureText(content) / 2),
					(float) ((j + 0.7) * mCellSpace - mTextPaint.measureText(
							content, 0, 1) / 2 + 20), mTextPaint);
			Resources resources = MyApplication.getInstance().getResources();
			Bitmap bp1 = BitmapFactory.decodeResource(resources, R.drawable.emoji001);
			Bitmap bp2 = BitmapFactory.decodeResource(resources, R.drawable.emoji022);
			x = (float) ((mCellSpace * (i+0.5) - bp1.getWidth()/2));
			y = (float) ((j + 0.5) * mCellSpace + (mCellSpace / 3 + 5));
			if(isNice){
				canvas.drawBitmap(bp2,x ,y,bitmapPaint);
			}else{
				canvas.drawBitmap(bp1,x ,y,bitmapPaint);
				Paint line = new Paint();
				line.setColor(Color.parseColor("#ffffff"));
				canvas.drawLine(x,y,x,y+20,line);
			}
		}
	}


	private boolean isNice;

	/**cell的state
	 *当前月日期，过去的月的日期，下个月的日期，今天，点击的日期*/
	enum CalendarState{
		NORMAL, TODAY, CLICK_DAY;
	}


	private void fillMonthDate() {
		int monthDay = DateUtil.getCurrentMonthDay();
		int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
		int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month);
		boolean isCurrentMonth = false;
		if (DateUtil.isCurrentMonth(mShowDate)) {
			isCurrentMonth = true;
		}
		int day = 0;
		for (int j = 0; j < TOTAL_ROW; j++) {
			rows[j] = new Row(j);
			for (int i = 0; i < TOTAL_COL; i++) {
				int postion = i + j * TOTAL_COL;
				if (postion >= firstDayWeek
						&& postion < firstDayWeek + currentMonthDays) {
					day++;
					if (isCurrentMonth && day == monthDay) {
						CustomDate date = CustomDate.modifiDayForObject(mShowDate, day);
						mClickCell = new Cell(date, CalendarState.TODAY, i,j);
						date.week = i;
						if(mCallBack != null)mCallBack.currentDay(date);
						rows[j].cells[i] = new Cell(date, CalendarState.CLICK_DAY, i,j);
						continue;
					}
					rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(mShowDate, day),
							CalendarState.NORMAL, i, j);
				}
			}
		}
	}

}
