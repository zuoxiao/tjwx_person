package com.example.tjwx_person.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.tool.DownLoadDialog;
import com.example.tjwx_person.tool.IsWifi;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zuo on 2016/6/20.
 */
public class Person_AboutWeActivity extends BaseActivity {

    private LinearLayout person_yijianfankui;
    private LinearLayout person_lianxikefu;
    private LinearLayout person_falvtiaokuan;
    private LinearLayout person_gongsijieshao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addContentView(R.layout.activity_about_we, 1, "关于我们");
        initView();

    }

    private void initView() {
        person_yijianfankui = (LinearLayout) findViewById(R.id.person_yijianfankui);
        person_lianxikefu = (LinearLayout) findViewById(R.id.person_lianxikefu);
        person_falvtiaokuan = (LinearLayout) findViewById(R.id.person_falvtiaokuan);
        person_gongsijieshao = (LinearLayout) findViewById(R.id.person_gongsijieshao);
        person_yijianfankui.setOnClickListener(this);
        person_lianxikefu.setOnClickListener(this);
        person_falvtiaokuan.setOnClickListener(this);
        person_gongsijieshao.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.person_yijianfankui:
                Intent yijianfankui = new Intent(this, FeelBackActivity.class);
                startActivity(yijianfankui);

                break;
            case R.id.person_lianxikefu:
                dialog();
                break;
            case R.id.person_falvtiaokuan:
                Intent falvtiaokuan=new Intent(this,CompanyClauseActivity.class);
                startActivity(falvtiaokuan);
                break;
            case R.id.person_gongsijieshao:
                Intent gongsijieshao=new Intent(this,CompanyProfileActivity.class);
                startActivity(gongsijieshao);

                break;

        }

    }

    public void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Person_AboutWeActivity.this);
        builder.setMessage("是否拨打电话");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.DIAL");
                intent1.setData(Uri.parse("tel:" + "4000077200"));
                startActivity(intent1);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


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
