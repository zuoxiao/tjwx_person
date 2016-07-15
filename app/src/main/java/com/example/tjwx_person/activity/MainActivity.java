package com.example.tjwx_person.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.bean.Position;
import com.example.tjwx_person.bean.UpdateDown;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.URLConstant;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.tool.DownLoadDialog;
import com.example.tjwx_person.tool.IsWifi;
import com.example.tjwx_person.tool.voice.SoundMeter;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.Theme;
import com.example.tjwx_person.utils.UserData;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements OnClickListener {

    private ImageView volume;
    private Handler mHandler = new Handler();
    /**
     * 录音
     */
    private SoundMeter mSensor;
    TimeCount time;
    int timeData;
    boolean isfinish;
    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    // 定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;
    private AddressData addresDetail = new AddressData();
    List<Marker> marker = new ArrayList<Marker>();
    // private long startVoiceT, endVoiceT;
    private String voiceName;
    private int flag = 1;
    private boolean isShosrt = false;
    private Context mContext;
    // 当前订单状态
    public String state = "";
    public publishedData publised;
    // 上传信息
    double location_lat;
    double location_lot;
    String voicePath;
    String Address;
    UserAction userAction;
    public static boolean isRush = false;
    public static boolean isRefreshXG = true;
    String ACTION = "com.example.tjwx_person.activity.MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MobclickAgent.UMAnalyticsConfig config=new MobclickAgent.UMAnalyticsConfig(this,"","");
//        MobclickAgent. startWithConfigure(config);
        isRefreshXG = true;
        mContext = this;
        setContentView(R.layout.activity_map);
        initView();
        initLocation();
        published();
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(this.getPackageName(), 0);

            float currentVersion = Float.valueOf(info.versionName);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            float lastVersion = prefs.getFloat("VERSION_KEY", 0);
            if (currentVersion > lastVersion) {
                //如果当前版本大于上次版本，该版本属于第一次启动
                userAction.setStatistic(mContext, new AsyncHandler() {
                    @Override
                    public void onSuccess(Object obj) {

                    }

                    @Override
                    public void onFailure(int statusCode, String message) {

                    }
                });
                //将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动

                prefs.edit().putFloat("VERSION_KEY", currentVersion).commit();
                if (currentVersion != 1.0) {
                    userAction.setStatisticUpdate(mContext, new AsyncHandler() {
                        @Override
                        public void onSuccess(Object obj) {

                        }

                        @Override
                        public void onFailure(int statusCode, String message) {

                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(MyBroadcastReceiver, filter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    private ImageView headbtn_user, headbtn_urgent;
    private TextView text_urgent, btn_repair, text_user;
    private LinearLayout ll_btn_user, ll_btn_urgent;
    private RelativeLayout rl_voice, map_title, rl_map_notice, map_order;
    private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
            voice_rcd_hint_tooshort, voice_isNo;
    private TextView voice_time;
    private LinearLayout del_re;
    private View rcChat_popup;
    private TextView order_title, order_cancel;
    private TextView notice_text1, notice_text2;
    private ImageView main_location;

    //工人接单
    private RelativeLayout map_dissuccess_rel;
    private ImageView dis_success_img;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    TextView disSuccess_name, disSuccess_id;
    TextView disSuccess_cancel, disSuccess_message, disSuccess_phone;

    private void initView() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.worke_img)
                .showImageForEmptyUri(R.drawable.worke_img)
                .showImageOnFail(R.drawable.worke_img)
                // .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(100)).cacheOnDisc(false)
                .considerExifParams(true).build();
        userAction = UserAction.getInstance();
        voice_rcd_hint_rcding = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_rcding);
        voice_rcd_hint_loading = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_loading);
        voice_rcd_hint_tooshort = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_tooshort);
        voice_isNo = (LinearLayout) findViewById(R.id.voice_isNo);
        voice_time = (TextView) findViewById(R.id.voice_time);
        rcChat_popup = this.findViewById(R.id.rcChat_popup);
        volume = (ImageView) this.findViewById(R.id.volume);
        del_re = (LinearLayout) this.findViewById(R.id.del_re);
        headbtn_user = (ImageView) findViewById(R.id.headbtn_user);
        headbtn_urgent = (ImageView) findViewById(R.id.headbtn_urgent);
        text_urgent = (TextView) findViewById(R.id.text_urgent);
        btn_repair = (TextView) findViewById(R.id.btn_repair);
        text_user = (TextView) findViewById(R.id.text_user);
        ll_btn_user = (LinearLayout) findViewById(R.id.ll_btn_user);
        ll_btn_urgent = (LinearLayout) findViewById(R.id.ll_btn_urgent);
        rl_voice = (RelativeLayout) findViewById(R.id.rl_voice);
        map_title = (RelativeLayout) findViewById(R.id.map_title);
        rl_map_notice = (RelativeLayout) findViewById(R.id.rl_map_notice);
        map_order = (RelativeLayout) findViewById(R.id.map_order);
        order_title = (TextView) findViewById(R.id.order_title);
        order_cancel = (TextView) findViewById(R.id.order_cancel);
        notice_text1 = (TextView) findViewById(R.id.notice_text1);
        notice_text2 = (TextView) findViewById(R.id.notice_text2);
        main_location = (ImageView) findViewById(R.id.main_location);
        map_dissuccess_rel = (RelativeLayout) findViewById(R.id.map_dissuccess_rel);
        dis_success_img = (ImageView) findViewById(R.id.dis_success_img);
        disSuccess_name = (TextView) findViewById(R.id.disSuccess_name);
        disSuccess_id = (TextView) findViewById(R.id.disSuccess_id);
        disSuccess_cancel = (TextView) findViewById(R.id.disSuccess_cancel);
        disSuccess_message = (TextView) findViewById(R.id.disSuccess_message);
        disSuccess_phone = (TextView) findViewById(R.id.disSuccess_phone);
        disSuccess_cancel.setOnClickListener(this);
        disSuccess_message.setOnClickListener(this);
        disSuccess_phone.setOnClickListener(this);
        ll_btn_user.setOnClickListener(this);
        btn_repair.setOnClickListener(this);
        ll_btn_urgent.setOnClickListener(this);
        order_cancel.setOnClickListener(this);
        main_location.setOnClickListener(this);
        Theme.setTextSize(text_urgent, Theme.title_small);
        Theme.setTextSize(text_user, Theme.title_small);
        Theme.setTextSize(btn_repair, Theme.title_big);
        Theme.setTextSize(order_title, Theme.title_big);
        Theme.setTextSize(order_cancel, Theme.title_small);

        Theme.setViewSize(headbtn_user, Theme.pix(35), Theme.pix(40));
        Theme.setViewSize(headbtn_urgent, Theme.pix(35), Theme.pix(40));

        Theme.setViewSize(ll_btn_urgent, Theme.pix(30), Theme.pix(30));
        Theme.setViewSize(map_title, ViewGroup.LayoutParams.MATCH_PARENT,
                Theme.pix(90));
        Theme.setViewSize(map_order, ViewGroup.LayoutParams.MATCH_PARENT,
                Theme.pix(90));
        Theme.setViewSize(ll_btn_user, Theme.pix(140), Theme.pix(70));
        Theme.setViewSize(ll_btn_urgent, Theme.pix(140), Theme.pix(70));
        Theme.setViewSize(btn_repair, ViewGroup.LayoutParams.MATCH_PARENT,
                Theme.pix(70));
        Theme.setViewMargin(headbtn_user, Theme.pix(30), 0, 0, 0);
        Theme.setViewMargin(order_cancel, 0, 0, Theme.pix(30), 0);
        rl_voice.setOnTouchListener(onTouchlisten);
        mSensor = SoundMeter.getSoundMeter(this);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mMapView.showZoomControls(false);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(10);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker == markerPositon) {
                    TextView textView = new TextView(mContext);
                    textView.setText(position.getAddress());
                    LatLng ll = marker.getPosition();
                    InfoWindow mInfoWindow = new InfoWindow(textView, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }


                return false;
            }
        });


        Theme.setViewTopMargin(mMapView, Theme.pix(90));
        Theme.setViewTopMargin(rl_map_notice, Theme.pix(90));
        initXGPushManager();

    }

    private static final int POLL_INTERVAL = 300;
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
                volume.setImageResource(R.drawable.amp1);
                break;
            case 2:
            case 3:
                volume.setImageResource(R.drawable.amp2);

                break;
            case 4:
            case 5:
                volume.setImageResource(R.drawable.amp3);
                break;
            case 6:
            case 7:
                volume.setImageResource(R.drawable.amp4);
                break;
            case 8:
            case 9:
                volume.setImageResource(R.drawable.amp5);
                break;
            case 10:
            case 11:
                volume.setImageResource(R.drawable.amp6);
                break;
            default:
                volume.setImageResource(R.drawable.amp6);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_voice:

                break;
            case R.id.ll_btn_user:
                if (UserData.getIsLogin(getApplicationContext())) {
                    Intent person = new Intent(this, PersonAcivity.class);
                    startActivity(person);
                } else {
                    Intent regist = new Intent(this, RegistActivity.class);
                    startActivity(regist);
                }

                break;
            case R.id.btn_repair://普通维修

                Intent repair = new Intent(this, RepairActivity.class);
                repair.putExtra("addresDetail", addresDetail);
                startActivity(repair);


                break;
            case R.id.ll_btn_urgent://紧急维修

                Intent EmergencyRepair = new Intent(this,
                        EmergencyRepairActivity.class);
                EmergencyRepair.putExtra("location_lat", location_lat);
                EmergencyRepair.putExtra("location_lot", location_lot);
                startActivity(EmergencyRepair);

                break;
            case R.id.order_cancel:
                if (state.equals("PUBLISH")) {
                    Intent PUBLISH = new Intent(this, CancelUnclosedActivity.class);
                    PUBLISH.putExtra("publised", publised);
                    startActivity(PUBLISH);
                } else {
                    published();
                }
                break;
            case R.id.main_location:

                mLocationClient.start();
                break;
            case R.id.disSuccess_cancel:
                Intent cancel = new Intent(this, CancelClosedActivity.class);
                cancel.putExtra("publised", publised);
                startActivity(cancel);
                break;
            case R.id.disSuccess_message:
                Intent message = new Intent(this, MessageActivity.class);
                message.putExtra("publised", publised);
                startActivity(message);

                break;
            case R.id.disSuccess_phone:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.DIAL");
                intent1.setData(Uri.parse("tel:" + publised.getProcessorMobilePhone()));
                startActivity(intent1);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        if (isRush) {
            isRush = false;
            published();

        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initXGPushManager();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        MobclickAgent.onPause(this);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        myListener = new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {

                try {
                    LatLng lat = new LatLng(location.getLatitude(),
                            location.getLongitude());// 初始化地图定位坐标位置
                    Address = location.getAddrStr();
                    location_lat = location.getLatitude();
                    location_lot = location.getLongitude();

                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(lat).zoom(18.0f);

                    BitmapDescriptor bdp = BitmapDescriptorFactory
                            .fromResource(R.drawable.map_me);
                    OverlayOptions op = new MarkerOptions().position(lat).icon(bdp)
                            .zIndex(17);
                    Marker marker = (Marker) mBaiduMap.addOverlay(op);
                    // mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(lat));
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                            .newMapStatus(builder.build()));
                    mLocationClient.stop();

                    // 保存信息
                    UserData.setSettingString(mContext, UserData.User_address,
                            location.getAddrStr());
                    UserData.setSettingString(mContext, UserData.User_lat,
                            String.valueOf(location.getLatitude()));
                    UserData.setSettingString(mContext, UserData.User_lot,
                            String.valueOf(location.getLongitude()));
                    addresDetail.setAddress(location.getAddrStr());
                    addresDetail.setLatitude(location.getLatitude());
                    addresDetail.setLongitude(location.getLongitude());
                } catch (NullPointerException ee) {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            isfinish = true;
            time = null;
            rl_voice.setBackgroundResource(R.drawable.voice_bg1);
            voice_rcd_hint_rcding.setVisibility(View.GONE);
            voice_time.setVisibility(View.GONE);
            stop();
            flag = 1;
            // int time = (int) ((endVoiceT - startVoiceT) / 1000);
            Log.d("time", String.valueOf(time));
            if (timeData < 1) {
                isShosrt = true;
                voice_rcd_hint_loading.setVisibility(View.GONE);
                voice_rcd_hint_rcding.setVisibility(View.GONE);
                voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        voice_rcd_hint_tooshort.setVisibility(View.GONE);
                        rcChat_popup.setVisibility(View.GONE);
                        isShosrt = false;
                    }
                }, 50);

            } else {
                voicePath = Environment.getExternalStorageDirectory() + "/"
                        + voiceName;
                if (Utils.isNetworkAvailable(mContext)) {
                    if (!"".equals(UserData
                            .getSettingString(mContext, UserData.user_ID))) {
                        publish();
                    } else {
                        Intent regist = new Intent(MainActivity.this, RegistActivity.class);
                        regist.putExtra("Regist_type", 1);
                        startActivityForResult(regist, 4);

                    }

                } else {
                    BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                }

            }

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            timeData = (int) (60 - (millisUntilFinished / 1000));
            if (millisUntilFinished / 1000 < 20) {
                voice_time.setVisibility(View.VISIBLE);
                voice_time.setText(String.valueOf(millisUntilFinished / 1000)
                        + "秒");

            }
            Log.d("time", String.valueOf(timeData));
        }
    }

    private void start(String name) {
        mSensor.start(name);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        volume.setImageResource(R.drawable.amp1);
    }

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stop();
        }
    };

    // 按下语音录制按钮时
    OnTouchListener onTouchlisten = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (!Environment.getExternalStorageDirectory().exists()) {

                return false;
            }
            if (Utils.isNetworkAvailable(mContext)) try {
                {


                    System.out.println("1");
                    int[] location = new int[2];
                    rl_voice.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
                    int btn_rc_Y = location[1];
                    int btn_rc_X = location[0];
                    int[] del_location = new int[2];
                    del_re.getLocationInWindow(del_location);
                    int del_Y = del_location[1];
                    int del_x = del_location[0];
                    if (event.getAction() == MotionEvent.ACTION_DOWN
                            && flag == 1) {
                        if (!Environment.getExternalStorageDirectory().exists()) {

                            return false;
                        }
                        System.out.println("2");

                        if (event.getRawY() > btn_rc_Y
                                && event.getRawX() > btn_rc_X) {// 判断手势按下的位置是否是语音录制按钮的范围内
                            if (time == null) {
                                time = new TimeCount(60 * 1000, 500);
                                time.start();
                                timeData = 0;
                            }
                            isfinish = false;
                            System.out.println("3");
                            rl_voice.setBackgroundResource(R.drawable.voice_bg2);
                            rcChat_popup.setVisibility(View.VISIBLE);
                            voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                            voice_rcd_hint_rcding.setVisibility(View.GONE);
                            voice_rcd_hint_tooshort.setVisibility(View.GONE);
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    if (!isShosrt) {
                                        voice_rcd_hint_loading
                                                .setVisibility(View.GONE);
                                        voice_rcd_hint_rcding
                                                .setVisibility(View.VISIBLE);
                                    }
                                }
                            }, 300);

                            del_re.setVisibility(View.GONE);
                            long startVoiceT = SystemClock
                                    .currentThreadTimeMillis();
                            voiceName = startVoiceT + ".amr";
                            start(voiceName);
                            flag = 2;
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP
                            && flag == 2) {// 松开手势时执行录制完成
                        System.out.println("4");
                        if (!isfinish) {// 假如倒计时完成已发送一遍就不再进行发送
                            if (event.getRawY() < btn_rc_Y
                                    || event.getRawX() < btn_rc_X) {
                                rl_voice.setBackgroundResource(R.drawable.voice_bg1);
                                voice_rcd_hint_rcding.setVisibility(View.GONE);
                                stop();
                                flag = 1;
                                File file = new File(
                                        Environment
                                                .getExternalStorageDirectory()
                                                + "/" + voiceName);
                                if (file.exists()) {
                                    file.delete();
                                }

                            } else {
                                rl_voice.setBackgroundResource(R.drawable.voice_bg1);
                                voice_rcd_hint_rcding.setVisibility(View.GONE);
                                stop();
                                // endVoiceT =
                                // SystemClock.currentThreadTimeMillis();
                                flag = 1;
                                // int time = (int) ((endVoiceT - startVoiceT) /
                                // 1000);
                                Log.d("time", String.valueOf(timeData));
                                if (timeData < 3) {
                                    voicePath = Environment
                                            .getExternalStorageDirectory()
                                            + "/" + voiceName;
                                    showDialog();
                                    isShosrt = true;
                                    voice_rcd_hint_loading
                                            .setVisibility(View.GONE);
                                    voice_rcd_hint_rcding
                                            .setVisibility(View.GONE);
                                    //                                voice_rcd_hint_tooshort
                                    //                                        .setVisibility(View.VISIBLE);
                                    mHandler.postDelayed(new Runnable() {
                                        public void run() {
                                            voice_rcd_hint_tooshort
                                                    .setVisibility(View.GONE);
                                            rcChat_popup
                                                    .setVisibility(View.GONE);
                                            isShosrt = false;
                                        }
                                    }, 500);
                                    time.cancel();
                                    time = null;
                                    return false;
                                } else {
                                    voicePath = Environment
                                            .getExternalStorageDirectory()
                                            + "/" + voiceName;
                                    mHandler.postDelayed(new Runnable() {
                                        public void run() {
                                            voice_rcd_hint_rcding.setVisibility(View.GONE);
                                        }
                                    }, 300);
                                    voice_rcd_hint_rcding
                                            .setVisibility(View.GONE);
                                    if (Utils.isNetworkAvailable(mContext)) {
                                        if (!"".equals(UserData
                                                .getSettingString(mContext, UserData.user_ID))) {
                                            isPublished = false;
                                            publish();
                                        } else {
                                            isPublished = true;
                                            Intent regist = new Intent(MainActivity.this, RegistActivity.class);
                                            regist.putExtra("Regist_type", 1);
                                            startActivityForResult(regist, 4);

                                        }

                                    } else {
                                        BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                                    }
                                }

                            }

                            time.cancel();
                            time = null;
                        }
                    }
                    if (event.getRawY() < btn_rc_Y
                            || event.getRawX() < btn_rc_X) {// 手势按下的位置不在语音录制按钮的范围内

                        del_re.setVisibility(View.VISIBLE);
                        voice_isNo.setVisibility(View.GONE);
                        voice_time.setVisibility(View.GONE);
                        if (event.getRawY() >= del_Y
                                && event.getRawY() <= del_Y
                                + del_re.getHeight()
                                && event.getRawX() >= del_x
                                && event.getRawX() <= del_x + del_re.getWidth()) {

                        }
                    } else {
                        del_re.setVisibility(View.GONE);
                        voice_isNo.setVisibility(View.VISIBLE);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            else {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                }
            }
            return true;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 4) {//未登陆时登陆以后返回的
            if (!"".equals(UserData
                    .getSettingString(mContext, UserData.user_ID))) {

                published();
            }else {
                BaseToast.makeShortToast(mContext,"自动提交失败，请重试");
            }
        }
    }

    /**
     * 发布订单
     */
    private void publish() {
        startProgressDialog();
        if (!"".equals(UserData
                .getSettingString(mContext, UserData.user_ID))) {
            userAction.publish(this, null, new File(voicePath), null, false, null,
                    Address, location_lot, location_lat,
                    UserData.getSettingString(mContext, UserData.access_token), true,
                    new AsyncHandler() {

                        @Override
                        public void onSuccess(Object obj) {
                            stopProgressDialog();
                            BaseToast.makeLongToast(
                                    mContext, "发布成功");
                            published();
                            UserData.setSettingBoolean(mContext,
                                    UserData.isPublish, true);
                            Log.d("", "");

                        }

                        @Override
                        public void onFailure(int statusCode, String message) {

                            if (message != null && !"".equals(message)) {
                                BaseToast.makeLongToast(
                                        mContext, message);
                            } else {
                                BaseToast.makeLongToast(
                                        mContext, "发布失败，请稍后再试");
                            }
                            stopProgressDialog();

                            Log.d("", "");

                        }
                    });
        } else {

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (UserData.getSettingBoolean(mContext, UserData.isPublish)) {
            map_order.setVisibility(View.VISIBLE);
            rl_map_notice.setVisibility(View.VISIBLE);
            map_title.setVisibility(View.GONE);
            rl_voice.setVisibility(View.GONE);
            state = UserData.getSettingString(mContext, UserData.state);
        } else {
            map_order.setVisibility(View.GONE);
            rl_map_notice.setVisibility(View.GONE);
            map_title.setVisibility(View.VISIBLE);
            rl_voice.setVisibility(View.VISIBLE);
        }
        if (UserData.getSettingBoolean(mContext, UserData.isRefreshMain)) {
            try {
                UserData.setSettingBoolean(mContext, UserData.isRefreshMain, false);
                stopGetPosition();
                map_order.setVisibility(View.GONE);
                map_title.setVisibility(View.VISIBLE);
                rl_map_notice.setVisibility(View.GONE);
                rl_voice.setVisibility(View.VISIBLE);
                UserData.setSettingBoolean(mContext,
                        UserData.isPublish, false);
                if (marker != null && marker.size() > 0) {
                    for (int i = 0; i < marker.size(); i++) {
                        marker.get(i).remove();
                    }
                    mBaiduMap.hideInfoWindow();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (!"".equals(UserData
                .getSettingString(mContext, UserData.user_ID))) {
            published();
        }


    }

    boolean isPublished = false;//是否是首次登陆后自动发送订单

    /**
     * 查询订单
     */
    private void published() {

        try {
            String access_token = UserData.getSettingString(mContext,
                    UserData.access_token);

            startProgressDialog();
            userAction.published(mContext, access_token, new AsyncHandler() {

                @Override
                public void onSuccess(Object obj) {
                    stopProgressDialog();
                    if (obj != null && !"".equals(obj)) {
                        isPublished = false;
                        int worker = 0;
                        publised = (publishedData) obj;

                        if (publised.getPositions() != null) {
                            worker = publised.getPositions().size();
                            if (worker != 0) {
                                BitmapDescriptor bdp = BitmapDescriptorFactory
                                        .fromResource(R.drawable.worker_icon);
                                for (int i = 0; i < worker; i++) {
                                    LatLng lat = new LatLng(publised
                                            .getPositions().get(i)
                                            .getLatitude(), publised
                                            .getPositions().get(i)
                                            .getLongitude());// 初始化地图定位坐标位置
                                    MarkerOptions op = new MarkerOptions()
                                            .position(lat).icon(bdp).zIndex(17);
                                    marker.add((Marker) mBaiduMap.addOverlay(op));
                                    ;
                                }

                            }
                        }
                        map_order.setVisibility(View.VISIBLE);
                        rl_map_notice.setVisibility(View.VISIBLE);
                        map_title.setVisibility(View.GONE);
                        state = publised.getState();
                        UserData.setSettingString(mContext, UserData.state,
                                state);
                        notice_text1.setText("已为您通知了附近的" + worker + "个维修工人");
                        order_cancel.setVisibility(View.VISIBLE);
                        rl_voice.setVisibility(View.GONE);
                        if (state != null && !"".equals(state)) {
                            map_dissuccess_rel.setVisibility(View.GONE);
                            if (state.equals("PICKER")) {//已被捡取
                                startGetPosition();
                                //                            Intent intent = new Intent(MainActivity.this,
                                //                                    DisSuccessActivity.class);
                                //                            intent.putExtra("publised", publised);
                                //                            MainActivity.this.startActivity(intent);
                                //  MainActivity.this.finish();

                                disSuccess_name.setText(publised.getProcessorName());
                                disSuccess_id.setText(publised.getId());
                                String access_token = UserData.getSettingString(mContext,
                                        UserData.access_token);
                                imageLoader.displayImage(URLConstant.FULLURL + publised.getProcessorHeadPortraitUrl() + "&access_token=" + access_token, dis_success_img, options, null);
                                order_cancel.setVisibility(View.GONE);

                            } else if (state.equals("COMMIT")) {//已提交金额
                                stopGetPosition();
                                Intent pay = new Intent(MainActivity.this,
                                        PayActivity.class);
                                pay.putExtra("publised", publised);
                                MainActivity.this.startActivity(pay);
                                //   MainActivity.this.finish();
                            } else {

                                stopGetPosition();
                            }
                        }

                        UserData.setSettingBoolean(mContext,
                                UserData.isPublish, true);
                    } else {
                        stopGetPosition();
                        map_order.setVisibility(View.GONE);
                        map_title.setVisibility(View.VISIBLE);
                        rl_map_notice.setVisibility(View.GONE);
                        rl_voice.setVisibility(View.VISIBLE);
                        UserData.setSettingBoolean(mContext,
                                UserData.isPublish, false);
                        if (marker != null) {
                            for (int i = 0; i < marker.size(); i++) {
                                marker.get(i).remove();
                            }

                        }
                        mBaiduMap.hideInfoWindow();
                        if (isPublished) {
                            publish();
                            isPublished = false;
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, String message) {
                    stopProgressDialog();
                    Log.d("published", "onFailure");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            stopProgressDialog();
        }


    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("报单时间过短，是否继续发送？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (Utils.isNetworkAvailable(mContext)) {
                    if (!"".equals(UserData
                            .getSettingString(mContext, UserData.user_ID))) {
                        isPublished = false;
                        publish();
                    } else {
                        isPublished = true;
                        Intent regist = new Intent(MainActivity.this, RegistActivity.class);
                        regist.putExtra("Regist_type", 1);
                        startActivityForResult(regist, 4);

                    }

                } else {
                    BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句

    }

    private void initXGPushManager() {
        if (isRefreshXG) {

            XGPushManager.registerPush(mContext,
                    UserData.getSettingString(mContext, UserData.user_phone),
                    new XGIOperateCallback() {

                        @Override
                        public void onSuccess(Object arg0, int arg1) {
                            Log.d("", "");
                        }

                        @Override
                        public void onFail(Object arg0, int arg1, String arg2) {

                            Log.d("", "");
                        }
                    });
            isRefreshXG = false;
        }


    }

    private BroadcastReceiver MyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            published();
        }
    };


    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            getPosition();
            return false;
        }
    });
    Timer timer;

    /**
     * 开始获取工人经纬度
     */
    private void startGetPosition() {
        map_dissuccess_rel.setVisibility(View.VISIBLE);
        if (timer == null) {
            timer = new Timer(true);
            timer.schedule(task, 1000, 300 * 1000); //延时1000ms后执行，1000ms执行一次
        }

    }

    private void stopGetPosition() {
        if (timer != null) {
            try {
                map_dissuccess_rel.setVisibility(View.GONE);
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    Marker markerPositon;
    Position position;

    private void getPosition() {
        String access_token = UserData.getSettingString(mContext,
                UserData.access_token);

        userAction.getPosition(mContext, publised.getProcessorId(), access_token, new AsyncHandler() {
                    @Override
                    public void onSuccess(Object obj) {
                        try {
                            if (obj != null) {
                                markerPositon.remove();
                                mBaiduMap.hideInfoWindow();
                                position = (Position) obj;
                                BitmapDescriptor bdp = BitmapDescriptorFactory
                                        .fromResource(R.drawable.worker_icon);
                                LatLng lat = new LatLng(position
                                        .getLatitude(), position
                                        .getLongitude());// 初始化地图定位坐标位置
                                MarkerOptions op = new MarkerOptions()
                                        .position(lat).icon(bdp).zIndex(17);
                                markerPositon = (Marker) mBaiduMap.addOverlay(op);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, String message) {

                    }
                }

        );


    }



}
