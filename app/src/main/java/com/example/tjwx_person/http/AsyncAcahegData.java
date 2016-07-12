package com.example.tjwx_person.http;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.loopj.android.http.RequestParams;

public class AsyncAcahegData extends AsyncTask<String, Integer, Object> {

	private Handler handler;
	private Context context;
	private String key;
	private Type type;
	private String str;
	private String url;
	private boolean isSave;
	AsyncHandler handle;
	RequestParams params;
	Object result;
	private boolean isTrue = true;// 是否解析缓存数据成功

	public AsyncAcahegData(Context mContext, String key, Handler handler,
			String url, Type type, String str, RequestParams params,
			boolean isSave, AsyncHandler handle) {
		context = mContext;
		this.key = key;
		this.handler = handler;
		this.str = str;
		this.url = url;
		this.type = type;
		this.isSave = isSave;
		this.handle = handle;
		this.params = params;
	}

	@Override
	protected Object doInBackground(String... param) {

		result = null;
		String datas = ACache.get(context).getAsString(key);
		if (!TextUtils.isEmpty(datas)) {// 如果缓存数据不为�?, 则解析数�?
			result = getData(datas);
			isTrue = true;
		} else {

			isTrue = false;
		}
		return result;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		if (!isCancelled()) {
			Bundle bund;
			Message msg = handler.obtainMessage();
			if (isTrue) {
				msg.what = 0;// 解析成功
				msg.obj = result;
				bund = new Bundle();
				bund.putSerializable("handle", handle);
				//解析成功做二次请�?
				MyContext datas = new MyContext();
				datas.setmContext(context);
				datas.setParams(params);
				datas.setType(type);
				bund.putString("url", url);
				bund.putString("key", key);
				bund.putString("str", str);
				bund.putBoolean("save", isSave);
				bund.putSerializable("datas", datas);
				msg.setData(bund);
			} else {
				msg.what = 1;// //解析失败
				MyContext datas = new MyContext();
				datas.setmContext(context);
				datas.setParams(params);
				datas.setType(type);
				bund = new Bundle();
				bund.putString("url", url);
				bund.putString("key", key);
				bund.putString("str", str);
				bund.putBoolean("save", isSave);
				bund.putSerializable("datas", datas);
				bund.putSerializable("handle", handle);
				msg.setData(bund);
			}

			msg.sendToTarget();
		}

	}

	private Object getData(String datas) {
		JSONObject jo = null;
		try {
			jo = new JSONObject(datas);
			boolean isOK = SetGetJson.getResult4Str(datas, "state");
			if (isOK) {
				if (type == null) {

				} else {
					String data = null;
					if (isOK) {
						data = jo.optString(str);
					} else {
						data = datas;
					}
					Object obj = SetGetJson.fromJson(data, type);
					// List<Art_Data> daList = SetGetJson.fromJson(new
					// String(responseBody),
					// new TypeToken<List<Art_Data>>(){}.getType());

					RunLogCat.i(url, "本地缓存数据�?" + datas);
					return obj;
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
			RunLogCat.e("getData4Native", e.getMessage());

		} catch (JsonFreameworkException e) {// 框架解析本地缓存json数据异常，请求网络解析方式解�?
			e.printStackTrace();
			RunLogCat.e("getData4Native", e.getMessage());

		}
		return null;
	}

}
