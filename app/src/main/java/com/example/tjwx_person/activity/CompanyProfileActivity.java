package com.example.tjwx_person.activity;

import android.os.Bundle;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zuo on 2016/6/24.
 */
public class CompanyProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_company_profile,1,"公司简介");


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
