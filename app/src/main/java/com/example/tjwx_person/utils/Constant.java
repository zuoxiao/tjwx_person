package com.example.tjwx_person.utils;



import android.os.Environment;

/**
 * 
 * @author Xiang_ChanLin
 *
 *@todo  存放app 静态常量、静态变量
 *注： 创建每个变量或常量时，请注释（防止重复）
 */
public class Constant {
	//是否debug，debug开启日志
	public static final boolean ISDEBUG = true;
	/**
	 * 配置 文件类型数据 持久化缓存目录
	 * 注：每次更新app时候 根据是否需要舍弃上一个版本缓存的数据来判断是否更换数据持久化 目录
	 */
	public static final String chcheDir = Environment.getExternalStorageDirectory()
			.getPath() + "/travel/temp/app1.0.0/";
		
	//字符编码wx783664c91b7c05a9
	public static final String encoding_UTF8 = "utf-8";
	// APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static final String APP_ID = "wxa1a8e4657fdf5bdd";
	
}
