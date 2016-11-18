package cj.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作工具类
 * Created by cj on 2015/11/22.
 */
public class FileUtil {

	public static final String CAMERA = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
	public static final String CACHE_IMG ="/cache/images/";

	/**
	 * 创建目录
	 * @param context
	 * @param cache 目录名
	 * @return
	 */
	public static File createDir(Context context,String cache) {
		StringBuilder path = new StringBuilder();
		if (SDRAMUtils.isSDAvailable()) {
			String root = Environment.getExternalStorageDirectory().getAbsolutePath() +
					AppUtils.getAppName(context);
			path.append(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			path.append(File.separator);// '/'
			path.append(root);// /mnt/sdcard/GooglePlay
			path.append(File.separator);
			path.append(cache);// /mnt/sdcard/GooglePlay/cache

		}else{
			File filesDir = context.getCacheDir();    //  cache  getFileDir file
			path.append(filesDir.getAbsolutePath());// /data/data/com.itheima.googleplay/cache
			path.append(File.separator);///data/data/com.itheima.googleplay/cache/
			path.append(cache);///data/data/com.itheima.googleplay/cache/cache
		}
		File file = new File(path.toString());
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();// 创建文件夹
		}
		return file;

	}

	/**
	 * 删除文件
	 */
	public static boolean delFile(String path) {
		if (path.equals("") || !SDRAMUtils.isSDAvailable()) {
			return false;
		} else {
			File file = new File(path);
			if (file.exists() && file.isFile()) {
				return file.delete();
			} else {
				return false;
			}
		}
	}

	/**
	 * 删除递归目录下的所有文件
	 * @param dir
	 * @return
	 */
	public static void delDir(String dir) {
		File file = new File(dir);
		if (file.exists()) {
			if (file.isDirectory()) {
				if (file.list().length == 0) {// 删除文件夹
					file.delete();
				} else {
					File[] files = file.listFiles();
					for (File f : files) {
						delDir(f.getPath());
					}
					file.delete();
				}
			} else {// 删除文件
				delFile(file.getPath());
			}
		}
	}


	/**
	 * 获取文件类型
	 * image图片，video视频，audio音乐
	 * @param name
	 * @return
	 */
	public static String getFileType(Context context,String name) {
		if (name == null) {
			return "";
		} else {
			String type = "";
			String end = name.substring(name.lastIndexOf(".") + 1,
					name.length()).toLowerCase(context.getResources()
							.getConfiguration().locale);
			if (end.equals("jpg") || end.equals("png")) {
				type = "image";
			} else if (end.equals("3gp") || end.equals("mp4")) {
				type = "video";
			} else if (end.equals("amr") || end.equals("wmv")
					|| end.equals("mp3")) {
				type = "audio";
			} else {
				type = "*";
			}
			type += "/*";
			return type;
		}
	}

	/**
	 * 打开文件
	 * @param file:文件对象
	 */
	public static void openFile(Context context,File file) {
		if (file.exists()) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		}
	}
}
