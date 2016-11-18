package com.test.mytest.testCalendarView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.test.mytest.R;

/**
 * Created by gordon on 2016/10/31.
 */

public class CalendarCard extends View {

    /**是否显示农历*/
    private boolean mIsShowLunar;
    /**是否显示上个月和下个月*/
    private boolean mIsShowPastAndNextMonth;
    /**日期颜色*/
    private int mTextColor;
    /**点击日期的颜色*/
    private int mCircleBgColor;
    /**日期默认颜色*/
    private String mDefaultTextColor = "#212121";
    /**背景圆默认颜色*/
    private String mDefaultCircleBgColor = "#a12b86e3";

    public boolean ismIsShowLunar() {
        return mIsShowLunar;
    }

    public void setmIsShowLunar(boolean mIsShowLunar) {
        this.mIsShowLunar = mIsShowLunar;
    }

    public boolean ismIsShowPastAndNextMonth() {
        return mIsShowPastAndNextMonth;
    }

    public void setmIsShowPastAndNextMonth(boolean mIsShowPastAndNextMonth) {
        this.mIsShowPastAndNextMonth = mIsShowPastAndNextMonth;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getmCircleBgColor() {
        return mCircleBgColor;
    }

    public void setmCircleBgColor(int mCircleBgColor) {
        this.mCircleBgColor = mCircleBgColor;
    }

    public String getmDefaultTextColor() {
        return mDefaultTextColor;
    }

    public void setmDefaultTextColor(String mDefaultTextColor) {
        this.mDefaultTextColor = mDefaultTextColor;
    }

    public String getmDefaultCircleBgColor() {
        return mDefaultCircleBgColor;
    }

    public void setmDefaultCircleBgColor(String mDefaultCircleBgColor) {
        this.mDefaultCircleBgColor = mDefaultCircleBgColor;
    }

    public String getmDefaultTodayCircleBgColor() {
        return mDefaultTodayCircleBgColor;
    }

    public void setmDefaultTodayCircleBgColor(String mDefaultTodayCircleBgColor) {
        this.mDefaultTodayCircleBgColor = mDefaultTodayCircleBgColor;
    }

    public String getmTodayTextColor() {
        return mTodayTextColor;
    }

    public void setmTodayTextColor(String mTodayTextColor) {
        this.mTodayTextColor = mTodayTextColor;
    }

    public String getmPastAndNextTextColor() {
        return mPastAndNextTextColor;
    }

    public void setmPastAndNextTextColor(String mPastAndNextTextColor) {
        this.mPastAndNextTextColor = mPastAndNextTextColor;
    }

    public String getmCurrentMonthNotYetDayTextColor() {
        return mCurrentMonthNotYetDayTextColor;
    }

    public void setmCurrentMonthNotYetDayTextColor(String mCurrentMonthNotYetDayTextColor) {
        this.mCurrentMonthNotYetDayTextColor = mCurrentMonthNotYetDayTextColor;
    }

    /**今天背景圆默认颜色*/
    private String mDefaultTodayCircleBgColor = "#2B86E3";
    /**今天文字颜色*/
    private String mTodayTextColor = "#F7FBFF";
    /**过去月和下个月的文字颜色*/
    private String mPastAndNextTextColor = "#B6B6B6";
    /**这个月还没到的日期文字颜色*/
    private String mCurrentMonthNotYetDayTextColor = "#727272";
    /**总行数*/
    private static final int TOTAL_ROW = 6;
    /**总列数*/
    private static final int TOTAL_COLUMN = 7;
    /**行数组，一行中有7列(即7个Cell)*/
    private Row[] mRows = new Row[TOTAL_ROW];
    /**标题栏画笔*/
    private Paint mTitlePaint;
    private Paint.FontMetrics mTitleFm;
    /**公历文字画笔*/
    private Paint mTextPaint;
    /**文字测量工具*/
    private Paint.FontMetrics tvFm;
    /**农历文字画笔*/
    private Paint mLunarTextPaint;
    /**农历文字测量*/
    private Paint.FontMetrics lunarTvFm;
    /**画点击时的圆*/
    private Paint mCircleBgPaint;
    /**用于判断是否发生滑动*/
    private float mTouchSlop;
    /**每个cell的宽度*/
    private float mCellWidth = 0;
    /**标题栏的高度*/
    private float mTitleHeight;
    /**日 一 二 三....所在的框的高度*/
    private float mWeekRectangleHeight;
    /**日 一 二 三....数组*/
    private String[] weekDay = new String[]{"日","一","二","三","四","五","六"};
    // cell 的状态
    public static final int STATE_TODAY = 0;               // 今天
    public static final int STATE_CURRENT_MONTH = 1;       // 这个月
    public static final int STATE_PAST_MONTH = 2;          // 过去一个月
    public static final int STATE_NEXT_MONTH = 3;          // 下个月
    public static final int STATE_NOT_YET_DAY = 4;         // 这个月还没到的天
    private int currentDayState;
    /**默认一进入时是当天的日期,一定要声明为static，否则多个切换时操作的不是同一个属性*/
    private static CustomDate mShowDate;

    private float downX;
    private float downY;
    /**上一次点击的单元格*/
    private Cell lastClickCell;

    private OnCalendarCardListener mListener;

    public void setOnCalendarCardListener(OnCalendarCardListener listener){
        this.mListener = listener;
    }

    public interface OnCalendarCardListener{
        /**点击日期*/
        void onItemClick(CustomDate date);
        /**viewPager页面切换*/
        void onPageChanged(CustomDate date);
    }

    public CustomDate getmShowDate(){
        return mShowDate;
    }

    public CalendarCard(Context context ,OnCalendarCardListener listener,boolean isShowLunar,boolean isShowPastAndNextMonth){
        this(context);
        this.mListener = listener;
        this.mIsShowLunar = isShowLunar;
        this.mIsShowPastAndNextMonth = isShowPastAndNextMonth;
    }

    public CalendarCard(Context context) {
        this(context,null);
    }

    public CalendarCard(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CalendarCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarCard);
        mIsShowLunar = array.getBoolean(R.styleable.CalendarCard_showLunar,false);
        mIsShowPastAndNextMonth = array.getBoolean(R.styleable.CalendarCard_showPastAndNext,false);
        mTextColor = array.getColor(R.styleable.CalendarCard_textColor, Color.parseColor(mDefaultTextColor));
        mCircleBgColor = array.getColor(R.styleable.CalendarCard_circleBgColor,Color.parseColor(mDefaultCircleBgColor));
        array.recycle();

        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setStyle(Paint.Style.FILL);
        mTitlePaint.setColor(Color.parseColor(mCurrentMonthNotYetDayTextColor));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);

        mLunarTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLunarTextPaint.setStyle(Paint.Style.FILL);
        mLunarTextPaint.setColor(Color.parseColor(mPastAndNextTextColor));

        mCircleBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleBgPaint.setStyle(Paint.Style.FILL);
        mCircleBgPaint.setColor(mCircleBgColor);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mShowDate = new CustomDate();
        fillData();

    }

    private void fillData() {

        lastClickCell = null;
        int today = mShowDate.getDay();
        int firstDayIndexOfWeek = CalendarUtil.getOneDayForWeek(mShowDate.getYear(),mShowDate.getMonth());
        int currentMonthDays = CalendarUtil.getSpecifyMonthDays(mShowDate.getYear(),mShowDate.getMonth());
        int pastMonthDays = CalendarUtil.getSpecifyMonthDays(mShowDate.getYear(),mShowDate.getMonth() - 1);

        boolean isCurrentMonth = false;
        if (CalendarUtil.isCurrentMonth(mShowDate)) {
            isCurrentMonth = true;
        }
        int day = 0;
        for (int row = 0; row < TOTAL_ROW; row++) {
            mRows[row] = new Row(row);
            for (int column = 0; column < TOTAL_COLUMN; column++) {
                int position = column + row * TOTAL_COLUMN;
                if (position >= firstDayIndexOfWeek && position < firstDayIndexOfWeek + currentMonthDays){
                    // 这个月
                    day++;
                    CustomDate date = new CustomDate(mShowDate.getYear(),mShowDate.getMonth(),day);
                    if (isCurrentMonth && day == today){
                        // 今天
                        currentDayState = STATE_TODAY;
                    } else if (isCurrentMonth && day > today){
                        // 还没到的天
                        currentDayState = STATE_NOT_YET_DAY;
                    } else if (isCurrentMonth && day < today){
                        // 这个月已过去的天
                        currentDayState = STATE_CURRENT_MONTH;
                    }
                    mRows[row].cells[column] = new Cell(date,currentDayState,row,column);

                } else if (position < firstDayIndexOfWeek){
                    // 上个月
                    if (mIsShowPastAndNextMonth) {
                        int pastDay = pastMonthDays - (firstDayIndexOfWeek - position - 1);
                        CustomDate date = new CustomDate(mShowDate.getYear(),mShowDate.getMonth() - 1,pastDay);
                        currentDayState = STATE_PAST_MONTH;
                        mRows[row].cells[column] = new Cell(date,currentDayState,row,column);
                    } else {
                        mRows[row].cells[column] = null;
                    }
                } else if (position >= firstDayIndexOfWeek + currentMonthDays){
                    // 下个月
                    if (mIsShowPastAndNextMonth) {
                        int nextDay = position - (firstDayIndexOfWeek + currentMonthDays) + 1;
                        CustomDate date = new CustomDate(mShowDate.getYear(),mShowDate.getMonth() + 1,nextDay);
                        currentDayState = STATE_NEXT_MONTH;
                        mRows[row].cells[column] = new Cell(date,currentDayState,row,column);
                    } else {
                        mRows[row].cells[column] = null;
                    }
                }
            }
        }

        if (mListener != null) {
            mListener.onPageChanged(mShowDate);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        measureCellWidth(w, h);
    }

    private void measureCellWidth(int w, int h) {
        mCellWidth = (w > h ? h / TOTAL_COLUMN : w / TOTAL_COLUMN);
        mTitleHeight = mCellWidth;
        mTitlePaint.setTextSize(mCellWidth / 2.5f);
        mTitleFm = mTitlePaint.getFontMetrics();
        mWeekRectangleHeight = mCellWidth / 2;
        mTextPaint.setTextSize(mCellWidth / 3);
        tvFm = mTextPaint.getFontMetrics();
        mLunarTextPaint.setTextSize(mCellWidth / 5);
        lunarTvFm = mLunarTextPaint.getFontMetrics();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCellWidth == 0){
            measureCellWidth(getWidth(),getHeight());
        }

        drawTitle(canvas);

        drawWeekDay(canvas);

        for (int i = 0; i < mRows.length; i++) {
            if (mRows[i] != null) {
                mRows[i].drawCell(canvas);
            }
        }

    }

    private void drawTitle(Canvas canvas) {
        String titleDate = mShowDate.getYear() + "-" + mShowDate.getMonth();
        float titleWidth = mTitlePaint.measureText(titleDate);
        float titleX = getWidth() / 2 - titleWidth / 2;
        float titleY = mTitleHeight / 2 - mTitleFm.descent + (mTitleFm.descent - mTitleFm.ascent) / 2;
        canvas.drawText(titleDate,titleX,titleY,mTitlePaint);

        // 画生肖年
        if (mIsShowLunar){
            mTitlePaint.setColor(Color.parseColor("#F36C6C"));
            float circleX = 6 * mCellWidth + mCellWidth / 2;
            float circleY = mCellWidth / 2;
            canvas.drawCircle(circleX,circleY,mCellWidth / 3,mTitlePaint);

            mTitlePaint.setColor(Color.parseColor(mCurrentMonthNotYetDayTextColor));
            String zodiacName = mShowDate.getZodiacName();
            float zodiacWidth = mTitlePaint.measureText(zodiacName);
            float x = 6 * mCellWidth + mCellWidth / 2 - zodiacWidth / 2;
            float y = mTitleHeight / 2 - mTitleFm.descent + (mTitleFm.descent - mTitleFm.ascent) / 2 ;
            canvas.drawText(zodiacName,x,y,mTitlePaint);
        }

    }

    private void drawWeekDay(Canvas canvas) {
        mTextPaint.setColor(Color.parseColor(mCurrentMonthNotYetDayTextColor));
        float centerX = 0;
        float centerY = mTitleHeight + mWeekRectangleHeight / 2 - tvFm.descent + (tvFm.descent - tvFm.ascent) / 2;
        for (int column = 0; column < TOTAL_COLUMN; column++) {
            String weekContent = weekDay[column];
            float textWidth = mTextPaint.measureText(weekContent);
            centerX = mCellWidth * column + (mCellWidth / 2 - textWidth / 2);
            canvas.drawText(weekContent,centerX,centerY,mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - downX;
                float disY = event.getY() - downY;
                if (disX * disX + disY * disY < mTouchSlop * mTouchSlop){
                    int row = (int) (downY / mCellWidth);
                    int column = (int) (downX / mCellWidth);
                    if (row > TOTAL_ROW || column > TOTAL_COLUMN){
                        return false;
                    }
                    Cell cell = mRows[row].cells[column];
                    if (cell != null) {
                        cell.isShowCircleBg = true;
                        if (lastClickCell == null) {
                            lastClickCell = cell;
                        } else {
                            lastClickCell.isShowCircleBg = false;
                        }
                        CustomDate date = cell.mDate;
                        invalidate();
                        lastClickCell = cell;
                        if (mListener != null) {
                            mListener.onItemClick(date);
                        }
                    }
                }


                break;
        }
        return true;
    }

    /**一行 组元素*/
    class Row{
        private int indexOfRow;
        private Cell[] cells = new Cell[TOTAL_COLUMN];

        public Cell[] getCells(){
            return cells;
        }

        public Row(int indexOfRow){
            this.indexOfRow = indexOfRow;
        }

        public void drawCell(Canvas canvas){
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null) {
                    cells[i].drawSelf(canvas);
                }
            }
        }
    }

    /**单元格元素*/
    class Cell{
        private CustomDate mDate;
        private int state;
        private int indexOfRow;
        private int indexOfColumn;
        private boolean isShowCircleBg;

        public Cell(CustomDate date,int state ,int indexOfRow , int indexOfColumn){
            this.mDate = date;
            this.state = state;
            this.indexOfRow = indexOfRow;
            this.indexOfColumn = indexOfColumn;
        }

        public void drawSelf(Canvas canvas){
            switch(state){
                case STATE_TODAY:
                    mTextPaint.setColor(Color.parseColor(mDefaultTodayCircleBgColor));
                    float centerX = mCellWidth * indexOfColumn + mCellWidth / 2;
                    float centerY = mTitleHeight + mWeekRectangleHeight + mCellWidth * indexOfRow + mCellWidth / 2;
                    canvas.drawCircle(centerX,centerY, (float) (mCellWidth / 2.5),mTextPaint);
                    mTextPaint.setColor(Color.parseColor(mTodayTextColor));
                    break;
                case STATE_CURRENT_MONTH:
                    mTextPaint.setColor(mTextColor);
                    break;
                case STATE_PAST_MONTH:
                case STATE_NEXT_MONTH:
                    mTextPaint.setColor(Color.parseColor(mPastAndNextTextColor));
                    break;
                case STATE_NOT_YET_DAY:
                    mTextPaint.setColor(Color.parseColor(mCurrentMonthNotYetDayTextColor));
                    break;
            }

            if (state != STATE_TODAY && isShowCircleBg){
                float centerX = mCellWidth * indexOfColumn + mCellWidth / 2;
                float centerY = mTitleHeight + mWeekRectangleHeight + mCellWidth * indexOfRow + mCellWidth / 2;
                canvas.drawCircle(centerX,centerY, (float) (mCellWidth / 2.5),mCircleBgPaint);
            }
            // 公历日期
            String content = mDate.getDay() + "";
            float textWidth = mTextPaint.measureText(content);
            float x = mCellWidth * indexOfColumn + (mCellWidth / 2 - textWidth / 2);
            float y = mTitleHeight + mWeekRectangleHeight + mCellWidth * indexOfRow + (mCellWidth / 2 - tvFm.descent + (tvFm.descent - tvFm.ascent) / 2);
            canvas.drawText(content,x,y,mTextPaint);

            // 农历日期
            if (mIsShowLunar) {
                int offsetTop = 20;
                String lunarDayString = mDate.getLunarDay();
                float lunarTextWidth = mLunarTextPaint.measureText(lunarDayString);
                float lx = mCellWidth * indexOfColumn + mCellWidth / 2 - lunarTextWidth / 2;
                float ly = offsetTop + tvFm.bottom + mWeekRectangleHeight + mTitleHeight +
                                mCellWidth * indexOfRow +
                                (mCellWidth / 2 - lunarTvFm.descent + (lunarTvFm.descent - lunarTvFm.ascent) / 2);
                canvas.drawText(lunarDayString, lx, ly, mLunarTextPaint);
            }
        }

    }

    /**viewPager向右翻页*/
    public void toPageRight() {
        if (mShowDate.getMonth() == 12) {
            mShowDate.setMonth(1);
            mShowDate.setYear(mShowDate.getYear() + 1);
        } else {
            mShowDate.setMonth(mShowDate.getMonth() + 1);
        }
        fillData();
        invalidate();
    }

    /**viewPager向左翻页*/
    public void toPageLeft() {
        if (mShowDate.getMonth() == 1){
            mShowDate.setMonth(12);
            mShowDate.setYear(mShowDate.getYear() - 1);
        } else {
            mShowDate.setMonth(mShowDate.getMonth() - 1);
        }
        fillData();
        invalidate();
    }


}
