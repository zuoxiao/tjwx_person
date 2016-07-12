package com.example.tjwx_person.http;

import java.io.Serializable;
import java.lang.reflect.Type;

import android.content.Context;

import com.loopj.android.http.RequestParams;

public class MyContext implements Serializable{

	/**
	 * 线程之间传�?�信�?
	 */
	private static final long serialVersionUID = -5971339194812213214L;
	
	private Context mContext ;
	private RequestParams params;
	private Type type;

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public RequestParams getParams() {
		return params;
	}

	public void setParams(RequestParams params) {
		this.params = params;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	

}
