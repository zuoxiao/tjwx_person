package com.example.tjwx_person.activity;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.CouponData;
import com.example.tjwx_person.bean.CouponDetailData;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.receiver.NotificationService;
import com.example.tjwx_person.receiver.XGNotification;
import com.example.tjwx_person.utils.UserData;
import com.tencent.android.tpush.XGPushManager;

public class PayActivity extends BaseActivity {

    publishedData publised = null;
    public Context mContext;
    UserAction userAction;
    public static PayActivity isFinsh;
    CouponDetailData detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        isFinsh = this;
        addContentView(R.layout.activity_pay, 2, "订单支付");
        initView();
        publised = (publishedData) getIntent().getSerializableExtra("publised");
        if (publised == null) {
            published();
        } else {
            pay_number.setText(publised.getAmount());
            pay_paidMoney.setText(publised.getAmount() + "元");
            pay_text2
                    .setText("如您有疑问，请与" + publised.getProcessorName() + "师傅联系");
            pay_text1.setText(publised.getProcessorName() + "师傅为您提供服务已结束！");
        }
        getVoucher();
        removeMessage(this);
    }

    // 按钮
    TextView pay_phone, pay_message, pay_ok, pay_no, callPhone;
    // 显示
    TextView pay_discount_text, pay_number, pay_paidMoney, pay_text1,
            pay_text2;
    RelativeLayout pay_discount_rl;

    private void initView() {
        pay_phone = (TextView) findViewById(R.id.pay_phone);
        pay_message = (TextView) findViewById(R.id.pay_message);
        pay_ok = (TextView) findViewById(R.id.pay_ok);
        pay_no = (TextView) findViewById(R.id.pay_no);
        callPhone = (TextView) findViewById(R.id.callPhone);
        pay_discount_text = (TextView) findViewById(R.id.pay_discount_text);
        pay_number = (TextView) findViewById(R.id.pay_number);
        pay_paidMoney = (TextView) findViewById(R.id.pay_paidMoney);
        pay_text1 = (TextView) findViewById(R.id.pay_text1);
        pay_text2 = (TextView) findViewById(R.id.pay_text2);
        pay_discount_rl = (RelativeLayout) findViewById(R.id.pay_discount_rl);
        pay_phone.setOnClickListener(this);
        pay_message.setOnClickListener(this);
        pay_ok.setOnClickListener(this);
        pay_no.setOnClickListener(this);
        callPhone.setOnClickListener(this);
        userAction = UserAction.getInstance();
    }

    /**
     * 查询订单
     */
    private void published() {

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
                        if (publised.getPositions() != null) {
                            pay_number.setText(publised.getAmount());
                            pay_text2.setText("如您有疑问，请与"
                                    + publised.getProcessorName() + "师傅联系");
                            pay_text1.setText(publised.getProcessorName()
                                    + "师傅为您提供服务已结束！");
                        }
                        if (detail != null) {
                            Double publisedAmount = Double.valueOf(publised
                                    .getAmount());
                            Double detailAmount = Double.valueOf(detail
                                    .getAmount());
                            Double amounts = publisedAmount - detailAmount;
                            BigDecimal b = new BigDecimal(amounts);
                            BigDecimal amount = b.setScale(2,
                                    BigDecimal.ROUND_DOWN);
                            BigDecimal minNumber = BigDecimal.valueOf(0.01);
                            if (amount.compareTo(minNumber) == -1) {
                                pay_paidMoney.setText(0.01 + "元");
                            } else {
                                pay_paidMoney.setText(amount.toString()
                                        + "元");
                            }
                        } else {
                            getVoucher();
                        }
                        UserData.setSettingBoolean(mContext,
                                UserData.isPublish, true);
                    } else {
                        PayActivity.this.finish();
                        UserData.setSettingBoolean(mContext,
                                UserData.isPublish, false);
                    }
                }

                @Override
                public void onFailure(int statusCode, String message) {
                    PayActivity.this.finish();
                    Log.d("published", "onFailure");
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pay_ok:
                Intent payChoice = new Intent(mContext, PayChoiceActivity.class);
                payChoice.putExtra("publised", publised);
                payChoice.putExtra("detail", detail);
                startActivity(payChoice);
                break;
            case R.id.pay_no:
                Intent closed = new Intent(mContext, refused_to_payAcitivity.class);
                closed.putExtra("publised", publised);
                startActivity(closed);
                break;
            case R.id.callPhone:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.DIAL");
                intent1.setData(Uri.parse("tel:" + callPhone.getText().toString()));
                startActivity(intent1);
                break;
            case R.id.pay_message:
                Intent message = new Intent(this, MessageActivity.class);
                message.putExtra("publised", publised);
                startActivity(message);
                break;
            case R.id.pay_phone:
                Intent intent2 = new Intent();
                intent2.setAction("android.intent.action.DIAL");
                intent2.setData(Uri.parse("tel:"
                        + publised.getProcessorMobilePhone()));
                startActivity(intent2);
                break;
            default:
                break;
        }

    }

    /**
     * 获取可用优惠券
     */
    private void getVoucher() {
        userAction.usableVoucher(mContext,
                UserData.getSettingString(mContext, UserData.access_token), "0", new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        if (obj != null && !"".equals(obj)) {
                            List<CouponDetailData> coupon = (List<CouponDetailData>) obj;
                            if (coupon != null && coupon.size() != 0) {
                                detail = coupon.get(0);
                                pay_discount_text.setText("使用优惠券为您节省了"
                                        + detail.getAmount() + "元");
                                Double publisedAmount = Double.valueOf(publised
                                        .getAmount());
                                Double detailAmount = Double.valueOf(detail
                                        .getAmount());
                                Double amounts = publisedAmount - detailAmount;
                                BigDecimal b = new BigDecimal(amounts);
                                BigDecimal amount = b.setScale(2,
                                        BigDecimal.ROUND_DOWN);
                                BigDecimal minNumber = BigDecimal.valueOf(0.01);
                                if (amount.compareTo(minNumber) == -1) {
                                    pay_paidMoney.setText(0.01 + "元");
                                } else {
                                    pay_paidMoney.setText(amount.toString()
                                            + "元");
                                }

                            } else {
                                pay_discount_rl.setVisibility(View.GONE);
                            }

                        } else {
                            //  PayActivity.this.finish();
                            pay_discount_rl.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        Log.d("", "");

                    }
                });
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

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
        removeMessage(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        published();

    }

    /**
     * 删除通知
     */
    private void removeMessage(Context context) {

        int count = NotificationService.getInstance(context).getCount();
        List<XGNotification> listXGNotification = NotificationService.getInstance(context).getScrollData(1, count, null);
        if (listXGNotification != null && listXGNotification.size() != 0) {
            for (int i = 0; i < listXGNotification.size(); i++) {
                if (listXGNotification.get(i).getActivity().equals("com.example.tjwx_person.activity.PayActivity")) {
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

}
