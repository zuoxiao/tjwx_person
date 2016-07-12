package com.example.tjwx_person.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.URLConstant;
import com.example.tjwx_person.receiver.NotificationService;
import com.example.tjwx_person.receiver.XGNotification;
import com.example.tjwx_person.utils.UserData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tencent.android.tpush.XGPushManager;


import java.util.List;

/**
 * 派工成功
 *
 * @author zuo
 */
public class DisSuccessActivity extends BaseActivity {

    String phoneNumber;
    publishedData publised = null;
    public static DisSuccessActivity isFinsh;
    public Context mContext;
    UserAction userAction;
    String ACTION = "com.example.tjwx_person.activity.DisSuccessActivity";
    boolean isfirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_dispatch_success, 2, "派工成功");
        isFinsh = this;
        mContext = this;
        initView();
        removeMessage(this);
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(MyBroadcastReceiver, filter);

    }

    TextView disSuccess_name, disSuccess_id;
    ImageView dis_success_img;
    TextView disSuccess_cancel, disSuccess_message, disSuccess_phone,
            callPhone;
    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private void initView() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.worke_img)
                .showImageForEmptyUri(R.drawable.worke_img)
                .showImageOnFail(R.drawable.worke_img)
                // .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(false)
                .displayer(new RoundedBitmapDisplayer(100)).cacheOnDisc(false)
                .considerExifParams(true).build();
        disSuccess_name = (TextView) findViewById(R.id.disSuccess_name);
        disSuccess_id = (TextView) findViewById(R.id.disSuccess_id);
        dis_success_img = (ImageView) findViewById(R.id.dis_success_img);
        disSuccess_cancel = (TextView) findViewById(R.id.disSuccess_cancel);
        disSuccess_message = (TextView) findViewById(R.id.disSuccess_message);
        disSuccess_phone = (TextView) findViewById(R.id.disSuccess_phone);
        callPhone = (TextView) findViewById(R.id.callPhone);
        Intent intent = getIntent();
        publised = (publishedData) intent.getSerializableExtra("publised");
        if (publised == null) {
            published();
        } else {
            disSuccess_name.setText(publised.getProcessorName());
            disSuccess_id.setText(publised.getId());
            phoneNumber = publised.getProcessorMobilePhone();
            String access_token = UserData.getSettingString(mContext,
                    UserData.access_token);
            imageLoader.displayImage(URLConstant.FULLURL + publised.getProcessorHeadPortraitUrl()+"&access_token="+access_token, dis_success_img, options, null);
        }

        disSuccess_cancel.setOnClickListener(this);
        disSuccess_message.setOnClickListener(this);
        disSuccess_phone.setOnClickListener(this);
        callPhone.setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isfirst) {
            published();
        }

        isfirst = false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
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
                intent1.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent1);
                break;
            case R.id.callPhone:
                Intent callphone = new Intent();
                callphone.setAction("android.intent.action.DIAL");
                callphone.setData(Uri
                        .parse("tel:" + callPhone.getText().toString()));
                startActivity(callphone);
                break;
            default:
                break;
        }
    }

    /**
     * 监听Back键按下事件,方法2: 注意: 返回值表示:是否能完全处理该事件 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    /**
     * 查询订单
     */
    private void published() {
        userAction = UserAction.getInstance();
        String access_token = UserData.getSettingString(mContext,
                UserData.access_token);
        if (access_token != null
                && !"".equals(UserData.getSettingString(mContext,
                UserData.access_token))) {

            userAction.published(mContext, access_token, new AsyncHandler() {

                @Override
                public void onSuccess(Object obj) {
                    if (obj != null && !"".equals(obj)) {
                        publised = (publishedData) obj;
                        String state = publised.getState();
                        if (state.equals("PICKER")) {

                            if (publised.getPositions() != null) {
                            }
                            disSuccess_name.setText(publised.getProcessorName());
                            disSuccess_id.setText(publised.getId());
                            phoneNumber = publised.getProcessorMobilePhone();
                            UserData.setSettingBoolean(mContext,
                                    UserData.isPublish, true);
                            String access_token = UserData.getSettingString(mContext,
                                    UserData.access_token);
                            imageLoader.displayImage(URLConstant.FULLURL + publised.getProcessorHeadPortraitUrl()+"&access_token="+access_token, dis_success_img, options, null);
                        } else {

                            if (state.equals("COMMIT")) {
                                Intent pay = new Intent(DisSuccessActivity.this,
                                        PayActivity.class);
                                pay.putExtra("publised", publised);
                                startActivity(pay);
                            }


                            DisSuccessActivity.this.finish();
                        }
                    } else {
                        DisSuccessActivity.this.finish();
                    }
                }

                @Override
                public void onFailure(int statusCode, String message) {
                    DisSuccessActivity.this.finish();
                    Log.d("published", "onFailure");
                }
            });

        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
        removeMessage(this);
    }

    /**
     * 删除通知
     */
    private void removeMessage(Context context) {

        int count = NotificationService.getInstance(context).getCount();
        List<XGNotification> listXGNotification = NotificationService.getInstance(context).getScrollData(1, count, null);
        if (listXGNotification != null && listXGNotification.size() != 0) {
            for (int i = 0; i < listXGNotification.size(); i++) {
                if (listXGNotification.get(i).getActivity().equals("com.example.tjwx_person.activity.DisSuccessActivity")) {
                    if (listXGNotification.get(i).getNotifactionId() != null && !"".equals(listXGNotification.get(i).getNotifactionId())) {
                        XGPushManager.cancelNotifaction(context,
                                Integer.valueOf(listXGNotification.get(i).getNotifactionId()));
                        NotificationService.getInstance(context).delete(listXGNotification.get(i).getId());
                    } else {
                        NotificationService.getInstance(context).delete(listXGNotification.get(i).getId());
                    }
                }

            }


        }
    }

    private BroadcastReceiver MyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            published();
        }
    };

}
