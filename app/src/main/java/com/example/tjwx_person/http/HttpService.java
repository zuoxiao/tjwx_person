/**
 * @title: HttpService.java
 * @package: com.ynchinamobile.hexintravel.http
 * @description: TODO(用一句话描述该文件做�?�?)
 * @author: ChanLin Xiang
 * @date: 2015-12-23 上午11:12:54
 * @version: v1.00
 */
package com.example.tjwx_person.http;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.tjwx_person.bean.UpdateDown;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.Constant;
import com.example.tjwx_person.utils.UserData;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @classname: HttpService
 * @description: TODO(这里用一句话描述这个类的作用)
 * @author: ChanLin Xiang
 * @date: 2015-12-23 上午11:12:54
 */
public class HttpService {
    private static final String TAG = HttpService.class.getName();

    /**
     * @param str
     * @param datas
     * @param handle
     * @param type   void 返回类型
     * @throws
     * @title: getData4Native
     * @description: TODO(本地数据解析)
     */
    private static <T> void PullData4Native(Context context, String url,
                                            String key, RequestParams params, final String str, String datas,
                                            final AsyncHandler handle, Class<T> type) {
        JSONObject jo = null;
        try {
            jo = new JSONObject(datas);
            boolean isOK = SetGetJson.getResult4Str(datas, "state");
            if (isOK) {
                if (type == null) {
                    handle.onSuccess(null);
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
                    handle.onSuccess(obj);
                    RunLogCat.i(url, "本地缓存数据�?" + datas);
                }

            } else {
                // 网络数据返回错误
                handle.onFailure(URLConstant.ReturnError,
                        URLConstant.map.get(URLConstant.ReturnError));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            RunLogCat.e("getData4Native", e.getMessage());
            PostData(context, url, key, str, params, true, handle, type);
        } catch (JsonFreameworkException e) {// 框架Json解析异常
            e.printStackTrace();
            RunLogCat.e("getData4Native", e.getMessage());
            // PostData(context, url, key, str, params, handle, type);
        }
    }

    /**
     *
     * @title: PullData4Network
     * @description: TODO(网络数据解析)
     * @param str
     * @param datas
     * @param handle
     * @param type
     *            void 返回类型
     * @throws
     */
    // private static <T> void PullData4Network(Context context, String key,
    // RequestParams params, final String str, String datas,
    // boolean issave, final AsyncHandler handle, Class<T> type) {
    // JSONObject jo = null;
    // try {
    // jo = new JSONObject(datas);
    // boolean isOK = SetGetJson.getResult4Str(datas, "state");
    // if (isOK) {
    //
    // if (type == null) {
    // handle.onSuccess(null);
    // } else {
    // String data = null;
    // Object obj = "";
    // boolean isTrue = SetGetJson.getResult4State(datas, "state");
    // if (isTrue) {
    //
    // data = jo.optString(str);
    //
    // if (!"".equals(data) && !"[]".equals(data)) {
    // obj = SetGetJson.fromJson(data, type);
    // }
    // handle.onSuccess(obj);
    // } else {
    // // 特殊情况下不止解析时逻辑运行不止�?要message还需要其他参数时
    //
    // String state = SetGetJson.getResult4SpecialState(datas,
    // "state");
    // data = SetGetJson.getMsg(datas, "message");
    // handle.onFailure(Integer.valueOf(state), data);
    //
    // }
    //
    // // 数据重新缓存
    //
    // // ACache.get(context).put(key, datas);
    //
    // }
    //
    // } else {
    // // 网络数据返回错误
    // handle.onFailure(URLConstant.ReturnError,
    // URLConstant.map.get(URLConstant.ReturnError));
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // RunLogCat.e("getData4Native", e.getMessage());
    // // 网络数据返回错误
    // handle.onFailure(URLConstant.ReturnError,
    // URLConstant.map.get(URLConstant.ReturnError));
    // } catch (JsonFreameworkException e) {
    // e.printStackTrace();
    // RunLogCat.e("getData4Native", e.getMessage());
    // // 框架Json 解析异常
    // handle.onFailure(URLConstant.ReturnError,
    // URLConstant.map.get(URLConstant.ReturnError));
    //
    // }
    // }

    /**
     * @param url
     * @param str
     * @param params
     * @param handle
     * @param type   void 返回类型
     * @throws
     * @title: PostData
     * @description: TODO(网络请求数据)
     */
    private static <T> void PostData(final Context context, final String url,
                                     final String key, final String str, final RequestParams params,
                                     final boolean issave, final AsyncHandler handle, final Class<T> type) {
        RequstClient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, org.apache.http.Header[] arg1,
                                  byte[] responseBody, Throwable gar2) {
                if (responseBody == null) {
                    // 网络数据返回�?
                    handle.onFailure(URLConstant.NoNetwork,
                            URLConstant.map.get(URLConstant.NoNetwork));
                } else {
                    try {
                        String datas = new String(responseBody,
                                Constant.encoding_UTF8);
                        RunLogCat.i(url, "网络响应数据�?" + datas);
                        // 网络数据解析
                        PullData4Network(context, key, params, str, datas,
                                issave, handle, type);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        handle.onFailure(URLConstant.ReturnError,
                                URLConstant.map.get(URLConstant.ReturnError));
                    }
                }
            }

            @Override
            public void onSuccess(int arg0, org.apache.http.Header[] arg1,
                                  byte[] arg2) {
                handle.onFailure(URLConstant.ReturnError,
                        URLConstant.map.get(URLConstant.ReturnError));

            }

        });
    }

    /**
     * @param context 上下文路�?
     * @param url     请求的url
     * @param isSave  是否缓存数据
     * @param key     缓存数据的key
     * @param str     解析数据的对象在json中的属�?�名称定�?
     * @param params  请求参数
     * @param handle  回调接口
     * @param type    �?要转换的数据类型 void 返回类型
     * @throws
     * @title: getData4Post
     * @description: TODO(post 方式获得数据, 转换的对象是
     *实体类型对象：注：如果本地有缓存数据则本地获取，如果没有则网络获取�??)
     */

    public static <T> void getData4Post(Context context, String url,
                                        boolean mustRefresh, boolean isSave, String key, final String str,
                                        RequestParams params, final AsyncHandler handle, final Type type) {
        Log.i(TAG, "URL: " + url);
        if (mustRefresh) {// 必须强制刷新数据

            if (Utils.isNetworkAvailable(context)) {
                PostData(context, url, key, str, params, true, handle, type);
            } else {
                handle.onFailure(URLConstant.NetworkAvailable,
                        URLConstant.map.get(URLConstant.NetworkAvailable));
            }
        } else {
            // 判断是否有缓存数�?;
            AsyncAcahegData ACacheData = new AsyncAcahegData(context, key,
                    handler, url, type, str, params, isSave, handle);
            ACacheData.execute();

            // String datas = ACache.get(context).getAsString(key);
            // if (datas != null && !"".equals(datas)) {// 如果缓存数据不为�?, 则解析数�?
            // // 解析数据，如果解析错误，还是得请求一次网络数据，并覆盖本地缓�?
            // PullData4Native(context, url, key, params, str, datas, handle,
            // type);
            // } else {
            // PostData(context, url, key, str, params, handle, type);
            // }
        }
    }

    public static <T> void getStatus4Post(final Context context,
                                          final String url, RequestParams params, final AsyncHandler handle) {
        RequstClient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                if (responseBody == null) {
                    // 网络数据返回�?
                    handle.onFailure(URLConstant.NoNetwork,
                            URLConstant.map.get(URLConstant.NoNetwork));
                } else {
                    try {
                        String datas = new String(responseBody,
                                Constant.encoding_UTF8);
                        RunLogCat.i(url, "网络响应数据�?" + datas);
                        // 网络数据解析
                        pullData4Post(context, datas, handle);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        handle.onFailure(URLConstant.ReturnError,
                                URLConstant.map.get(URLConstant.ReturnError));
                    }
                }
            }

            private void pullData4Post(Context context, String datas,
                                       AsyncHandler handle) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(datas);
                    boolean isOK = SetGetJson.getResult4Str(datas, "state");
                    if (isOK) {
                        handle.onSuccess(null);
                    } else {
                        // 网络数据返回错误
                        handle.onFailure(URLConstant.ReturnError,
                                URLConstant.map.get(URLConstant.ReturnError));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    RunLogCat.e("getData4Native", e.getMessage());
                    handle.onFailure(URLConstant.ReturnError,
                            URLConstant.map.get(URLConstant.ReturnError));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    String data = new String(responseBody,
                            Constant.encoding_UTF8);
                    RunLogCat.i(url, "网络响应数据�?" + data);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                handle.onFailure(URLConstant.ReturnError,
                        URLConstant.map.get(URLConstant.ReturnError));

            }
        });
    }

    /**
     * @param str
     * @param datas
     * @param handle
     * @param type   void 返回类型
     * @throws
     * @title: getData4Native
     * @description: TODO(本地数据解析)
     */
    private static <T> void PullData4Native(Context context, String url,
                                            String key, RequestParams params, final String str, String datas,
                                            final AsyncHandler handle, Type type) {
        JSONObject jo = null;
        try {
            jo = new JSONObject(datas);
            boolean isOK = SetGetJson.getResult4Str(datas, "state");
            if (isOK) {
                if (type == null) {
                    handle.onSuccess(null);
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
                    handle.onSuccess(obj);
                    RunLogCat.i(url, "本地缓存数据�?" + datas);
                }

            } else {
                // 网络数据返回错误
                handle.onFailure(URLConstant.ReturnError,
                        URLConstant.map.get(URLConstant.ReturnError));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            RunLogCat.e("getData4Native", e.getMessage());
            PostData(context, url, key, str, params, true, handle, type);
        } catch (JsonFreameworkException e) {// 框架解析本地缓存json数据异常，请求网络解析方式解�?
            e.printStackTrace();
            RunLogCat.e("getData4Native", e.getMessage());
            PostData(context, url, key, str, params, true, handle, type);
        }
    }

    /**
     * @param str
     * @param datas
     * @param handle
     * @param type   void 返回类型
     * @throws
     * @title: PullData4Network
     * @description: TODO(网络数据解析)
     */
    private static <T> void PullData4Network(Context context, String key,
                                             RequestParams params, final String str, String datas,
                                             boolean isSave, final AsyncHandler handle, Type type) {
        JSONObject jo = null;
        try {
            jo = new JSONObject(datas);
            boolean isOK = SetGetJson.getResult4Str(datas, "success");
            if (isOK) {
                if (type == null) {
                    datas = jo.optString(str);
                    handle.onSuccess(datas);
                } else {
                    String data = null;
                    Object obj = "";

                    if (str != null) {
                        data = jo.optString(str);
                    } else {
                        data = datas;
                    }
                    if (!"".equals(data) && !"[]".equals(data)) {
                        obj = SetGetJson.fromJson(data, type);


                        if (type.equals(UpdateDown.class)) {
                            boolean isupData = SetGetJson.getResult4Str(datas, "update");
                            UpdateDown updateDown = (UpdateDown) obj;
                            updateDown.setUpdate(isupData);
                            handle.onSuccess(obj);
                        } else {

                            handle.onSuccess(obj);
                        }
                    } else {
                        handle.onFailure(0, "");
                    }


                    // 数据重新缓存
                    if (isSave) {
                        ACache.get(context).put(key, datas);
                    }

                }

            } else {
                String FailureData = SetGetJson.getData(datas, "msg");
                handle.onFailure(0, FailureData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            RunLogCat.e("getData4Native", e.getMessage());
            handle.onFailure(URLConstant.ReturnError,
                    URLConstant.map.get(URLConstant.ReturnError));
        } catch (JsonFreameworkException e) {// 框架解析异常
            e.printStackTrace();
            RunLogCat.e("getData4Native", e.getMessage());
            handle.onFailure(URLConstant.ReturnError,
                    URLConstant.map.get(URLConstant.ReturnError));
        }
    }

    /**
     * @param url
     * @param str
     * @param params
     * @param handle
     * @param type   void 返回类型
     * @throws
     * @title: PostData
     * @description: TODO(网络请求数据)
     */
    public static <T> void PostData(final Context context, final String url,
                                    final String key, final String str, final RequestParams params,
                                    final boolean isSave, final AsyncHandler handle, final Type type) {

        RequstClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                if (responseBody == null) {
                    // 网络数据返回空
                    handle.onFailure(URLConstant.NoNetwork,
                            URLConstant.map.get(URLConstant.NoNetwork));
                } else {
                    try {
                        String datas = new String(responseBody,
                                Constant.encoding_UTF8);

                        //	BaseToast.makeLongToast(context,datas);

                        RunLogCat.i(url, "网络响应数据：" + datas);
                        // 网络数据解析
                        PullData4Network(context, key, params, str, datas,
                                isSave, handle, type);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        handle.onFailure(URLConstant.ReturnError,
                                URLConstant.map.get(URLConstant.ReturnError));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    try {

                        String datas = new String(responseBody,
                                Constant.encoding_UTF8);
                        //	BaseToast.makeLongToast(context,datas);
                        if (SetGetJson.getResultToken(datas, "error")) {
                            // 判断错误是否是注册过时
                            RequstClient.post(URLConstant.TOKEN_URL, context,
                                    new AsyncHttpResponseHandler() {

                                        @Override
                                        public void onSuccess(int arg0,
                                                              Header[] arg1, byte[] arg2) {
                                            String access_token = "";
                                            try {
                                                String datas = new String(arg2,
                                                        Constant.encoding_UTF8);
                                                JSONObject object;
                                                try {
                                                    object = new JSONObject(
                                                            datas);
                                                    access_token = object
                                                            .optString("value");
                                                    UserData.setSettingString(
                                                            context,
                                                            UserData.access_token,
                                                            access_token);

                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch
                                                    // block
                                                    e.printStackTrace();
                                                }

                                            } catch (UnsupportedEncodingException e1) {
                                                // TODO Auto-generated catch
                                                // block
                                                e1.printStackTrace();
                                            }
                                            params.remove("access_token");
                                            params.add("access_token",
                                                    access_token);

                                            HttpService.PostData(context, url,
                                                    key, str, params, isSave,
                                                    handle, type);


                                        }

                                        @Override
                                        public void onFailure(int arg0,
                                                              Header[] arg1, byte[] arg2,
                                                              Throwable arg3) {
                                            if (arg2 != null) {
                                                try {
                                                    String datas = new String(
                                                            arg2,
                                                            Constant.encoding_UTF8);
                                                    handle.onFailure(1, "");
                                                    Log.d("", "");
                                                } catch (UnsupportedEncodingException e) {
                                                    // TODO Auto-generated catch
                                                    // block
                                                    e.printStackTrace();
                                                }
                                            }

                                        }

                                    });
                        } else {
                            handle.onFailure(URLConstant.NoNetwork,
                                    URLConstant.map.get(URLConstant.NoNetwork));
                        }
                        // handle.onFailure(URLConstant.ReturnError, "");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    handle.onFailure(1, "");
                }
            }
        });
    }

    /**
     * @param context 上下文路�?
     * @param url     请求的url
     * @param key     缓存数据的key
     * @param str     解析数据的对象在json中的属�?�名称定�?
     * @param params  请求参数
     * @param handle  回调接口
     * @param type    �?要转换的数据类型�? 如果不需要返回参数类型则可以传null，此参数定义了复杂的数据类型 void 返回类型
     * @throws
     * @title: getData4Post
     * @description: TODO(post 方式获得数据 ，转换的对象是type
     *类型的对象：注：如果本地有缓存数据则本地获取，如果没有则网络获取�?)
     */

    public static <T> void getData4Post(Context context, String url,
                                        boolean mustRefresh, String key, final String str,
                                        RequestParams params, final AsyncHandler handle, Type type) {
        Log.i(TAG, "URL: " + url);

        if (mustRefresh) {// 必须强制刷新数据
            if (Utils.isNetworkAvailable(context)) {
                PostData(context, url, key, str, params, true, handle, type);
            } else {
                handle.onFailure(URLConstant.NetworkAvailable,
                        URLConstant.map.get(URLConstant.NetworkAvailable));
            }
        } else {

            // 判断是否有缓存数�?;
            AsyncAcahegData ACacheData = new AsyncAcahegData(context, key,
                    handler, url, type, str, params, true, handle);
            ACacheData.execute();

            // String datas = ACache.get(context).getAsString(key);
            // if (!TextUtils.isEmpty(datas)) {// 如果缓存数据不为�?, 则解析数�?
            // // 解析数据，如果解析错误，还是得请求一次网络数据，并覆盖本地缓�?
            // PullData4Native(context, url, key, params, str, datas, handle,
            // type);
            // } else {
            // PostData(context, url, key, str, params, handle, type);
            // }
        }
    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            if (msg.what == 0) {// 0为解析成�?

                AsyncHandler handle = (AsyncHandler) data
                        .getSerializable("handle");
                handle.onSuccess(msg.obj);
                MyContext datas = (MyContext) data.getSerializable("datas");
                PostData(datas.getmContext(), data.getString("url"),
                        data.getString("key"), data.getString("str"),
                        datas.getParams(), data.getBoolean("save"), handle,
                        datas.getType());
            } else {
                AsyncHandler handle = (AsyncHandler) data
                        .getSerializable("handle");
                MyContext datas = (MyContext) data.getSerializable("datas");
                PostData(datas.getmContext(), data.getString("url"),
                        data.getString("key"), data.getString("str"),
                        datas.getParams(), data.getBoolean("save"), handle,
                        datas.getType());
            }

        }
    };

}
