package com.example.tjwx_person.http;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;

import android.content.Context;
import android.os.Handler;

import com.example.tjwx_person.utils.UserData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class RequstClient {
	private static AsyncHttpClient client = new AsyncHttpClient();

	static {
		client.setTimeout(20000);
		client.setResponseTimeout(20000);
		client.setConnectTimeout(20000);

	}

	public static void get(String url, RequestParams params,
						   AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
		RunLogCat.d("上行get ——》 ", url + "?" + params.toString());
	}

	public static void post(String url, RequestParams params,
							AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);

		RunLogCat.d("上行post URL ——》 ", url);
		RunLogCat.d("上行post params——》 ", params.toString());

	}

	public static void post(Context context, String url, String contentType,
							RequestParams params, AsyncHttpResponseHandler responseHandler) {

		client.post(context, url, null, params, contentType, responseHandler);
	}

	public static void post(String url, Context context,
							AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(20000);

		RequestParams params = new RequestParams();
		String clientId = UserData.getSettingString(context, UserData.user_ID);
		String clientSecret = UserData.getSettingString(context,
				UserData.clientSecret);
		// params.add("client_id", clientId);
		// params.add("client_secret", clientSecret);
		// params.add("grant_type", "client_credentials");
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);
		params.put("grant_type", "client_credentials");

		client.setBasicAuth(clientId, clientSecret, new AuthScope(
				URLConstant.HOST, URLConstant.PORT, AuthScope.ANY_REALM));
		// client.setBasicAuth(clientId, clientSecret, new AuthScope(
		// URLConstant.HOST, URLConstant.PORT, AuthScope.ANY_REALM));

		// client.addHeader("Authorization", "Basic " + header);
		// client.setCredentials(new AuthScope(URLConstant.HOST,
		// URLConstant.PORT), new UsernamePasswordCredentials(clientId,
		// clientSecret));
		client.post(url, params, responseHandler);
		RunLogCat.d("上行post URL ——》 ", url);
		RunLogCat.d("上行post params——》 ", params.toString());

	}

	public static void post(Context context, String url, HttpEntity entity,
							String contentType, AsyncHttpResponseHandler responseHandler) {
		client.post(context, url, entity, contentType, responseHandler);

		RunLogCat.d("上行post URL ——》 ", url);
		RunLogCat.d("上行post entity ——》 ", entity.toString());

	}

	public static void post(String url, RequestParams params,
							JsonHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
		RunLogCat.d("上行post上行post URL ——》 ", url);
		RunLogCat.d("上行post params——》 ", params.toString());

	}

	// public static void post(String url, AsyncHttpResponseHandler
	// responseHandler) {
	// client.post(url, responseHandler);
	// RunLogCat.d("上行post ——》 ", url);
	// }

	public static void post(Context content, String url, RequestParams params,
							AsyncHttpResponseHandler responseHandler) {
		client.post(content, url, params, responseHandler);

		RunLogCat.d("上行post ——》 ", url);
		RunLogCat.d("上行post params ——》 ", params.toString());
	}

	public static void get(String url, AsyncHttpResponseHandler responseHandler) {

		client.get(url, responseHandler);
		RunLogCat.d("上行get ——》 ", url);

	}

	public static void getECGFile(String url,
								  AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
		RunLogCat.d("上行getECGFile——》 ", url);
	}

}
