package cj.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * @Description:检查网络状态的工具类
 * 检查是否有活动的网络
 * 检查网络类型0：无网络 one：WIFI 2：CMWAP 3：CMNET
 * 判断是否是WiFi连接
 * 判断是否是移动流量连接
 * 判断是否是3G网络
 * 打开设置中心
 * Created by cj on 2015/11/22.
 */
public class NetStateUtil {

	/**
	 * 检查是否有活动的网络
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isAvailable() && info.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断当前是否有可用的网络以及网络类型 0：无网络 one：WIFI 2：CMWAP 3：CMNET
	 * 
	 * @param context
	 * @return
	 */
	public static int isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return 0;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return 1;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							String extraInfo = netWorkInfo.getExtraInfo();
							if ("cmwap".equalsIgnoreCase(extraInfo)
									|| "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
								return 2;
							}
							return 3;
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 判断是否是WiFi连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null) {
			int type = networkInfo.getType();
			if (networkInfo.isAvailable() && networkInfo.isConnected()
					&& type == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前网络是否是移动流量连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobile(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null) {
			int type = networkInfo.getType();
			if (networkInfo.isAvailable() && networkInfo.isConnected()
					&& type == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是3G网络
	 * @param context
	 * @return
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 打开设置中心
	 * @param paramContext
	 */
	public static void openSettings(Context paramContext) {
		if (paramContext == null)
			return;
		try {
			if (Build.VERSION.SDK_INT > 10) {
				paramContext.startActivity(new Intent(
						"android.settings.SETTINGS"));
				return;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
			return;
		}
		paramContext.startActivity(new Intent(
				"android.settings.WIRELESS_SETTINGS"));
	}

}
