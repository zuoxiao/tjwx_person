package com.example.tjwx_person.http;


import android.util.Log;

import com.example.tjwx_person.utils.Constant;


/**
 * 打印logcat日志
 * @author x_tanghebiao
 *
 */
public class RunLogCat {
	
	
	 /**
     * 打印日志
     * 
     * @param msg
     *            日志内容
     */
    public static void v(String tag , String msg) {
        if(Constant.ISDEBUG){
        	Log.v(tag, msg);
        	Utils.writeLog(tag, msg);
        }
    }
    
    /**
     * 打印日志
     * 
     * @param msg
     *            日志内容
     */
    public static void d(String tag , String msg) {
        if(Constant.ISDEBUG){
        	Log.d(tag, msg);
        	Utils.writeLog(tag, msg);
        }
    }
    
    /**
     * 打印日志
     * 
     * @param msg
     *            日志内容
     */
    public static void w(String tag , String msg) {
        if(Constant.ISDEBUG){
        	Log.w(tag, msg);
        	Utils.writeLog(tag, msg);
        }
    }

    /**
     * 打印日志
     * 
     * @param msg
     *            日志内容
     *            错误日志，必须收集到日志文件
     */
    public static void e(String tag , String msg) {
//        if(Constant.ISDEBUG){
//        	
//        }
        Log.e(tag, msg);
        Utils.writeLog(tag, msg);
    }
    /**
     * 打印日志
     * 
     * @param msg
     *            日志内容
     */
    public static void i(String tag , String msg) {
        if(Constant.ISDEBUG){
        	Log.i(tag, msg);
        	Utils.writeLog(tag, msg);
        }
    }
   
}
