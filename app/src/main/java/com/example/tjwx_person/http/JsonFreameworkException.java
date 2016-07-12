package com.example.tjwx_person.http;



/** 
 * @classname: JsonFreameworkException 
 * @description: TODO(自定义Json 框架解析异常类) 
 * @author: ChanLin Xiang
 * @date: 2015-12-25 上午10:24:01 
 *  
 */
public class JsonFreameworkException extends Exception {

	private static final long serialVersionUID = 7765222743704125947L;

	public JsonFreameworkException(){
		super();
	}
	public JsonFreameworkException(String msg) {

		super(msg);

		}

		public JsonFreameworkException(String msg, Throwable cause) {

		super(msg, cause);

		}

		public JsonFreameworkException(Throwable cause) {

			super(cause);

		}

}
