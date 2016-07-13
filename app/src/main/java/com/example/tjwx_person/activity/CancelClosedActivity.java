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
 * 取消被接后的订单
 *
 * @author zuo
 */
public class CancelClosedActivity extends BaseActivity {
    String cancelReason = "";//取消类型
    String cancelData = "";//取消原因
    UserAction userAction;
    Context mContext;
    String orderId;
    publishedData publised;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_cancel_closed, 3, "取消订单");
        mContext = this;
        initView();
    }

    @Override
    public void backButton() {
        this.finish();
    }

    ;

    CheckBox closed_planChange, closed_noNeed, closed_myown,
            closed_behaveBadly, closed_goodBad, closed_cookingBad,
            closed_dislike, closed_fool, closed_highPrice;
    EditText unclosed_edt;

    TextView unclosed_ok;

    private void initView() {
        userAction = UserAction.getInstance();
        publised = (publishedData) getIntent().getSerializableExtra("publised");
        closed_planChange = (CheckBox) findViewById(R.id.closed_planChange);
        closed_planChange.setOnClickListener(this);
        closed_noNeed = (CheckBox) findViewById(R.id.closed_noNeed);
        closed_noNeed.setOnClickListener(this);
        closed_myown = (CheckBox) findViewById(R.id.closed_myown);
        closed_myown.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.closed_planChange:
                //setCheckedFalse();
                if (closed_planChange.isChecked()) {
                    closed_planChange.setChecked(true);
                } else {
                    closed_planChange.setChecked(false);
                }

                //   cancelReason += ",计划改变了";
                setWorkerCheckedFalse();
                break;
            case R.id.closed_noNeed:
                //setCheckedFalse();
                if (closed_noNeed.isChecked()) {
                    closed_noNeed.setChecked(true);
                } else {
                    closed_noNeed.setChecked(false);
                }
                //   cancelReason += ",不需要修理了";
                setWorkerCheckedFalse();
                break;
            case R.id.closed_myown:
                //setCheckedFalse();
                if (closed_myown.isChecked()) {
                    closed_myown.setChecked(true);
                } else {
                    closed_myown.setChecked(false);
                }
                //  cancelReason += ",自己搞定了";
                setWorkerCheckedFalse();
                break;
            case R.id.closed_behaveBadly:
                //setCheckedFalse();
                if (closed_behaveBadly.isChecked()) {
                    closed_behaveBadly.setChecked(true);
                } else {
                    closed_behaveBadly.setChecked(false);
                }
                //   cancelReason += ",态度恶劣";
                setCheckedMeFalse();
                break;
            case R.id.closed_goodBad:
                //setCheckedFalse();
                if (closed_goodBad.isChecked()) {
                    closed_goodBad.setChecked(true);
                } else {
                    closed_goodBad.setChecked(false);
                }
                //    cancelReason += ",把好的整坏了";
                setCheckedMeFalse();
                break;
            case R.id.closed_cookingBad:
                //setCheckedFalse();
                if (closed_cookingBad.isChecked()) {
                    closed_cookingBad.setChecked(true);
                } else {
                    closed_cookingBad.setChecked(false);
                }
                //  cancelReason += ",技术太差了";
                setCheckedMeFalse();
                break;
            case R.id.closed_dislike:
                //setCheckedFalse();
                if (closed_dislike.isChecked()) {
                    closed_dislike.setChecked(true);
                } else {
                    closed_dislike.setChecked(false);
                }

                //   cancelReason += ",看不顺眼";
                setCheckedMeFalse();
                break;
            case R.id.closed_fool:
                //setCheckedFalse();
                if (closed_fool.isChecked()) {
                    closed_fool.setChecked(true);
                } else {
                    closed_fool.setChecked(false);
                }
                //cancelReason += ",纯属糊弄";
                setCheckedMeFalse();
                break;
            case R.id.closed_highPrice:
                //setCheckedFalse();
                if (closed_highPrice.isChecked()) {
                    closed_highPrice.setChecked(true);
                } else {
                    closed_highPrice.setChecked(false);
                }
                //  cancelReason += ",价格要高了";
                setCheckedMeFalse();
                break;
            case R.id.unclosed_ok:
                if (Utils.isNetworkAvailable(mContext)) {
                    if (closed_planChange.isChecked()) {
                        cancelReason += "计划改变了";
                    }

                    if(closed_noNeed.isChecked()){
                        if (cancelReason.equals("")){
                            cancelReason += "不需要修理了";
                        }else {
                            cancelReason += ",不需要修理了";
                        }

                    }
                    if (closed_myown.isChecked()) {

                        if (cancelReason.equals("")){
                            cancelReason += "自己搞定了";
                        }else {
                            cancelReason += ",自己搞定了";
                        }

                    }
                    if (closed_behaveBadly.isChecked()) {

                        if (cancelReason.equals("")){
                            cancelReason += "态度恶劣";
                        }else {
                            cancelReason += ",态度恶劣";
                        }

                    }
                    if (closed_goodBad.isChecked()) {

                        if (cancelReason.equals("")){
                            cancelReason += "把好的整坏了";
                        }else {
                            cancelReason += ",把好的整坏了";
                        }

                    }
                    if (closed_cookingBad.isChecked()) {

                        if (cancelReason.equals("")){
                            cancelReason += "技术太差了";
                        }else {
                            cancelReason += ",技术太差了";
                        }

                    }
                    if (closed_fool.isChecked()) {

                        if (cancelReason.equals("")){
                            cancelReason += "纯属糊弄";
                        }else {
                            cancelReason += ",纯属糊弄";
                        }

                    }
                    if (closed_highPrice.isChecked()) {

                        if (cancelReason.equals("")){
                            cancelReason += "价格要高了";
                        }else {
                            cancelReason += ",价格要高了";
                        }

                    }
                    if (closed_dislike.isChecked()) {

                        if (cancelReason.equals("")){
                            cancelReason += "看不顺眼";
                        }else {
                            cancelReason += ",看不顺眼";
                        }

                    }

                    cancelData = unclosed_edt.getText().toString();
                    if (!"".equals(cancelReason) || !"".equals(cancelData)) {
                        if("".equals(cancelData)){
                            cancelData=cancelReason;
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

    private void setCheckedMeFalse() {
        closed_planChange.setChecked(false);
        closed_noNeed.setChecked(false);
        closed_myown.setChecked(false);

    }

    private void setWorkerCheckedFalse() {
        closed_behaveBadly.setChecked(false);
        closed_goodBad.setChecked(false);
        closed_cookingBad.setChecked(false);
        closed_dislike.setChecked(false);
        closed_fool.setChecked(false);
        closed_highPrice.setChecked(false);

    }

    private void publishCancel() {
        orderId = publised.getId();

        userAction.CancelUnclose(mContext,
                UserData.getSettingString(mContext, UserData.access_token),
                orderId, cancelReason, cancelData, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        CancelClosedActivity.this.finish();
                        BaseToast.makeLongToast(mContext, "取消成功");
                        if (DisSuccessActivity.isFinsh != null) {
                            DisSuccessActivity.isFinsh.finish();
                        }
                        if (PayActivity.isFinsh != null) {
                            PayActivity.isFinsh.finish();
                        }
                        UserData.setSettingBoolean(mContext,UserData.isRefreshMain,true);
                        UserData.setSettingBoolean(mContext,
                                UserData.isPublish, false);
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        if (message!=null&&!"".equals(message)){
                            BaseToast.makeLongToast(mContext,message);
                        }else {
                            BaseToast.makeLongToast(mContext, "取消失败，请稍后再试");
                        }

                    }
                });

    }
}
