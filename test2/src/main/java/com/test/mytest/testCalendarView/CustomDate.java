package com.test.mytest.testCalendarView;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by gordon on 2016/10/31.
 */

public class CustomDate implements Parcelable{

    private int year;
    private int month;
    private int day;
    private int dayOfWeek;
    /**生肖*/
    private String zodiacName;
    /**农历时间*/
    private String lunarDay;
    /**是否是节日*/
    private boolean isHoliday;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setZodiacName(String zodiacName){
        this.zodiacName = zodiacName;
    }

    public String getZodiacName(){
        return zodiacName;
    }

    public void setLunarDay(String lunarDay){
        this.lunarDay = lunarDay;
    }

    public String getLunarDay(){
        return lunarDay;
    }

    public void setHoliday(boolean isHoliday){
        this.isHoliday = isHoliday;
    }

    public boolean isHoliday(){
        return isHoliday;
    }

    public CustomDate(int year, int month, int day){
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        this.year = year;
        this.month = month;
        this.day = day;
        getLunarDayOfThis();
    }

    public CustomDate(){
        this.year = CalendarUtil.getYear();
        this.month = CalendarUtil.getMonth();
        this.day = CalendarUtil.getDayOfMonth();
        getLunarDayOfThis();
    }

    /**获取该日期的农历时间*/
    private void getLunarDayOfThis(){
        String date = year
                + "-" + (month > 9 ? month : ("0" + month))
                + "-" + (day > 9 ? day : ("0" + day));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long time = sdf.parse(date).getTime();
            Lunar lunar = new Lunar(time);
            this.zodiacName = lunar.getAnimalString();
            if (lunar.isSFestival()) {
                this.lunarDay = lunar.getSFestivalName();
                this.isHoliday = true;
            } else {
                if (lunar.isLFestival()
                        && lunar.getLunarMonthString().substring(0,1).equals("闰") == false){
                    this.lunarDay = lunar.getLFestivalName();
                    this.isHoliday = true;
                } else {
//                    if (lunar.getLunarDayString().equals("初一")){
//                        this.lunarDay = lunar.getLunarMonthString() + "月";
//                    } else {
                        this.lunarDay = lunar.getLunarDayString();
//                    }
                    this.isHoliday = false;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    protected CustomDate(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        dayOfWeek = in.readInt();
    }

    public static final Creator<CustomDate> CREATOR = new Creator<CustomDate>() {
        @Override
        public CustomDate createFromParcel(Parcel in) {
            return new CustomDate(in);
        }

        @Override
        public CustomDate[] newArray(int size) {
            return new CustomDate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(dayOfWeek);
    }

    @Override
    public String toString() {
        return "CustomDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", dayOfWeek=" + dayOfWeek +
                '}';
    }
}
