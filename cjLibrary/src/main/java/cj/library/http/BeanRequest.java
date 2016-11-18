package cj.library.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
/**
 * 将返回的json数据转成object的网络请求
 * Created by cj on 2015/11/24.
 */
public class BeanRequest<T> extends Request<T> {
	private Gson mGson = new Gson();
	private Class<T> mClazz;
	private Listener<T> mListener;
	private Map<String, String> mHeaders;
	private Map<String, String> mParams;

	public BeanRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
		this(Method.GET, url, clazz, null, listener, errorListener);
	}

	public BeanRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
					   Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mClazz = clazz;
		this.mHeaders = headers;
		this.mListener = listener;
		this.setShouldCache(true);
	}

	public BeanRequest(String url, Map<String, String> params, Class<T> clazz,
					   Listener<T> listener, ErrorListener errorListener){
		this(Method.POST, url, params,clazz, null, listener, errorListener);
	}

	public BeanRequest(int method, String url, Map<String, String> params, Class<T> clazz, Map<String, String> headers,
					   Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mParams = params;
		this.mClazz = clazz;
		this.mHeaders = headers;
		this.mListener = listener;
		this.setShouldCache(true);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParams != null ? mParams : super.getParams();
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(mGson.fromJson(json, mClazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}
