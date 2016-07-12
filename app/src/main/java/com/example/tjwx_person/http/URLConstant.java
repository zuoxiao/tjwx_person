package com.example.tjwx_person.http;

import java.util.HashMap;
import java.util.Map;

public class URLConstant {

    /**
     * 接口版本号
     */
    public static final String versionName = "1.0";

    /**
     * 服务器域名端口地址
     */
    public static final String HOST = "123.56.198.240";

    public static final int PORT = 8081;

    public static final String PROJECT = "oauth2";

    // public static final String FULLURL = "http://123.56.198.240:8081/oauth2";
    public static final String FULLURL = "http://" + HOST + ":" + PORT + "/"
            + PROJECT;
    /**
     * 服务器域名端测试地址
     */
    // public static final String HOST = "http://10.12.8.174:42010";
    /**
     * 用户管理模块接口
     */
    public static final String peopleApi = HOST + "/api/" + versionName
            + "/peopleApi/";

    // 3.3. 手机注册
    public static final String register = FULLURL + "/user/login";

    public static final String skillList = FULLURL + "/maintenance/list";

    public static final String TOKEN_URL = FULLURL + "/oauth/token";
    // 发布订单
    public static final String publish = FULLURL + "/order/publish";
    // 优惠券
    public static final String usable_list = FULLURL + "/voucher/usable_list";
    // 我的发布订单
    public static final String published = FULLURL + "/order/published/my";
    // 取消订单
    public static final String cancel_published = FULLURL + "/order/cancel";
    // 拒绝支付
    public static final String refusal = FULLURL + "/order/refusal";
    // 获取短信验证码
    public static final String verify_code = FULLURL + "/sms/verify_code";
    // 获取会话
    public static final String conversationInfo = FULLURL
            + "/conversation/info";
    // 发布会话
    public static final String conversationPublish = FULLURL
            + "/conversation/publish";
    // 发布评价
    public static final String evaluate = FULLURL + "/evaluate/publish";

    // 添加地址
    public static final String addAddress = FULLURL + "/address/add";

    // 修改地址
    public static final String updateAddress = FULLURL + "/address/update";

    // 查询地址
    public static final String selectAddress = FULLURL + "/address/fixed";
    // 查询历史订单
    public static final String selectOrder = FULLURL + "/order/list";
    // 支付宝签名
    public static final String ZFB_alipay = "http://123.56.198.240:8083/"
            + "payment/alipay/sign";
    //微信支付
    public static final String WX_alipay = "http://123.56.198.240:8083/"
            + "payment/wechat/sign";
//    public static final String WX_alipay = "http://58.30.212.164:8083/wechat/sign";


    // 可用优惠券
    public static final String usableVoucher = FULLURL + "/voucher/usable";

    //下载文件
    public static final String downLoad = FULLURL + "/file/download/fileId";

    //意见反馈
    public static final String feelBack = FULLURL + "/feedback/add";
    //在线升级
    public static final String updateDownLoad = FULLURL + "/app/update";
    //获取工人实时位置
    public static final String getPosition = FULLURL + "/position/get";
    //获取消息
    public static final String getNotice = FULLURL + "/notification/info";
    //设置静音
    public static final String changesinlence = FULLURL + "/user/changeSinlence";
    //获取用户信息
    public static final String PersonData = FULLURL + "/user/info";
    //用于用户统计
    public static final String statistic = FULLURL + "/statistic/add";

    /**
     * // 网络请求返回数据为空
     */
    public static final int NoNetwork = 0;
    /**
     * // 网络数据返回错误
     */
    public static final int ReturnError = 1;
    /**
     * // json数据解析错误
     */
    public static final int PullError = 2;
    /**
     * // 平台返回的错误信息
     */
    public static final int ServiecError = 3;
    /**
     * //手机无网络
     */
    public static final int NetworkAvailable = 4;
    /**
     * 用户已收藏过
     */
    public static final int CollectionState = 113;
    public static HashMap<Integer, String> map;

    public static void initMessage() {
        map = new HashMap<Integer, String>();
        map.put(NoNetwork, "网络请求返回数据为空");
        map.put(ReturnError, "网络数据返回错误");
        map.put(PullError, "json数据解析错误");
        map.put(NetworkAvailable, "网络连接失败，请检查您的网络连接");
    }

}
