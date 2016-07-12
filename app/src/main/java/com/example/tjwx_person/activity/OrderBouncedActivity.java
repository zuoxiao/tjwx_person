package com.example.tjwx_person.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.utils.DateUtil;

/**
 * Created by zuo on 2016/6/26.
 */
public class OrderBouncedActivity extends BaseActivity {
    publishedData publishedData = new publishedData();
    private TextView bounced_dingdanhao;
    private TextView bounced_time;
    private TextView bounced_detail;
    private TextView bounced_bouncedDetail;
    private TextView disSuccess_message;
    private TextView disSuccess_phone;
    private TextView callPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publishedData = (publishedData) getIntent().getSerializableExtra("publishedData");
        if (publishedData.getState().equals("CANCEL")) {
            addContentView(R.layout.activity_order_bounced, 1, "已取消订单");
        } else {
            addContentView(R.layout.activity_order_bounced, 1, "拒支付订单");
        }
        initView();
    }

    private void initView() {

        bounced_dingdanhao = (TextView) findViewById(R.id.bounced_dingdanhao);
        bounced_time = (TextView) findViewById(R.id.bounced_time);
        bounced_detail = (TextView) findViewById(R.id.bounced_detail);
        bounced_bouncedDetail = (TextView) findViewById(R.id.bounced_bouncedDetail);
        bounced_dingdanhao.setText("订单号：" + publishedData.getId());
        bounced_time.setText("发布时间：" + DateUtil.date2StringLong(publishedData.getPublishTime()));
        if (publishedData.getState().equals("CANCEL")) {
            bounced_bouncedDetail.setText("取消原因：" + publishedData.getCancelReason());
        } else {
            bounced_bouncedDetail.setText("拒付原因：" + publishedData.getRefusalReason());
        }
        if (publishedData.isAudioOrder()){
            bounced_detail.setText("维修内容："+"语音");
        }else {


            if (publishedData.getSkills()!=null&&publishedData.getSkills().size()>0){
                bounced_detail.setText("维修内容："+publishedData.getSkills().get(0).getDescription());
            }else {
                bounced_detail.setText("维修内容：");
            }

        }

        disSuccess_message = (TextView) findViewById(R.id.disSuccess_message);
        disSuccess_message.setOnClickListener(this);
        disSuccess_phone = (TextView) findViewById(R.id.disSuccess_phone);
        disSuccess_phone.setOnClickListener(this);
        callPhone = (TextView) findViewById(R.id.callPhone);
        callPhone.setOnClickListener(this);
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
            default:
                break;
        }
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }
}
