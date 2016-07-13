package com.example.tjwx_person.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
 * 取消的订单
 *
 * @author zuo
 */
public class CancelUnclosedActivity extends BaseActivity {

    String cancelReason = "";//取消类型
    String cancelData="";//取消原因
    UserAction userAction;
    Context mContext;
    String orderId;
    public publishedData publised;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        addContentView(R.layout.activity_cancel_unclosed, 3, "取消订单");
        mContext = this;
        initView();
    }

    @Override
    public void backButton() {
        this.finish();
    }

    CheckBox unclosed_longtime, unclosed_goOut, unclosed_myown,
            unclosed_planChange;
    EditText unclosed_edt;

    TextView unclosed_ok;

    private void initView() {
        publised = (publishedData) getIntent().getSerializableExtra("publised");
        userAction = UserAction.getInstance();
        unclosed_longtime = (CheckBox) findViewById(R.id.unclosed_longtime);
        unclosed_longtime.setOnClickListener(this);

        unclosed_goOut = (CheckBox) findViewById(R.id.unclosed_goOut);
        unclosed_goOut.setOnClickListener(this);

        unclosed_myown = (CheckBox) findViewById(R.id.unclosed_myown);
        unclosed_myown.setOnClickListener(this);

        unclosed_planChange = (CheckBox) findViewById(R.id.unclosed_planChange);


        unclosed_edt = (EditText) findViewById(R.id.unclosed_edt);
        unclosed_ok = (TextView) findViewById(R.id.unclosed_ok);
        unclosed_ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.unclosed_longtime:
            //    cancelReason = "等待时间太长";
/*
            unclosed_myown.setChecked(false);
			unclosed_planChange.setChecked(false);
			unclosed_goOut.setChecked(false);*/

                break;
            case R.id.unclosed_goOut:
            //    cancelReason = "要外出";
/*			unclosed_goOut.setChecked(true);
            unclosed_myown.setChecked(false);
			unclosed_planChange.setChecked(false);
			unclosed_longtime.setChecked(false);*/
                break;
            case R.id.unclosed_myown:
           //     cancelReason = "自己修好了";
/*			unclosed_myown.setChecked(true);
            unclosed_longtime.setChecked(false);
			unclosed_goOut.setChecked(false);
			unclosed_planChange.setChecked(false);*/
                break;
            case R.id.unclosed_planChange:
            //    cancelReason = "计划改变了";
/*			unclosed_myown.setChecked(false);
            unclosed_goOut.setChecked(false);
			unclosed_longtime.setChecked(false);
			unclosed_planChange.setChecked(true);*/
                break;
            case R.id.unclosed_ok:

                if (Utils.isNetworkAvailable(mContext)) {
                    if (unclosed_longtime.isChecked()) {
                        cancelReason += "等待时间太长";
                    }
                    if (unclosed_goOut.isChecked()) {
                        if (cancelReason.equals("")){
                            cancelReason += "要外出";
                        }else {
                            cancelReason += ",要外出";
                        }

                    }
                    if (unclosed_myown.isChecked()) {

                        if (cancelReason.equals("")){
                            cancelReason += "自己修好了";
                        }else {
                            cancelReason += ",自己修好了";
                        }

                    }
                    if (unclosed_planChange.isChecked()) {

                        if (cancelReason.equals("")){
                            cancelReason += "计划改变了";
                        }else {
                            cancelReason += ",计划改变了";
                        }

                    }

                    cancelData=unclosed_edt.getText().toString();
                    if (!"".equals(cancelReason)||!"".equals(cancelData)) {
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

    private void publishCancel() {

        orderId = publised.getId();
        userAction.CancelUnclose(mContext,
                UserData.getSettingString(mContext, UserData.access_token),
                orderId, cancelReason, cancelData, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {

                        CancelUnclosedActivity.this.finish();
                        BaseToast.makeLongToast(mContext, "取消成功");
                        UserData.setSettingBoolean(mContext, UserData.isPublish, false);

                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        if (message != null && !"".equals(message)) {
                            BaseToast.makeLongToast(mContext, message);
                        } else {
                            BaseToast.makeLongToast(mContext, "取消失败，请稍后再试");
                        }


                    }
                });

    }

}
