package com.example.tjwx_person.http;



import java.io.Serializable;

/**
 * 
* @classname: AsyncHandler 
* @description: TODO(接口请求回调接口) 
* @author: ChanLin Xiang
* @date: 2015-12-22 下午2:29:16 
*
 */
public abstract class AsyncHandler implements Serializable{

	public abstract void onSuccess(Object obj);

	public abstract void onFailure(int statusCode, String message);

	

	
}
