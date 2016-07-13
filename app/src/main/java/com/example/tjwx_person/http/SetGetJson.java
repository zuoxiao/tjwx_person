package com.example.tjwx_person.http;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 *
 * @classname: SetGetJson
 * @description: TODO(这里用一句话描述这个类的作用)
 * @author: ChanLin Xiang
 * @date: 2015-12-22 上午10:38:33
 *
 */
public class SetGetJson {

	private static final String TAG = "SetGetJson";

	/**
	 *
	 * @title: getMsg
	 * @description: TODO(获得返回的message信息)
	 * @param datas
	 *            json 的String字符串
	 * @param message
	 *            message的返回字符定义,非空必填
	 * @return: String 返回类型
	 * @throws
	 */
	public static String getMsg(String datas, String message) {

		JSONObject object;
		try {
			object = new JSONObject(datas);
			String data = object.optString(message);
			if (!data.equals("")) {
				return data;
			} else if (data != null && !data.equals("")) {
				object = new JSONObject(data);
				return "";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			RunLogCat.e(TAG, e.getMessage());
		}
		return "";
	}

	/**
	 *
	 * @title: getTotalCount
	 * @description: TODO(获得返回的数据条数)
	 * @param datas
	 * @param totalCount
	 *            ,非空必填
	 * @return String 返回类型
	 * @throws
	 */
	public static int getTotalCount(String datas, String totalCount) {
		JSONObject object;
		try {
			object = new JSONObject(datas);
			int data = object.optInt(totalCount);
			return data;
		} catch (JSONException e) {
			e.printStackTrace();
			RunLogCat.e(TAG, e.getMessage());
		}
		return 0;

	}

	/**
	 *
	 * @title: getData
	 * @description: TODO(获得data字节的数据)
	 * @param datas
	 * @param data
	 *            data字节 的属性定义名称,非空必填
	 * @return String 返回类型
	 * @throws
	 */
	public static String getData(String datas, String data) {

		JSONObject object;
		try {
			object = new JSONObject(datas);
			String dataStr = object.optString(data);
			if (dataStr != null && !dataStr.equals("")) {
				return dataStr;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			RunLogCat.e(TAG, e.getMessage());
		}
		return "";
	}

	/**
	 *
	 * @title: getResult4Int
	 * @description: TODO(如果平台返回的result 返回码是int类型，则调用此方法 获得result返回的bool类型的
	 *               返回结果,注：这里我们是以0为真，其他为false)
	 * @param datas
	 * @param result
	 *            传入的返回码属性名称，非空必填
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean getResult4Int(String datas, String result) {

		JSONObject object;
		try {
			object = new JSONObject(datas);
			int data = object.optInt(result);
			if (data == 0) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			RunLogCat.e(TAG, e.getMessage());
		}
		return false;
	}

	/**
	 *
	 * @title: getResult4Str
	 * @description: TODO(如果平台返回的result 返回码是String 类型 表示返回成功失败，则调用此方法
	 *               获得result返回的bool类型的
	 *               返回结果。注：这里我们支持平台返回的result有：0、ok、yes、OK、YES)
	 * @param datas
	 * @param resultCode
	 *            平台定义的 String类型的返回码 属性名称，非空必填
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean getResult4Str(String datas, String resultCode) {

		JSONObject object;
		try {
			object = new JSONObject(datas);
			String data = object.optString(resultCode);
			// state待定
			if (data != null && "true".equals(data)) {
				return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			RunLogCat.e(TAG, e.getMessage());
		}
		return false;
	}

	/**
	 *
	 * @title: getResult4Str
	 * @description: TODO(如果平台返回的result 返回码是String 类型 表示返回成功失败，则调用此方法
	 *               获得result返回的bool类型的
	 *               返回结果。注：这里我们支持平台返回的result有：0、ok、yes、OK、YES)
	 * @param datas
	 * @param resultCode
	 *            平台定义的 String类型的返回码 属性名称，非空必填
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean getResultToken(String datas, String resultCode) {

		JSONObject object;
		try {
			object = new JSONObject(datas);
			String data = object.optString(resultCode);

			if (data != null &&

					"invalid_token".equals(data)) {
				return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			RunLogCat.e(TAG, e.getMessage());
		}
		return false;
	}

	/**
	 *
	 * @title: getResult4Str
	 * @description: 特殊情况下不止解析时逻辑运行不止需要message还需要其他参数时
	 * @param datas
	 * @param resultCode
	 *            平台定义的 String类型的返回码 属性名称，非空必填
	 * @return boolean 返回类型
	 * @throws
	 */
	public static String getResult4SpecialState(String datas, String resultCode) {

		JSONObject object;
		String data = "";
		try {
			object = new JSONObject(datas);
			data = object.optString(resultCode);

		} catch (JSONException e) {
			e.printStackTrace();
			RunLogCat.e(TAG, e.getMessage());
		}
		return data;
	}

	/**
	 *
	 * @param datas
	 * @param resultCode
	 * @return 获取错误信息
	 */
	public static String getResult4Error(String datas, String resultCode) {

		JSONObject object;
		String data = "";
		try {
			object = new JSONObject(datas);
			data = object.optString(resultCode);

		} catch (JSONException e) {
			e.printStackTrace();
			RunLogCat.e(TAG, e.getMessage());
		}
		return data;
	}

	/**
	 *
	 * @title: getListjson
	 * @description: TODO(这里用一句话描述这个方法的作用)
	 * @param model
	 * @param datas
	 * @param modelList
	 * @return List<T> 返回类型
	 * @throws
	 */
	/*
	 * public static <T extends ImModel> List<T> getListjson(T model, String
	 * datas, String modelList) {
	 * 
	 * JSONObject object; try {
	 * 
	 * object = new JSONObject(datas);
	 * 
	 * JSONArray data = object.getJSONArray(modelList);
	 * 
	 * List<T> immodel = new ArrayList<T>(); // for (int i = 0; i <
	 * data.length(); i++) { // Class<?> clazz = model.getClass(); // Field[]
	 * fields = clazz.getDeclaredFields(); // JSONObject dataObject =
	 * data.getJSONObject(i); // Object obj = null; // obj =
	 * clazz.newInstance(); // // for (int j = 0; j < fields.length; j++) { //
	 * setter(obj, fields[j].getName(), //
	 * dataObject.optString(fields[j].getName()), // String.class); // // } //
	 * immodel.add((ImModel) obj); // } for (int i = 0; i < data.length(); i++)
	 * { model = (T) data.get(i); immodel.add(model); }
	 * 
	 * return immodel; } catch (JSONException e) { e.printStackTrace();
	 * RunLogCat.e(TAG, e.getMessage()); } return null; }
	 */
	/**
	 * 对象转换成json字符串
	 *
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) throws JsonFreameworkException {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	/**
	 *
	 * @title: fromJson
	 * @description: TODO(json转成复杂数据类型方法)
	 * @param str
	 * @param type
	 *            对象的类型 new TypeToken<List<T>>(){}.getType()
	 * @return T 返回类型
	 * @throws
	 */
	public static <T> T fromJson(String str, Type type)
			throws JsonFreameworkException {
		try {
			Gson gson = new Gson();
			return gson.fromJson(str, type);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json字符串转成class对象
	 *
	 * @param str
	 * @param type
	 * @return
	 */
	public static <T> T fromJson(String str, Class<T> type)
			throws JsonFreameworkException {
		try {
			Gson gson = new Gson();
			return gson.fromJson(str, type);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
