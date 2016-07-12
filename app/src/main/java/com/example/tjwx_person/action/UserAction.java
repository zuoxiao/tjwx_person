package com.example.tjwx_person.action;

import android.content.Context;

import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.bean.CouponData;
import com.example.tjwx_person.bean.CouponDetailData;
import com.example.tjwx_person.bean.MessageListData;
import com.example.tjwx_person.bean.NoticeDatas;
import com.example.tjwx_person.bean.NoticeDate;
import com.example.tjwx_person.bean.PersenData;
import com.example.tjwx_person.bean.Position;
import com.example.tjwx_person.bean.RegistData;
import com.example.tjwx_person.bean.TypeData;
import com.example.tjwx_person.bean.UpdateDown;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.bean.selectOrders;
import com.example.tjwx_person.bean.skillData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.HttpService;
import com.example.tjwx_person.http.URLConstant;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.FileRequestParams;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class UserAction {
    private static final String TAG = "UserAction";
    private static UserAction userAction = null;
    RequestParams params = null;

    public static UserAction getInstance() {
        if (userAction == null) {
            userAction = new UserAction();
        }
        return userAction;
    }

    /**
     * 登录和注册接口
     *
     * @param context
     * @param mobileno
     * @param Verification
     * @param mustRefresh
     * @param handle
     */
    public void regist(Context context, String mobileno, String Verification,
                       boolean mustRefresh, AsyncHandler handle) {
        params = new RequestParams();
        params.add("mobilePhone", mobileno);
        params.add("verifyCode", Verification);
        params.add("type", "CUSTOMER");

        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.register;
        String url = URLConstant.register;
        HttpService.getData4Post(context, url, mustRefresh, key, str, params,
                handle, RegistData.class);
        // 用法 new TypeToken<List<People>>(){}.getType()
        // HttpService.getData4Post(context, url, mustRefresh, key, str, params,
        // handle, null);
    }

    /**
     * 获取服务类型
     */
    public void getTypes(Context context, String clientId, String access_token, boolean emergency,
                         boolean mustRefresh, AsyncHandler handle) {

        params = new RequestParams();
        //params.add("clientId", clientId);
        // params.add("access_token", access_token);

        params.put("emergency", emergency);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.skillList;

        HttpService.getData4Post(context, URLConstant.skillList, mustRefresh,
                key, str, params, handle, new TypeToken<List<skillData>>() {
                }.getType());
        // 用法 new TypeToken<List<People>>(){}.getType()
        // HttpService.getData4Post(context, url, mustRefresh, key, str, params,
        // handle, null);

    }

    /**
     * 发布订单
     *
     * @throws UnsupportedEncodingException
     */

    public void publish(Context context, File[] pictures, File audio,
                        String comment, boolean emergency, String skillIds, String address,
                        double longitude, double latitude, String access_token, boolean audioOrder,
                        AsyncHandler handle) {

        FileRequestParams params = new FileRequestParams();
        params.setForceMultipartEntityContentType(true);
        try {
            if (pictures != null && pictures.length > 0) {
                //params.put("pictures", pictures, "image/jpg", pictures[0].getName());
                params.put("pictures", pictures, "image/jpg");
                /*for (int i = 0; i < pictures.length; i++) {
                    params.put("pictures[" + i + "]", pictures[i], "image/jpg");
                }*/
            }
            if (audio != null && audio.exists()) {

                params.put("audio", audio, "audio/AMR", audio.getName());
            } else {
                //params.put("audio", new File("") , "audio/AMR","");
            }

            //如果发布的是紧急订单，则尝试创建一个临时文件，避免服务端返回multipart/form错误
          /*  if (emergency) {
                try {
                    params.put("file", File.createTempFile("file", "bak"));
                } catch (IOException e) {
                    Log.d(TAG, "publish: 创建临时文件失败");
                }
            }*/
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (comment != null) {
            params.put("comment", comment);
        }
        if (skillIds != null) {
            params.put("skillIds", skillIds);
        }
        if (address != null) {
            params.put("address", address);
        }
        if (access_token != null) {
            params.put("access_token", access_token);
        }
        if (longitude != 0) {
            params.put("longitude", longitude);
        }
        if (latitude != 0) {
            params.put("latitude", latitude);
        }

        params.put("emergency", emergency);
        params.put("audioOrder", audioOrder);


        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.publish;
        HttpService.getData4Post(context, URLConstant.publish, true, key, str,
                params, handle, null);


    }

    /**
     * 获取优惠券
     *
     * @param context
     * @param access_token
     * @param handle
     */
    public void getCoupon(Context context, String access_token, String page,
                          AsyncHandler handle) {

        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("size", "10");
        params.add("page", page);

        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.usable_list;
        HttpService.getData4Post(context, URLConstant.usable_list, true, key,
                str, params, handle, CouponData.class);

    }

    /**
     * 查询当前可用优惠券
     *
     * @param context
     * @param access_token
     * @param page
     * @param handle
     */
    public void usableVoucher(Context context, String access_token,
                              String page, AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("size", "10");
        params.add("page", page);
        // params.add("sort", sort);

        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.usableVoucher;
        HttpService.getData4Post(context, URLConstant.usableVoucher, true, key,
                str, params, handle, new TypeToken<List<CouponDetailData>>() {
                }.getType());

    }

    /**
     * 取消订单
     *
     * @param context
     * @param access_token
     * @param orderId
     * @param cancelType
     * @param cancelReason
     * @param handle
     */
    public void CancelUnclose(Context context, String access_token,
                              String orderId, String cancelType, String cancelReason,
                              AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("orderId", orderId);
        params.add("cancelType", cancelType);
        params.add("cancelReason", cancelReason);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.cancel_published;
        HttpService.getData4Post(context, URLConstant.cancel_published, true,
                key, null, params, handle, null);

    }

    /**
     * 拒绝支付
     *
     * @param context
     * @param access_token
     * @param orderId
     * @param cancelType
     * @param cancelReason
     * @param handle
     */
    public void refused_to_pay(Context context, String access_token,
                               String orderId, String cancelType, String cancelReason,
                               AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("orderId", orderId);
        params.add("refusalType", cancelType);
        params.add("refusalReason", cancelReason);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.refusal;
        HttpService.getData4Post(context, URLConstant.refusal, true, key, null,
                params, handle, null);

    }

    /**
     * 获取我发布的订单
     *
     * @param context
     * @param access_token
     * @param handle
     */
    public void published(Context context, String access_token,
                          AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.published;
        HttpService.getData4Post(context, URLConstant.published, true, key,
                str, params, handle, publishedData.class);
    }

    /**
     * 查询会话
     *
     * @param context
     * @param access_token
     * @param orderId
     * @param sort
     * @param size
     * @param page
     * @param handle
     */
    public void conversation(Context context, String access_token,
                             String orderId, String sort, String size, int page,
                             AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("orderId", orderId);
        params.add("sort", sort);
        params.add("size", size);
        params.add("page", String.valueOf(page));
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = URLConstant.conversationInfo;
        HttpService.getData4Post(context, URLConstant.conversationInfo, true,
                key, str, params, handle, MessageListData.class);
    }

    /**
     * 获取验证码
     *
     * @param context
     * @param mobilePhone
     * @param handle
     */
    public void verify_code(Context context, String mobilePhone,
                            AsyncHandler handle) {
        params = new RequestParams();
        params.add("mobilePhone", mobilePhone);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.verify_code;
        HttpService.getData4Post(context, URLConstant.verify_code, true, key,
                str, params, handle, null);
    }

    /**
     * 发布会话
     */
    public void conversationPublish(Context context, String access_token,
                                    String orderId, String toId, String content, AsyncHandler handle) {

        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("orderId", orderId);
        params.add("toId", toId);
        params.add("content", content);

        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.conversationPublish;
        HttpService.getData4Post(context, URLConstant.conversationPublish,
                true, key, str, params, handle, null);

    }

    /**
     * 发布评价
     */

    public void evaluate(Context context, String access_token, String orderId,
                         String level, String type, String comment, AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("orderId", orderId);
        params.add("level", level);
        params.add("type", type);
        params.add("comment", comment);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.evaluate;
        HttpService.getData4Post(context, URLConstant.evaluate, true, key, str,
                params, handle, null);

    }

    /**
     * 添加地址
     */
    public void AddAddress(Context context, String access_token,
                           AddressData address, AsyncHandler handle) {

        params = new RequestParams();

        params.put("province", address.getProvince());
        params.put("longitude", address.getLongitude());
        params.put("latitude", address.getLatitude());
        params.put("city", address.getCity());
        params.put("district", address.getDistrict());
        params.put("street", address.getStreet());
        params.put("streetNumber", address.getStreetNumber());
        params.put("comment", address.getComment());
        params.put("fixed", "true");
        params.put("access_token", access_token);

        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.addAddress;
        HttpService.getData4Post(context, URLConstant.addAddress, true, key,
                str, params, handle, null);

    }

    /**
     * 修改地址
     */
    public void updateAddress(Context context, String access_token,
                              AddressData address, AsyncHandler handle) {

        params = new RequestParams();
        if (address.getProvince() != null && !address.getProvince().equals("")) {
            params.put("province", address.getProvince());
        }
        if (address.getLongitude() != null
                && !address.getLongitude().equals("")) {
            params.put("longitude", address.getLongitude());
        }
        if (address.getLatitude() != null && !address.getLatitude().equals("")) {
            params.put("latitude", address.getLatitude());
        }
        if (address.getCity() != null && !address.getCity().equals("")) {
            params.put("city", address.getCity());
        }
        if (address.getDistrict() != null && !address.getDistrict().equals("")) {
            params.put("town", address.getDistrict());
        }
        if (address.getStreet() != null && !address.getStreet().equals("")) {
            params.put("road", address.getStreet());
        }
        if (address.getName() != null && !address.getName().equals("")) {
            params.put("street", address.getName());
        }
        if (address.getStreetNumber() != null
                && !address.getStreetNumber().equals("")) {
            params.put("streetNumber", address.getStreetNumber());
        }
        if (address.getComment() != null && !address.getComment().equals("")) {
            params.put("comment", address.getComment());
        }

        params.put("fixed", "true");
        params.put("access_token", access_token);
        params.put("id", address.getId());
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.updateAddress;
        HttpService.getData4Post(context, URLConstant.updateAddress, true, key,
                str, params, handle, null);

    }

    /**
     * 查询地址
     */

    public void selectAddress(Context context, String access_token,
                              AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);

        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.selectAddress;
        HttpService.getData4Post(context, URLConstant.selectAddress, true, key,
                str, params, handle, AddressData.class);
    }

    /**
     * 查询历史订单
     */

    public void selectOrder(Context context, String page, String access_token,
                            AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("sort", "publishTime,desc");
        params.add("size", "10");
        params.add("page", page);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.selectOrder;
        HttpService.getData4Post(context, URLConstant.selectOrder, true, key,
                str, params, handle, selectOrders.class);
    }

    /**
     * 支付宝签名
     *
     * @param context
     * @param access_token
     * @param orderId      客户端主键
     * @param voucherId    优惠券主键
     * @param handle
     */
    public void ZFB_Pay(Context context, String access_token, String orderId,
                        String voucherId, AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("orderId", orderId);
        params.add("voucherId", voucherId);

        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.ZFB_alipay;
        HttpService.getData4Post(context, URLConstant.ZFB_alipay, true, key,
                str, params, handle, null);
    }


    /**
     * 微信签名
     *
     * @param context
     * @param access_token
     * @param orderId      客户端主键
     * @param voucherId    优惠券主键
     * @param handle
     */
    public void WX_Pay(Context context, String access_token, String orderId,
                       String voucherId, AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("orderId", orderId);
        params.add("voucherId", voucherId);

        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.WX_alipay;
        HttpService.getData4Post(context, URLConstant.WX_alipay, true, key,
                str, params, handle, null);

    }

    public void download(Context context, String access_token, String fileId, AsyncHandler handle) {

        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("fileId", fileId);


        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.downLoad;
        HttpService.getData4Post(context, URLConstant.downLoad, true, key,
                str, params, handle, null);
    }

    /**
     * 意见反馈
     */
    public void feelBack(Context context, String access_token, String comment, AsyncHandler handle) {
        params = new RequestParams();
        params.add("access_token", access_token);
        params.add("clientType", "CUSTOMER");
        params.add("deviceType", "ANDROID");
        params.add("comment", comment);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.feelBack;
        HttpService.getData4Post(context, URLConstant.feelBack, true, key,
                str, params, handle, null);
    }

    /**
     * 下载更新
     *
     * @param context
     * @param version
     * @param handle
     */
    public void updateDownLoad(Context context, String version, AsyncHandler handle) {
        params = new RequestParams();
        params.add("clientType", "CUSTOMER");
        params.add("deviceType", "ANDROID");
        params.add("version", version);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.updateDownLoad;
        HttpService.getData4Post(context, URLConstant.updateDownLoad, true, key,
                str, params, handle, UpdateDown.class);
    }


    /**
     * 获取工人实时位置
     */
    public void getPosition(Context context, String processorId, String access_token, AsyncHandler handle) {

        params = new RequestParams();
        params.add("processorId", processorId);
        params.add("access_token", access_token);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.getPosition;
        HttpService.getData4Post(context, URLConstant.getPosition, true, key,
                str, params, handle, Position.class);


    }

    /**
     * 获取消息列表
     */
    public void getNotice(Context context, int size, int page, String access_token, AsyncHandler handle) {
        params = new RequestParams();
        params.add("size", String.valueOf(size));
        params.add("access_token", access_token);
        params.add("page", String.valueOf(page));
        params.add("type", "ALL");
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.getNotice;
        HttpService.getData4Post(context, URLConstant.getNotice, true, key,
                str, params, handle, NoticeDatas.class);
    }

    /**
     * 修改静音状态
     *
     * @param context
     * @param isSinlence
     * @param access_token
     * @param handle
     */
    public void changeSinlence(Context context, boolean isSinlence, String access_token, AsyncHandler handle) {
        params = new RequestParams();

        params.put("access_token", access_token);
        params.put("silence", isSinlence);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.changesinlence;
        HttpService.getData4Post(context, URLConstant.changesinlence, true, key,
                str, params, handle, null);
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @param access_token
     * @param handle
     */
    public void getPersonData(Context context, String access_token, AsyncHandler handle) {
        params = new RequestParams();

        params.put("access_token", access_token);
        String str = "data";// 需要解析的实体对象在json字符串中的属性名称
        // key 的组成部分按照 tag + 平台接口方法名称 来
        String key = TAG + URLConstant.PersonData;
        HttpService.getData4Post(context, URLConstant.PersonData, true, key,
                str, params, handle, PersenData.class);
    }

}
