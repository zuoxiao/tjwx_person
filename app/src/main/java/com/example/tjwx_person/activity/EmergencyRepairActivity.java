package com.example.tjwx_person.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.activity.OrderDetailActivity.TimeCount;
import com.example.tjwx_person.adapter.EmergencyRepair_TypeAdapter;
import com.example.tjwx_person.adapter.Repair_TypeAdapter;
import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.bean.OrderData;
import com.example.tjwx_person.bean.TypeData;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.bean.skillData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.tool.voice.SoundMeter;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.UserData;

/**
 * 紧急订单
 *
 * @author zuo
 */
public class EmergencyRepairActivity extends BaseActivity {
    private Context mContext;
    UserAction userAction;
    private List<skillData> typeDatas = new ArrayList<skillData>();
    public HashMap<Integer, Boolean> isSelected = new HashMap<>();
    private EmergencyRepair_TypeAdapter adapter;
    // 上传的信息
    AddressData address;
    // OrderData orderData = null;
    String voiceData = null;
    /**
     * 录音
     */
    private SoundMeter mSensor;
    private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
            voice_rcd_hint_tooshort, voice_isNo;
    private TextView voice_time;
    private LinearLayout del_re;
    private View rcChat_popup;
    private int flag = 1;
    private boolean isShosrt = false;
    private ScrollView order_scroll;
    // private long startVoiceT, endVoiceT;
    private String voiceName;
    private ImageView volume;
    private Handler mHandler = new Handler();
    TimeCount time;
    int timeData;
    boolean isfinish;
    private TextView voice_btn;
    double location_lat;
    double location_lot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_emergency_repair, 3, "紧急维修");
        mContext = this;
        initView();
    }

    private ListView repair_type;
    private RelativeLayout repair_gpsAddress;
    private TextView repair_gpsAddress_text, repair_pulish, repair_cancel;
    private EditText emergency_comment;

    private void initView() {
        // voice_rcd_hint_rcding = (LinearLayout) this
        // .findViewById(R.id.voice_rcd_hint_rcding);
        // voice_rcd_hint_loading = (LinearLayout) this
        // .findViewById(R.id.voice_rcd_hint_loading);
        // voice_rcd_hint_tooshort = (LinearLayout) this
        // .findViewById(R.id.voice_rcd_hint_tooshort);
        // voice_isNo = (LinearLayout) this.findViewById(R.id.voice_isNo);
        // voice_time = (TextView) findViewById(R.id.voice_time);
        // volume = (ImageView) this.findViewById(R.id.volume);
        // del_re = (LinearLayout) this.findViewById(R.id.del_re);
        // order_scroll = (ScrollView) findViewById(R.id.order_scroll);
        // voice_btn = (TextView) findViewById(R.id.voice_btn);
        // emergency_comment = (EditText) findViewById(R.id.emergency_comment);
        // rcChat_popup = this.findViewById(R.id.rcChat_popup);
        // mSensor = new SoundMeter(this);
        // voice_btn.setOnTouchListener(onTouchlisten);
        //

        // repair_gpsAddress_text = (TextView)
        // findViewById(R.id.repair_gpsAddress_text);
        //
        // repair_gpsAddress = (RelativeLayout)
        // findViewById(R.id.repair_gpsAddress);
        // repair_gpsAddress.setOnClickListener(this);
        //
        //

        //
        // repair_type.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        //
        // if (isSelected.get(position) == false) {
        // init();
        // isSelected.put(position, true);
        //
        // } else {
        // init();
        // isSelected.put(position, false);
        //
        // }
        // adapter.notifyDataSetChanged();
        // }
        // });
        //
        // userAction.getTypes(mContext,
        // UserData.getSettingString(mContext, UserData.xg_token),
        // UserData.getSettingString(mContext, UserData.access_token),
        // true, new AsyncHandler() {
        //
        // @Override
        // public void onSuccess(Object obj) {
        // List<TypeData> typeDatass = (List<TypeData>) obj;
        // if (typeDatas != null) {
        // typeDatas.clear();
        // }
        // typeDatas.addAll(typeDatass);
        //
        // repair_type.measure(0, 0);
        // int height = repair_type.getMeasuredHeight();
        // LayoutParams params = new LayoutParams(
        // LayoutParams.FILL_PARENT, height
        // * typeDatas.size());
        // repair_type.setLayoutParams(params);
        //
        // for (int i = 0; i < typeDatas.size(); i++) {
        // isSelected.put(i, false);
        // }
        // Log.d("", "");
        // }
        //
        // @Override
        // public void onFailure(int statusCode, String message) {
        // // TODO Auto-generated
        // // method stub
        //
        // }
        // });
        try {
            location_lat=getIntent().getDoubleExtra("location_lat",0);
            location_lot=getIntent().getDoubleExtra("location_lot",0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userAction = new UserAction();
        repair_pulish = (TextView) findViewById(R.id.repair_pulish);
        repair_cancel = (TextView) findViewById(R.id.repair_cancel);
        repair_type = (ListView) findViewById(R.id.repair_type);
        repair_pulish.setOnClickListener(this);
        repair_cancel.setOnClickListener(this);
        adapter = new EmergencyRepair_TypeAdapter(mContext, typeDatas,
                isSelected);

        repair_type.setAdapter(adapter);
        userAction.getTypes(mContext,
                UserData.getSettingString(mContext, UserData.xg_token),
                UserData.getSettingString(mContext, UserData.access_token),true,
                true, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        List<skillData> typeDatass = (List<skillData>) obj;
                        if (typeDatas != null) {
                            typeDatas.clear();
                        }
                        typeDatas.addAll(typeDatass);
                        repair_type.measure(0, 0);
                        int height = repair_type.getMeasuredHeight();
                        LayoutParams params = new LayoutParams(
                                LayoutParams.FILL_PARENT, height
                                * typeDatas.size());
                        repair_type.setLayoutParams(params);
                        for (int i = 0; i < typeDatas.size(); i++) {
                            isSelected.put(i, false);
                        }
                        Log.d("", "");
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        // TODO Auto-generated
                        // method stub

                    }
                });
        repair_type.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (isSelected.get(position) == false) {
                    init();
                    isSelected.put(position, true);

                } else {
                    init();
                    isSelected.put(position, false);

                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void init() {

        for (int i = 0; i < isSelected.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();

    }
    String skillIds=null;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.repair_gpsAddress:

                Intent address = new Intent(this, AddressActivity.class);
                startActivityForResult(address, 1);
                break;
            case R.id.repair_pulish:

                for (int i = 0; i < typeDatas.size(); i++) {
                    if (isSelected.get(i)) {
                        skillIds = typeDatas.get(i).getId();
                    }
                }
                if (skillIds != null) {
                if (Utils.isNetworkAvailable(mContext)) {
                    if (!"".equals(UserData
                            .getSettingString(mContext, UserData.user_ID))) {
                        publish();
                    } else {
                        Intent regist = new Intent(this, RegistActivity.class);
                        regist.putExtra("Regist_type", 2);
                        startActivityForResult(regist, 4);
                    }

                } else {
                    BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                }
                }else {
                    BaseToast.makeLongToast(mContext, "请选择维修类型");
                }
                break;
            case R.id.repair_cancel:
                this.finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            address = (AddressData) data.getSerializableExtra("address");
            repair_gpsAddress_text.setText(address.getAddress());
            Log.d("", "");
        }
        if (resultCode == 4) {
            if (!"".equals(UserData
                    .getSettingString(mContext, UserData.user_ID))) {
                published();
            }
        }

    }

    /**
     * 发布
     */
    private void publish() {
        // File[] pictures = null;
        File audio = null;

        String skillIds = null;

        for (int i = 0; i < typeDatas.size(); i++) {
            if (isSelected.get(i)) {
                skillIds = typeDatas.get(i).getId();
            }
        }
        if (voiceData != null) {
            audio = new File(voiceData);
        }
        startProgressDialog();
        userAction.publish(mContext, null, null, null, true, skillIds, null, location_lot, location_lat,
                UserData.getSettingString(mContext, UserData.access_token), false,
                new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        com.example.tjwx_person.utils.BaseToast.makeLongToast(
                                mContext, "发布成功");
                        EmergencyRepairActivity.this.finish();
                        UserData.setSettingBoolean(mContext,
                                UserData.isPublish, true);
                        UserData.setSettingString(mContext, UserData.state,
                                "PUBLISH");
                        MainActivity.isRush = true;
                        Log.d("", "");
                        stopProgressDialog();
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        if (message != null && !"".equals(message)) {
                            BaseToast.makeShortToast(mContext, message);
                        } else {
                            com.example.tjwx_person.utils.BaseToast
                                    .makeLongToast(mContext, "发布失败，请稍后再试");
                        }
                        Log.d("", "");
                        stopProgressDialog();
                    }
                });
    }

    /**
     * 语音功能
     */

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            isfinish = true;
            time = null;
            voice_btn.setBackgroundResource(R.drawable.text_fillet_bg);
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
                }, 500);

            } else {
                voiceData = Environment.getExternalStorageDirectory() + "/"
                        + voiceName;

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

        }
    }

    // 按下语音录制按钮时
    OnTouchListener onTouchlisten = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (!Environment.getExternalStorageDirectory().exists()) {

                return false;
            }

            order_scroll.requestDisallowInterceptTouchEvent(true);
            System.out.println("1");
            int[] location = new int[2];
            voice_btn.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
            int btn_rc_Y = location[1];
            int btn_rc_X = location[0];
            int[] del_location = new int[2];
            del_re.getLocationInWindow(del_location);
            int del_Y = del_location[1];
            int del_x = del_location[0];
            if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
                if (!Environment.getExternalStorageDirectory().exists()) {

                    return false;
                }
                System.out.println("2");

                if (event.getRawY() > btn_rc_Y && event.getRawX() > btn_rc_X) {// 判断手势按下的位置是否是语音录制按钮的范围内
                    System.out.println("3");
                    if (time == null) {
                        time = new TimeCount(60 * 1000, 500);
                        time.start();
                        if (voiceData != null) {
                            File file = new File(voiceData);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                    }
                    voice_btn
                            .setBackgroundResource(R.drawable.text_fillet_deep_bg);
                    isfinish = false;
                    rcChat_popup.setVisibility(View.VISIBLE);
                    voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    voice_rcd_hint_tooshort.setVisibility(View.GONE);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (!isShosrt) {
                                voice_rcd_hint_loading.setVisibility(View.GONE);
                                voice_rcd_hint_rcding
                                        .setVisibility(View.VISIBLE);
                            }
                        }
                    }, 300);

                    del_re.setVisibility(View.GONE);
                    long startVoiceT = SystemClock.currentThreadTimeMillis();
                    voiceName = startVoiceT + ".amr";
                    start(voiceName);
                    flag = 2;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {// 松开手势时执行录制完成
                voice_btn.setBackgroundResource(R.drawable.text_fillet_bg);
                System.out.println("4");
                if (!isfinish) {// 假如倒计时完成已发送一遍就不再进行发送
                    if (event.getRawY() >= del_Y
                            && event.getRawY() <= del_Y + del_re.getHeight()
                            && event.getRawX() >= del_x
                            && event.getRawX() <= del_x + del_re.getWidth()) {

                        stop();
                        flag = 1;
                        File file = new File(
                                android.os.Environment
                                        .getExternalStorageDirectory()
                                        + "/"
                                        + voiceName);
                        if (file.exists()) {
                            file.delete();
                        }
                    } else {

                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        stop();
                        // endVoiceT = SystemClock.currentThreadTimeMillis();
                        flag = 1;
                        // int time = (int) ((endVoiceT - startVoiceT) / 1000);
                        if (timeData < 1) {
                            isShosrt = true;
                            voice_rcd_hint_loading.setVisibility(View.GONE);
                            voice_rcd_hint_rcding.setVisibility(View.GONE);
                            voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    voice_rcd_hint_tooshort
                                            .setVisibility(View.GONE);
                                    rcChat_popup.setVisibility(View.GONE);
                                    isShosrt = false;
                                }
                            }, 500);
                            return false;
                        } else {
                            voiceData = Environment
                                    .getExternalStorageDirectory()
                                    + "/"
                                    + voiceName;

                        }

                    }
                }
            }
            if (event.getRawY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内

                del_re.setVisibility(View.VISIBLE);
                voice_isNo.setVisibility(View.GONE);
                voice_time.setVisibility(View.GONE);
                if (event.getRawY() >= del_Y
                        && event.getRawY() <= del_Y + del_re.getHeight()
                        && event.getRawX() >= del_x
                        && event.getRawX() <= del_x + del_re.getWidth()) {

                }
            } else {

                del_re.setVisibility(View.GONE);
                voice_isNo.setVisibility(View.VISIBLE);

            }

            return true;
        }
    };
    private static final int POLL_INTERVAL = 300;
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

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

    /**
     * 查询订单
     */
    private void published() {
        String access_token = UserData.getSettingString(mContext,
                UserData.access_token);
    /*		if (access_token != null
				&& !"".equals(UserData.getSettingString(mContext,
				UserData.access_token))) {*/
        startProgressDialog();
        userAction.published(mContext, access_token, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {

                        if (obj != null && !"".equals(obj)) {
                            publishedData publised = (publishedData) obj;
                            String state = publised.getState();
                            if (state != null && !"".equals(state)) {
                                if (state.equals("PICKER")) {
                                    Intent intent = new Intent(EmergencyRepairActivity.this,
                                            DisSuccessActivity.class);
                                    intent.putExtra("publised", publised);
                                    startActivity(intent);
                                } else if (state.equals("COMMIT")) {
                                    Intent pay = new Intent(EmergencyRepairActivity.this,
                                            PayActivity.class);
                                    pay.putExtra("publised", publised);
                                    startActivity(pay);
                                }
                            }
                            EmergencyRepairActivity.this.finish();
                            UserData.setSettingBoolean(mContext,
                                    UserData.isPublish, true);
                            BaseToast.makeShortToast(mContext, "您有未完成订单");
                        } else {
                            publish();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        stopProgressDialog();
                        Log.d("published", "onFailure");
                    }
                }

        );

    }
//	}

}
