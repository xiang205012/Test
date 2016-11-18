package cj.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获取app的名称和版本号
 * 获取设备名称，设备版本号和sdk版本号
 * Created by cj on 2015/11/22.
 */
public class AppUtils {

    private AppUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context){
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get android os version no
     * @return
     */
    public static float getAndroidVersion() {
        return Float.valueOf(android.os.Build.VERSION.RELEASE);
    }

    /**
     * get device model
     * @return
     */
    public String getDeviceModel () {
        return android.os.Build.MODEL;
    }

    /**
     * get android os sdk version  2.2 = 8,2.3 = 9,4.2.one = 17
     * @return sdk version
     */
    public static int getSDKVersion(){
        return android.os.Build.VERSION.SDK_INT;
    }

}
