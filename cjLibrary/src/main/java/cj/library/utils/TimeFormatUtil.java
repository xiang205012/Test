package cj.library.utils;

import java.text.SimpleDateFormat;

/**
 * Created by cj on 2016/one/12.
 */
public class TimeFormatUtil {

    public static final int DATE = 1;
    public static final int TIME = 2;
    public static final int NOSECOND = 3;
    public static final int DATEANDTIME = 4;
    public static final int TIMENOSECOND = 5;

    /**
     * 格式化时间
     *
     * @param object 时间字符串或Date
     * @param index
     *          one、yyyy-MM-dd
     *          2、HH:mm:ss
     *          3、yyyy-MM-dd HH:mm
     *          4、yyyy-MM-dd HH:mm:ss
     *          5、HH:mm
     * @return
     */
    public static String format(Object object,int index){
        return getFormat(index).format(object);
    }

    public static SimpleDateFormat getFormat(int index){
        if(index == DATE){
            return new SimpleDateFormat("yyyy-MM-dd");
        }else if(index == TIME){
            return new SimpleDateFormat("HH:mm:ss");
        }else if(index == NOSECOND){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }else if(index == DATEANDTIME){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }else if(index == TIMENOSECOND){
            return new SimpleDateFormat("HH:mm");
        }
        return null;
    }

}
