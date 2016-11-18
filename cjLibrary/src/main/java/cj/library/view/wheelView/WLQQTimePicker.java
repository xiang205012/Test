package cj.library.view.wheelView;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cj.library.R;


public class WLQQTimePicker extends LinearLayout {
    public static final String PICKED_TIME_EXT = "picked_time";
    private static final int UPDATE_TITLE_MSG = 0x111;
    private static final int UPDATE_WHEEL = 0x112;
    private static final int UPDATE_UpdateDay_MSG = 0x113;
    private final int START_YEAR = 2013;
    private final int END_YEAR = 2100;
    private TextView mPickerTitle;
    private WheelView mWheelYear;
    private WheelView mWheelMonth;
    private WheelView mWheelDay;
    private WheelView mWheelHour;
    private WheelView mWheelMinutes;
    private TextView mCancelBtn;
    private TextView mConfirmBtn;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinutes;

    private Calendar mCalendar;
    private int mDefaultDayWhellIndex = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TITLE_MSG: {
                    updateTitle();
                }
                break;
                case UPDATE_WHEEL: {
                    updateWheel();
                }
                break;
                case UPDATE_UpdateDay_MSG: {
                    updateDay(mMonth);
                }
                break;
            }

        }
    };

    private TimerPickerListener listener;

    public void setTimerPickerListener(TimerPickerListener listener){
        this.listener = listener;
    }

    public interface TimerPickerListener{
        void makeTimePick(int year, int month, int day, int hour, int minutie);
        void cancel();
    }

    private WheelView.OnSelectListener mYearListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int year, String text) {
            mYear = START_YEAR + year;
            mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
        }

        @Override
        public void selecting(int id, String text) {
        }
    };

    private WheelView.OnSelectListener mMonthListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int month, String text) {
            mMonth = month;
            mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
            mHandler.sendEmptyMessage(UPDATE_UpdateDay_MSG);
        }

        @Override
        public void selecting(int id, String text) {
        }
    };

    private WheelView.OnSelectListener mDayListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int day, String text) {
            mDay = day + 1;
            mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
        }

        @Override
        public void selecting(int day, String text) {
        }
    };
    private WheelView.OnSelectListener mHourListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int hour, String text) {
            mHour = hour;
            mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
        }

        @Override
        public void selecting(int day, String text) {
        }
    };

    private WheelView.OnSelectListener mMinutesListener = new WheelView.OnSelectListener(){

        @Override
        public void endSelect(int minutes, String text) {
            mMinutes = minutes;
            mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
        }

        @Override
        public void selecting(int id, String text) {
        }
    };

    private Activity mContext;

    public WLQQTimePicker(Context context) {
        this(context, null);
    }

    public WLQQTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContext = (Activity) getContext();
        LayoutInflater.from(mContext).inflate(R.layout.time_picker, this);
        mPickerTitle = (TextView) findViewById(R.id.picker_title);
        mWheelYear = (WheelView) findViewById(R.id.year);
        mWheelMonth = (WheelView) findViewById(R.id.month);
        mWheelDay = (WheelView) findViewById(R.id.day);
        mWheelHour = (WheelView) findViewById(R.id.hour);
        mWheelMinutes = (WheelView) findViewById(R.id.minutes);
        mCancelBtn = (TextView) findViewById(R.id.cancel);
        mConfirmBtn = (TextView) findViewById(R.id.confirm);
        mWheelYear.setOnSelectListener(mYearListener);
        mWheelMonth.setOnSelectListener(mMonthListener);
        mWheelDay.setOnSelectListener(mDayListener);
        mWheelHour.setOnSelectListener(mHourListener);
        mWheelMinutes.setOnSelectListener(mMinutesListener);
        mConfirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WLQQTimePicker.class.getSimpleName(), mContext.getString(R.string.picker_title, mYear, mMonth + 1, mDay, mHour, mMinutes));
                if(listener != null){
                    listener.makeTimePick(mYear,mMonth,mDay,mHour,mMinutes);
                }
            }
        });
        mCancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.cancel();
                }
            }
        });
    }

    private void updateDay(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mWheelDay.resetData(getDayData(maxDay));
        if (mDay > maxDay) {
            mWheelDay.setDefault(mDefaultDayWhellIndex);
            mDay = mDefaultDayWhellIndex + 1;
        } else {
            mWheelDay.setDefault(mDay - 1);
        }
    }

    /**
     * set WLQQTimePicker date
     * @param date
     */
    public void setDate(long date) {
        mCalendar = Calendar.getInstance(Locale.CHINA);
        mCalendar.setTimeInMillis(date);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinutes = mCalendar.get(Calendar.MINUTE);

        mWheelYear.setData(getYearData());
        mWheelMonth.setData(getMonthData());
        mWheelDay.setData(getDayData(mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)));
        mWheelHour.setData(getHourData());
        mWheelMinutes.setData(getMinutes());

        mHandler.sendEmptyMessage(UPDATE_TITLE_MSG);
        mHandler.sendEmptyMessage(UPDATE_WHEEL);
    }

    private void updateTitle() {
        mPickerTitle.setText(mContext.getString(R.string.picker_title, mYear, mMonth + 1, mDay, mHour,mMinutes));
    }

    private void updateWheel() {
        mWheelYear.setDefault(mYear - START_YEAR);
        mWheelMonth.setDefault(mMonth);
        mWheelDay.setDefault(mDay - 1);
        mWheelHour.setDefault(mHour);
        mWheelMinutes.setDefault(mMinutes);
    }

    private ArrayList<String> getYearData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            list.add(i + "年");
        }
        return list;
    }

    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            list.add(i + "月");
        }
        return list;
    }

    private ArrayList<String> getDayData(int endDay) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= endDay; i++) {
            list.add(i + "日");
        }
        return list;
    }

    private ArrayList<String> getHourData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            list.add(i + "时");
        }
        return list;
    }

    private ArrayList<String> getMinutes(){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0;i < 60;i++){
            list.add(i + "分");
        }
        return list;
    }

}