package com.test.mytest.testCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gordon on 2016/10/31.
 */

public class CalendarUtil {

    /**
     * Calendar 的 month 从 0 开始，也就是全年 12 个月由 0 ~ 11 进行表示。
        而 Calendar.DAY_OF_WEEK 定义和值如下：
        Calendar.SUNDAY = 1
        Calendar.MONDAY = 2
        Calendar.TUESDAY = 3
        Calendar.WEDNESDAY = 4
        Calendar.THURSDAY = 5
        Calendar.FRIDAY = 6
        Calendar.SATURDAY = 7
     */

    public static Calendar getCalendar(){
        return Calendar.getInstance();
    }

    public static int getYear(){
        return getCalendar().get(Calendar.YEAR);
    }

    public static int getMonth(){
        return getCalendar().get(Calendar.MONTH) + 1;
    }

    public static int getDayOfMonth(){
        return getCalendar().get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfWeek(){
        return getCalendar().get(Calendar.DAY_OF_WEEK);
    }

    /**获取指定月的天数*/
    public static int getSpecifyMonthDays(int year,int month){
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1){
            month = 12;
            year -= 1;
        }

        int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
            arr[1] = 29;
        }
        return arr[month - 1];
    }

    /**获取指定月的第一天是一周中的第几天*/
    public static int getOneDayForWeek(int year,int month){
        Calendar calendar = getCalendar();
        calendar.setTime(getDateFromString(year,month));
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (index < 0){
            index = 0;
        }
        return index;
    }

    private static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month)) + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**判断是否是今天*/
    public static boolean isToday(CustomDate date){
        return (date.getYear() == getYear() && date.getMonth() == getMonth() && date.getDay() == getDayOfMonth());
    }

    /**判断是否是当月*/
    public static boolean isCurrentMonth(CustomDate date){
        return (date.getYear() == getYear() && date.getMonth() == getMonth());
    }

    /**获取相对于今天的下一个星期天的日期*/
    public static CustomDate getNextSunday(){
        Calendar calendar = getCalendar();
        calendar.set(Calendar.DATE,7 - getDayOfWeek() + 1);
        CustomDate nextDate = new CustomDate(calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        return nextDate;
    }

}
