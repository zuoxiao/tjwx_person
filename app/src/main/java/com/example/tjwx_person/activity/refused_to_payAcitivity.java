package com.example.tjwx_person.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.UserData;

/**
 * 拒绝支付
 *
 * @author zuo
 */
public class refused_to_payAcitivity extends BaseActivity {
    String cancelReason = "";//取消类型
    String cancelData = "";//取消原因
    Context mContext;
    UserAction userAction;
    String orderId;
    publishedData publised;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.acitivity_refused_to_pay, 1, "拒绝支付");
        publised = (publishedData) getIntent().getSerializableExtra("publised");
        mContext = this;
        initView();
    }

    private CheckBox closed_behaveBadly, closed_goodBad, closed_cookingBad,
            closed_dislike, closed_fool, closed_highPrice;
    EditText unclosed_edt;

    TextView unclosed_ok;

    private void initView() {
        userAction = UserAction.getInstance();
        closed_behaveBadly = (CheckBox) findViewById(R.id.closed_behaveBadly);
        closed_behaveBadly.setOnClickListener(this);
        closed_goodBad = (CheckBox) findViewById(R.id.closed_goodBad);
        closed_goodBad.setOnClickListener(this);
        closed_cookingBad = (CheckBox) findViewById(R.id.closed_cookingBad);
        closed_cookingBad.setOnClickListener(this);
        closed_dislike = (CheckBox) findViewById(R.id.closed_dislike);
        closed_dislike.setOnClickListener(this);
        closed_fool = (CheckBox) findViewById(R.id.closed_fool);
        closed_fool.setOnClickListener(this);
        closed_highPrice = (CheckBox) findViewById(R.id.closed_highPrice);
        closed_highPrice.setOnClickListener(this);
        unclosed_ok = (TextView) findViewById(R.id.unclosed_ok);
        unclosed_ok.setOnClickListener(this);
        unclosed_edt = (EditText) findViewById(R.id.unclosed_edt);
    }

/*    private void setCheckedFalse() {

        closed_behaveBadly.setChecked(false);
        closed_goodBad.setChecked(false);
        closed_cookingBad.setChecked(false);
        closed_dislike.setChecked(false);
        closed_fool.setChecked(false);
        closed_highPrice.setChecked(false);
    }*/

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

          /*  case R.id.closed_behaveBadly:
                setCheckedFalse();
                closed_behaveBadly.setChecked(true);
                cancelReason = "态度恶劣";
                break;
            case R.id.closed_goodBad:
                setCheckedFalse();
                closed_goodBad.setChecked(true);
                cancelReason = "把好的整坏了";
                break;
            case R.id.closed_cookingBad:
                setCheckedFalse();
                closed_cookingBad.setChecked(true);
                cancelReason = "技术太差了";
                break;
            case R.id.closed_dislike:
                setCheckedFalse();
                closed_dislike.setChecked(true);
                cancelReason = "看不顺眼";
                break;
            case R.id.closed_fool:
                setCheckedFalse();
                closed_fool.setChecked(true);
                cancelReason = "纯属糊弄";
                break;
            case R.id.closed_highPrice:
                setCheckedFalse();
                closed_highPrice.setChecked(true);
                cancelReason = "价格要高了";
                break;*/
            case R.id.unclosed_ok:
                if (Utils.isNetworkAvailable(mContext)) {
                    if (closed_behaveBadly.isChecked()) {
                        cancelReason += "态度恶劣";
                    }
                    if (closed_goodBad.isChecked()) {

                        if (cancelReason.equals("")) {
                            cancelReason += "把好的整坏了";
                        } else {
                            cancelReason += ",把好的整坏了";
                        }
                    }
                    if (closed_cookingBad.isChecked()) {

                        if (cancelReason.equals("")) {
                            cancelReason += "技术太差了";
                        } else {
                            cancelReason += ",技术太差了";
                        }
                    }
                    if (closed_dislike.isChecked()) {

                        if (cancelReason.equals("")) {
                            cancelReason += "看不顺眼";
                        } else {
                            cancelReason += ",看不顺眼";
                        }
                    }
                    if (closed_fool.isChecked()) {

                        if (cancelReason.equals("")) {
                            cancelReason += "纯属糊弄";
                        } else {
                            cancelReason += ",纯属糊弄";
                        }
                    }
                    if (closed_highPrice.isChecked()) {
                        if (cancelReason.equals("")) {
                            cancelReason += "价格要高了";
                        } else {
                            cancelReason += ",价格要高了";
                        }
                    }

                    cancelData = unclosed_edt.getText().toString();
                    if (!"".equals(cancelReason) || !"".equals(cancelData)) {
                        if ("".equals(cancelData)) {
                            cancelData = cancelReason;
                        }
                        publishCancel();
                    } else {
                        BaseToast.makeShortToast(mContext, "请选择或者输入取消原因");
                    }

                } else {
                    BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                }
                break;
            default:
                break;
        }
    }

    private void publishCancel() {
        orderId = publised.getId();


        userAction.refused_to_pay(mContext,
                UserData.getSettingString(mContext, UserData.access_token),
                orderId, cancelReason, cancelData, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        refused_to_payAcitivity.this.finish();
                        BaseToast.makeLongToast(mContext, "取消成功");
                        if (DisSuccessActivity.isFinsh != null) {
                            DisSuccessActivity.isFinsh.finish();
                        }
                        if (PayActivity.isFinsh != null) {
                            PayActivity.isFinsh.finish();
                        }

                        UserData.setSettingBoolean(mContext,
                                UserData.isPublish, false);
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        if (!"".equals(message)) {
                            BaseToast.makeLongToast(mContext, message);
                        } else {
                            BaseToast.makeLongToast(mContext, "取消失败，请稍后再试");
                        }


                    }
                });

    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }
}
