package com.example.tjwx_person.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.utils.DateUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zuo on 2016/6/26.
 */
public class OrderFinishedActivity extends BaseActivity {
    com.example.tjwx_person.bean.publishedData publishedData = new publishedData();
    private TextView finished_dingdanhao;
    private TextView finished_time;
    private TextView finished_detail;
    private TextView finished_wanchengren;
    private TextView finished_money;
    private TextView disSuccess_message;
    private TextView disSuccess_phone;
    private TextView callPhone;
    private TextView finished_commentState;
    public static  String ACTION_NAME="OrderFinishedActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_order_finished, 1, "已完成订单");
        publishedData = (publishedData) getIntent().getSerializableExtra("publishedData");
        initView();
    }

    private void initView() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
        finished_dingdanhao = (TextView) findViewById(R.id.finished_dingdanhao);
        finished_time = (TextView) findViewById(R.id.finished_time);
        finished_detail = (TextView) findViewById(R.id.finished_detail);
        finished_wanchengren = (TextView) findViewById(R.id.finished_wanchengren);
        finished_money = (TextView) findViewById(R.id.finished_money);
        disSuccess_message = (TextView) findViewById(R.id.disSuccess_message);
        disSuccess_phone = (TextView) findViewById(R.id.disSuccess_phone);
        callPhone = (TextView) findViewById(R.id.callPhone);
        finished_commentState = (TextView) findViewById(R.id.finished_commentState);
        finished_commentState.setOnClickListener(this);
        disSuccess_phone.setOnClickListener(this);
        disSuccess_message.setOnClickListener(this);
        callPhone.setOnClickListener(this);
        finished_dingdanhao.setText("订单号：" + publishedData.getId());
        finished_time.setText("发布时间：" + DateUtil.date2StringLong(publishedData.getPublishTime()));
        if (publishedData.isAudioOrder()){
            finished_detail.setText("维修内容："+"语音");
        }else {
            if (publishedData.getSkills()!=null&&publishedData.getSkills().size()>0){
                finished_detail.setText("维修内容："+publishedData.getSkills().get(0).getDescription());
            }else {
                finished_detail.setText("维修内容：");
            }
        }

        finished_wanchengren.setText("维修人员：" + publishedData.getProcessorName());
        finished_money.setText("维修金额：" + publishedData.getAmount() + "元");

        initVisibility();

    }

    private void initVisibility(){

        if (publishedData.isCommentState()) {
            finished_commentState.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.disSuccess_message:
                Intent message = new Intent(this, MessageActivity.class);
                message.putExtra("publised", publishedData);
                startActivity(message);

                break;
            case R.id.disSuccess_phone:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.DIAL");
                intent1.setData(Uri.parse("tel:" + publishedData.getProcessorMobilePhone()));
                startActivity(intent1);
                break;
            case R.id.callPhone:
                Intent callphone = new Intent();
                callphone.setAction("android.intent.action.DIAL");
                callphone.setData(Uri
                        .parse("tel:" + callPhone.getText().toString()));
                startActivity(callphone);

            case R.id.finished_commentState:

                Intent compares = new Intent(this, ComparesActivity.class);
                compares.putExtra("publised", publishedData);
                startActivity(compares);
                break;
            default:
                break;
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_NAME)){
                publishedData.setCommentState(true);
                initVisibility();
            }
        }
    };


    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
