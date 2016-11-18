package cj.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *SharedPreferences封装
 * 保存，获取和删除基本类型数据及保存和获取对象
 * 建议在Application中初始化
 * Created by cj on 2015/11/22.
 */
public class SPUtils {
	// 保存的文件名
	private static final String FILE_NAME = "sp_data";

	public static SharedPreferences sp;
	public static Editor editor;

	public static SharedPreferences spInit(Context context){
		if(sp == null){
			synchronized (SPUtils.class){
				if(sp == null){
					sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
					editor = sp.edit();
					return sp;
				}
			}
		}
		return null;
	}


	/**
	 * 保存数据,拿到保存数据的具体类型,然后根据类型调用不同的保存方法
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void put(Context context, String key, Object object) {
		//如果没有初始化，先初始化spInit(context);
		if (object instanceof String) {
			editor.putString(key, (String) object);
		}else if (object instanceof  Integer) {
			editor.putInt(key, (Integer) object);
		}else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		}else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		}else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		}else {
			editor.putString(key, object.toString());
		}
		editor.commit();
	}
	
	
	/**
	 * 得到保存数据的方法,根据默认值得到保存的数据的具体类型， 
	 * 然后调用相对于的方法获取值
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject) {
		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		}else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		}else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		}else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		}
		return null;
	}

	/**
	 * 将对象进行base64编码后保存到SharePref中
	 *	前提是对象要序列化
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void saveMakeInvoice(Context context, String key,Object object) {
		//如果没有初始化，先初始化spInit(context);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			// 将对象的转为base64码
			String objBase64 = new String(Base64.encode(baos
					.toByteArray(), Base64.DEFAULT));
			//Log.i(TAG, "将对象转成字符串后的结果：" + objBase64);
			sp.edit().putString(key, objBase64).commit();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将SharePref中经过base64编码的对象读取出来
	 *
	 * @param context
	 * @param key
	 * @param
	 * @return
	 * @throws IOException
	 */
	public static Object getsMakeInvoice(Context context, String key) throws IOException {
		String objString = sp.getString(key, "");
		if (objString == null) return null;
		byte[] mByte = Base64.decode(objString, Base64.DEFAULT);
		ByteArrayInputStream bais = new ByteArrayInputStream(mByte);
		ObjectInputStream oips = null;
		Object obj = null;
		try {
			oips = new ObjectInputStream(bais);
			obj = (Object) oips.readObject();
			oips.close();
			return obj;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key){
		editor.remove(key);
		SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
	}

	/**
	 * 清除所有数据
	 * @param context
	 */
	public static void clear(Context context){
		editor.clear();
		SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
	}

}
