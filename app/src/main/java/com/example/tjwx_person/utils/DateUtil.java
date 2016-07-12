package com.example.tjwx_person.utils;

import android.util.Log;

import com.example.tjwx_person.http.RunLogCat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zuo on 2016/5/16.
 */
public class DateUtil {

    private static String SERVER_FORMAT_STYLE = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static String LOCAL_FORMAT_STYLE_LONG = "yyyy-MM-dd HH:mm:ss";

    private static String LOCAL_FORMAT_STYLE_SHORT = "yyyy-MM-dd";

    /**
     * 将服务器返回的时间格式为默认长时间格式
     *
     * @param str 服务器返回的时间字符串
     * @return
     */
    public static String date2StringLong(String str) {
        String result = "";
        try {
            result = new SimpleDateFormat(LOCAL_FORMAT_STYLE_LONG).format(new SimpleDateFormat(SERVER_FORMAT_STYLE).parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            RunLogCat.d("DateUtil", "时间转换失败, " + e.getMessage());
        }
        return result;
    }

    /**
     * 将服务器返回的时间格式为默认长时间格式
     *
     * @param str 服务器返回的时间字符串
     * @return
     */
    public static String date2StringShort(String str) {
        String result = "";
        try {
            result = new SimpleDateFormat(LOCAL_FORMAT_STYLE_SHORT).format(new SimpleDateFormat(SERVER_FORMAT_STYLE).parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            RunLogCat.d("DateUtil", "时间转换失败, " + e.getMessage());
        }
        return result;
    }


}
