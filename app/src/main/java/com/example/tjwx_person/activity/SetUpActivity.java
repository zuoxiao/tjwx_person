package com.example.tjwx_person.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.PersenData;
import com.example.tjwx_person.bean.UpdateDown;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.URLConstant;
import com.example.tjwx_person.tool.DownLoadDialog;
import com.example.tjwx_person.tool.IsWifi;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.UserData;

/**
 * Created by zuo on 2016/6/27.
 */
public class SetUpActivity extends BaseActivity {


    private ImageView setup_voice_img;
    private RelativeLayout setup_voice_rel;
    private LinearLayout setup_update;
    private TextView textView3;
    TextView setup_update_text;
    UserAction userAction;
    Context mContext;
    boolean isSinglence = true;
    private RelativeLayout function_rel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_set_up, 1, "设置");
        userAction = UserAction.getInstance();
        mContext = this;
        initView();
        getPersonData();


    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    private void initView() {
        setup_voice_img = (ImageView) findViewById(R.id.setup_voice_img);
        setup_voice_rel = (RelativeLayout) findViewById(R.id.setup_voice_rel);
        setup_update = (LinearLayout) findViewById(R.id.setup_update);
        textView3 = (TextView) findViewById(R.id.textView3);
        setup_update_text = (TextView) findViewById(R.id.setup_update_text);
        setup_voice_rel.setOnClickListener(this);
        setup_update.setOnClickListener(this);

        function_rel = (RelativeLayout) findViewById(R.id.function_rel);
        function_rel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.setup_voice_rel:
                //     final boolean isSinglence = UserData.getSettingBoolean(mContext, UserData.Singlence, true);
                startProgressDialog();
                userAction.changeSinlence(mContext, !isSinglence, UserData.getSettingString(mContext, UserData.access_token), new AsyncHandler() {
                    @Override
                    public void onSuccess(Object obj) {
                        stopProgressDialog();
                        if (!isSinglence) {
                            isSinglence = !false;

                            setup_voice_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_setup_voice_off));
                        } else {
                            isSinglence = !true;

                            setup_voice_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_setup_voice_no));
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        if (message != null && !"".equals(message)) {
                            BaseToast.makeLongToast(mContext, message);
                        } else {
                            BaseToast.makeLongToast(mContext, "设置失败，请稍后再试");
                        }
                        stopProgressDialog();
                    }
                });

                break;
            case R.id.setup_update:
                update();
                break;
            case R.id.function_rel:
                Intent function=new Intent(this,FunctionActivity.class);
                startActivity(function);
                break;


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
                        if (url.isUpdate()){
                            setup_update_text.setText("有可更新版本");
                            dialog("http://" + URLConstant.HOST + ":8084/frontend" + url.getUrl());
                        }else {
                            setup_update_text.setText("已是最新版本");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String message) {
                setup_update_text.setText("已是最新版本");
            }
        });


    }


    private void dialog(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetUpActivity.this);
        builder.setMessage("有新版本是否更新？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {
                if (IsWifi.isWifiActive(SetUpActivity.this)) {
                    dialog.dismiss();
                    DownLoadDialog dialog2 = new DownLoadDialog();
                    dialog2.setUrl(url);
                    Dialog dialogs = dialog2.alertDialog(SetUpActivity.this, SetUpActivity.this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(SetUpActivity.this);
        builder.setMessage("您当前非Wifi连接，是否继续更新？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (IsWifi.isWifiActive(SetUpActivity.this)) {
                    dialog.dismiss();
                    DownLoadDialog dialog2 = new DownLoadDialog();
                    dialog2.setUrl(url);
                    Dialog dialogs = dialog2.alertDialog(SetUpActivity.this, SetUpActivity.this);
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
     * 获取用户信息
     */
    private void getPersonData() {

        userAction.getPersonData(mContext, UserData.getSettingString(mContext, UserData.access_token), new AsyncHandler() {
            @Override
            public void onSuccess(Object obj) {
                PersenData persenData = (PersenData) obj;
                if (!persenData.isSilence()) {
                    setup_voice_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_setup_voice_no));
                    isSinglence = !true;
                } else {
                    setup_voice_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_setup_voice_off));
                    isSinglence = !false;
                }


            }

            @Override
            public void onFailure(int statusCode, String message) {

            }
        });


    }

}
