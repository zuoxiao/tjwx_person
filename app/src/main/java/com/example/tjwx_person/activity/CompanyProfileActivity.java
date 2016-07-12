package com.example.tjwx_person.activity;

import android.os.Bundle;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;

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
}
