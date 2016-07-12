package com.cxcl.property.customer.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tjwx_person.activity.PayActivity;
import com.example.tjwx_person.activity.PayChoiceActivity;
import com.example.tjwx_person.activity.Pay_NOActivity;
import com.example.tjwx_person.activity.Pay_OKActivity;
import com.example.tjwx_person.utils.Constant;
import com.example.tjwx_person.utils.UserData;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.errCode==0) {

			Intent payOK = new Intent(WXPayEntryActivity.this,
					PayChoiceActivity.class);
			payOK.putExtra("ispay", true);
			startActivity(payOK);

		} else {
			Intent payOK = new Intent(WXPayEntryActivity.this,
					PayChoiceActivity.class);
			payOK.putExtra("ispay", false);
			startActivity(payOK);

		}
	}
}