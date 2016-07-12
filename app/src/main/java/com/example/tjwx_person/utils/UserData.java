package com.example.tjwx_person.utils;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;

public class UserData {
    private static final String PREFIX_NAME = "user_prefer";
    public static String IsLogin = "isLogin";
    public static String user_ID = "user_Id";
    public static String user_phone = "user_phone";
    public static String xg_token = "xg_token";
    public static String clientSecret = "clientSecret";
    public static String access_token = "access_token";
    public static String Message = "Message";
    /**
     * 是否有提交的訂單
     */
    public static String isPublish = "isPublish";
    public static String state = "state";
    // 用户信息
    public static String User_lat = "User_lat";
    public static String User_lot = "User_lot";
    public static String User_address = "User_address";
    public static String User_address1 = "User_address1";

    public static String User_address1_lat = "User_address1_lat";
    public static String User_address1_lot = "User_address1_lot";
    public static String User_address1_city = "User_address1_city";
    public static String User_address1_province = "User_address1_province";
    public static String User_address1_district = "User_address1_district";
    public static String User_address1_street = "User_address1_street";
    public static String User_address1_streetNumber = "User_address1_streetNumber";
    public static String User_address1_comment = "User_address1_comment";
    public static String User_address1_name = "User_address1_name";

    public static String User_address2 = "User_address2";
    public static String User_address2_lat = "User_address2_lat";
    public static String User_address2_lot = "User_address2_lot";
    public static String User_address2_city = "User_address2_city";
    public static String User_address2_province = "User_address2_province";
    public static String User_address2_district = "User_address2_district";
    public static String User_address2_street = "User_address2_street";
    public static String User_address2_streetNumber = "User_address2_streetNumber";
    public static String User_address2_comment = "User_address2_comment";
    public static String User_address2_name = "User_address2_name";

    public static String User_isOneAddress = "User_isOneAddress";
    /**
     * 有多少优惠券可用
     */
    public static String totalElements = "totalElements";


    public static String Singlence="Singlence";
    /**
     * 保存String类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setSettingString(Context context, String key,
                                        String value) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 0);
        SharedPreferences.Editor prefEditor = clientPreferences.edit();
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    /**
     * 获取String类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static String getSettingString(Context context, String strKey) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 0);
        String strValue = clientPreferences.getString(strKey, "");
        return strValue;
    }

    /**
     * 删除String类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void ClearSettingString(Context context, String strKey) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 0);
        SharedPreferences.Editor prefEditor = clientPreferences.edit();
        prefEditor.remove(strKey);
        prefEditor.commit();
    }

    /**
     * 保存boolean类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setSettingBoolean(Context context, String strKey,
                                         boolean value) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 0);
        SharedPreferences.Editor prefEditor = clientPreferences.edit();
        prefEditor.putBoolean(strKey, value);
        prefEditor.commit();
    }

    /**
     * 获取boolean类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static boolean getSettingBoolean(Context context, String strKey) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 0);
        boolean value = clientPreferences.getBoolean(strKey, false);
        return value;
    }

    public static boolean getSettingBoolean(Context context, String strKey,
                                            boolean value) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 0);
        boolean ret = clientPreferences.getBoolean(strKey, value);
        return ret;
    }

    /**
     * 保存int类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setSettingInt(Context context, String strKey, int value) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 0);
        SharedPreferences.Editor prefEditor = clientPreferences.edit();
        prefEditor.putInt(strKey, value);
        prefEditor.commit();
    }

    /**
     * 获取int类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static int getSettingInt(Context context, String strKey) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 1);
        int value = clientPreferences.getInt(strKey, 0);
        return value;
    }

    /**
     * 保存加密的String类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setEncrpSettingString(Context context, String key,
                                             String value) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 0);
        SharedPreferences.Editor prefEditor = clientPreferences.edit();
        prefEditor.putString(TripleDES.encryptAndBase64(key),
                TripleDES.encryptAndBase64(value));
        prefEditor.commit();
    }

    /**
     * 获取加密String类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static String getEncrpSettingString(Context context, String strKey) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, 0);
        String strValue = clientPreferences.getString(
                TripleDES.encryptAndBase64(strKey), "");
        String value = TripleDES.decryptAndBase64(strValue);
        if (value == null) {
            value = "";
        }
        return value;
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean getIsLogin(Context context) {

        String isLoging = getSettingString(context, clientSecret);
        if (!"".equals(isLoging)) {
            return true;
        }

        return false;
    }

    /**
     * 保存之前留言
     */
    public void saveMessageList(Context mContext, String message) {

        String strJson = "";
        Gson gson = new Gson();
        strJson = gson.toJson(message);
        UserData.setSettingString(mContext, UserData.Message, strJson);

    }
}