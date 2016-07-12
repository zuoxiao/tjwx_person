package com.example.tjwx_person.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.UserData;

/**
 * Created by zuo on 2016/6/23.
 */
public class FeelBackActivity extends BaseActivity {
    EditText editText;
    UserAction userAction;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_feelback, 7, "意见反馈");
        editText = (EditText) findViewById(R.id.feelback_edt);
        userAction = UserAction.getInstance();
        mContext = this;
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    @Override
    public void submitBtn() {
        super.submitBtn();
        if (!editText.getText().toString().equals("")) {
            startProgressDialog();
            userAction.feelBack(this, UserData.getSettingString(mContext, UserData.access_token), editText.getText().toString(), new AsyncHandler() {
                @Override
                public void onSuccess(Object obj) {
                    BaseToast.makeLongToast(mContext, "反馈成功，感谢您的反馈");
                    stopProgressDialog();
                    FeelBackActivity.this.finish();
                }

                @Override
                public void onFailure(int statusCode, String message) {
                    stopProgressDialog();
                    if (!message.equals("")){
                        BaseToast.makeLongToast(mContext, message);
                    }else {
                        BaseToast.makeLongToast(mContext, "反馈成功，感谢您的反馈");
                    }

                }
            });
        } else {
            BaseToast.makeShortToast(mContext, "请输入您的意见或建议");
        }


    }
}
