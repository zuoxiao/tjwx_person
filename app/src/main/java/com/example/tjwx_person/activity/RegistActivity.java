package com.example.tjwx_person.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.RegistData;
import com.example.tjwx_person.bean.UpdateDown;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.URLConstant;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.tool.DownLoadDialog;
import com.example.tjwx_person.tool.IsWifi;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.UserData;
import com.tencent.android.tpush.XGPushConfig;

public class RegistActivity extends BaseActivity {


    int Regist_type = 0;//从哪个界面跳转过来的1是维修 2是紧急维修

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        XGPushConfig.enableDebug(this, true);
        addContentView(R.layout.activity_regist, 3, "注册");
        initView();
        update();
    }

    private EditText regist_Verification_editext, regist_phone;
    private TextView regist_Verification_text, regist_agreement_text,
            regist_btn, callPhone;
    private TimeCount time;
    private CheckBox regist_agreement_btn;
    UserAction userAction;
    Context mContext;


    private void initView() {
        if (getIntent().getIntExtra("Regist_type", 0) != 0) ;
        {
            Regist_type = getIntent().getIntExtra("Regist_type", 0);
        }
        mContext = this;
        userAction = UserAction.getInstance();
        regist_Verification_editext = (EditText) findViewById(R.id.regist_Verification_editext);

        regist_phone = (EditText) findViewById(R.id.regist_phone);
        regist_Verification_text = (TextView) findViewById(R.id.regist_Verification_text);
        regist_agreement_text = (TextView) findViewById(R.id.regist_agreement_text);
        regist_btn = (TextView) findViewById(R.id.regist_btn);
        callPhone = (TextView) findViewById(R.id.callPhone);
        regist_agreement_btn = (CheckBox) findViewById(R.id.regist_agreement_btn);
        regist_Verification_text.setOnClickListener(this);
        regist_agreement_text.setOnClickListener(this);
        regist_btn.setOnClickListener(this);
        callPhone.setOnClickListener(this);
        time = new TimeCount(60 * 1000, 10);// 设置计时器
        // XGPushConfig.enableDebug(this, true);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.regist_Verification_text:
                // 获取验证码
                if (!regist_phone.getText().toString().equals("")) {
                    String regExp1 = "^1[3578]\\d{9}$";
                    Pattern p1 = Pattern.compile(regExp1);
                    Matcher m1 = p1.matcher(regist_phone.getText().toString());
                    if (m1.find()) {
                        userAction.verify_code(mContext, regist_phone.getText()
                                .toString(), new AsyncHandler() {

                            @Override
                            public void onSuccess(Object obj) {
                                BaseToast.makeShortToast(mContext, "验证码已发送");

                            }

                            @Override
                            public void onFailure(int statusCode, String message) {
                                if (message != null && !"".equals(message))
                                    BaseToast.makeShortToast(mContext, message);
                                Log.d("", "");
                            }
                        });
                        time.start();
                    } else {
                        BaseToast.makeShortToast(mContext, "请输入正确手机号");
                    }
                } else {
                    BaseToast.makeShortToast(mContext, "请输入手机号");
                }

                break;
            case R.id.regist_agreement_text:
                Intent agreegment = new Intent(this, agreegmentActivity.class);
                startActivity(agreegment);
                break;
            case R.id.regist_btn:

                if (regist_agreement_btn.isChecked()) {
                    String regExp1 = "^1[3578]\\d{9}$";
                    Pattern p1 = Pattern.compile(regExp1);
                    Matcher m1 = p1.matcher(regist_phone.getText().toString());
                    if (m1.find()) {
                        //
                        // UserData.setSettingString(getApplicationContext(),
                        // UserData.user_ID, "111");
                        // UserData.setSettingString(getApplicationContext(),
                        // UserData.IsLogin, "true");
                        // this.finish();
                        if (Utils.isNetworkAvailable(mContext)) {
                            if (!regist_phone.getText().toString().equals("")) {
                                if (!regist_Verification_editext.getText()
                                        .toString().equals("")) {
                                    userAction.regist(mContext, regist_phone
                                                    .getText().toString(),
                                            regist_Verification_editext.getText()
                                                    .toString(), true,
                                            new AsyncHandler() {

                                                @Override
                                                public void onSuccess(Object obj) {

                                                    RegistData data = (RegistData) obj;
                                                    UserData.setSettingString(
                                                            mContext,
                                                            UserData.clientSecret,
                                                            data.getClientSecret());
                                                    UserData.setSettingString(
                                                            mContext,
                                                            UserData.user_ID,
                                                            data.getClientId());
                                                    UserData.setSettingString(
                                                            mContext,
                                                            UserData.user_phone,
                                                            data.getClientId());
                                                    UserData.setSettingBoolean(
                                                            mContext,
                                                            UserData.IsLogin, true);
                                                    BaseToast.makeLongToast(
                                                            mContext, "登陆或注册成功");
                                                    MainActivity.isRush = true;
                                                    MainActivity.isRefreshXG = true;

                                                    if (Regist_type != 0 && Regist_type == 1) {
                                                        Intent publied = new Intent(RegistActivity.this, RepairActivity.class);
                                                        setResult(4, publied);
                                                    } else if (Regist_type != 0 && Regist_type == 2) {
                                                        Intent publied = new Intent(RegistActivity.this, EmergencyRepairActivity.class);
                                                        setResult(4, publied);
                                                    }
                                                    RegistActivity.this.finish();
                                                }

                                                @Override
                                                public void onFailure(
                                                        int statusCode,
                                                        String message) {
                                                    if (message != null && !"".equals(message)) {
                                                        BaseToast.makeLongToast(
                                                                mContext,
                                                                message);
                                                    } else {
                                                        BaseToast.makeLongToast(
                                                                mContext,
                                                                "请输入正确的验证码或者手机号");
                                                    }

                                                    Log.d("", "");
                                                }
                                            });
                                } else {
                                    BaseToast.makeShortToast(mContext, "请输入验证码");
                                }
                            } else {
                                BaseToast.makeShortToast(mContext, "请输入手机号");
                            }
                        } else {
                            BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                        }
                    } else {
                        BaseToast.makeShortToast(mContext, "请输入正确手机号");
                    }
                } else {
                    BaseToast.makeShortToast(mContext, "请您阅读并同意服务协议");
                }

                break;
            case R.id.callPhone:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.DIAL");
                intent1.setData(Uri.parse("tel:" + callPhone.getText().toString()));
                startActivity(intent1);
                break;

            default:
                break;
        }

    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            regist_Verification_text.setText("重新验证");
            regist_Verification_text.setClickable(true);
            regist_Verification_text.setTextColor(getResources().getColor(
                    R.color.title_color));

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            regist_Verification_text.setClickable(false);
            regist_Verification_text.setTextColor(getResources().getColor(
                    R.color.sub_textColor));
            regist_Verification_text.setText("获取验证码" + "("
                    + millisUntilFinished / 1000 + ")");

        }
    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 查询更新
     */
    private void update() {


        userAction.updateDownLoad(mContext, getVersion(), new AsyncHandler() {
            @Override
            public void onSuccess(Object obj) {
                if (obj != null) {
                    try {
                        UpdateDown url = (UpdateDown) obj;
                        if (url.isUpdate()) {
                            dialog("http://" + URLConstant.HOST + ":8084/frontend" + url.getUrl());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String message) {

            }
        });


    }


    private void dialog(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
        builder.setMessage("有新版本是否更新？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {
                if (IsWifi.isWifiActive(RegistActivity.this)) {
                    dialog.dismiss();
                    DownLoadDialog dialog2 = new DownLoadDialog();
                    dialog2.setUrl(url);
                    Dialog dialogs = dialog2.alertDialog(RegistActivity.this, RegistActivity.this);
                    dialogs.show();

                } else {
                    isWifidialog(url, false);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    public void isWifidialog(final String url, final boolean isMust) {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
        builder.setMessage("您当前非Wifi连接，是否继续更新？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (IsWifi.isWifiActive(RegistActivity.this)) {
                    dialog.dismiss();
                    DownLoadDialog dialog2 = new DownLoadDialog();
                    dialog2.setUrl(url);
                    Dialog dialogs = dialog2.alertDialog(RegistActivity.this, RegistActivity.this);
                    dialogs.show();


                }


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isMust) {
                    dialog.dismiss();

                    System.exit(0);
                } else {
                    dialog.dismiss();
                }


            }
        });
        builder.create().show();

    }


}
