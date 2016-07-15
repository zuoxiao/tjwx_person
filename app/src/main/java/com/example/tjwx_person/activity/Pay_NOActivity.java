package com.example.tjwx_person.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.bean.publishedData;
import com.umeng.analytics.MobclickAgent;

public class Pay_NOActivity extends BaseActivity {

	TextView callPhone, pay_no_back;
	publishedData publised = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addContentView(R.layout.activity_pay_no, 1, "支付状态");
		publised = (publishedData) getIntent().getSerializableExtra("publised");
		callPhone = (TextView) findViewById(R.id.callPhone);
		pay_no_back = (TextView) findViewById(R.id.pay_no_back);
		callPhone.setOnClickListener(this);
		pay_no_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.callPhone:
			Intent intent1 = new Intent();
			intent1.setAction("android.intent.action.DIAL");
			intent1.setData(Uri.parse("tel:" + callPhone.getText().toString()));
			startActivity(intent1);
		case R.id.pay_no_back:
			this.finish();
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
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
