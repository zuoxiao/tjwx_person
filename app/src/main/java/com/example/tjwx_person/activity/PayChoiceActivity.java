package com.example.tjwx_person.activity;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.CouponDetailData;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.tool.pay.PayResult;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.Constant;
import com.example.tjwx_person.utils.UserData;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PayChoiceActivity extends BaseActivity {

	Context mContext;
	UserAction userAction;
	publishedData publised = null;
	CouponDetailData detail = null;

	// 微信支付
	private IWXAPI api;
	public static PayChoiceActivity isfinsh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_pay_choice, 1, "请选择支付方式");
		publised = (publishedData) getIntent().getSerializableExtra("publised");
		if (getIntent().getSerializableExtra("detail") != null) {
			detail = (CouponDetailData) getIntent().getSerializableExtra(
					"detail");
		}
		mContext = this;
		isfinsh = this;
		initView();

	}

	TextView pay_choice_money;
	RelativeLayout pay_choice_zfb, pay_choice_wx;
	TextView pay_choice_btn, callPhone;
	ImageView pay_zfb_img, pay_wx_img;
	boolean isZFB = false;
	boolean isWX = false;

	private void initView() {
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
		boolean isregiter = api.registerApp(Constant.APP_ID);
		pay_choice_money = (TextView) findViewById(R.id.pay_choice_money);
		pay_choice_btn = (TextView) findViewById(R.id.pay_choice_btn);
		callPhone = (TextView) findViewById(R.id.callPhone);
		pay_choice_zfb = (RelativeLayout) findViewById(R.id.pay_choice_zfb);
		pay_choice_wx = (RelativeLayout) findViewById(R.id.pay_choice_wx);
		pay_zfb_img = (ImageView) findViewById(R.id.pay_zfb_img);
		pay_wx_img = (ImageView) findViewById(R.id.pay_wx_img);
		pay_choice_wx.setOnClickListener(this);
		pay_choice_zfb.setOnClickListener(this);
		pay_choice_btn.setOnClickListener(this);
		callPhone.setOnClickListener(this);
		userAction = UserAction.getInstance();
		if (detail == null) {
			pay_choice_money.setText(publised.getAmount());
		} else {
			Double publisedAmount = Double.valueOf(publised.getAmount());
			Double detailAmount = Double.valueOf(detail.getAmount());
			Double amounts = publisedAmount - detailAmount;
			// java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			// String amount=df.format(amounts);

			BigDecimal b = new BigDecimal(amounts);
			BigDecimal amount = b.setScale(2, BigDecimal.ROUND_DOWN);

			BigDecimal minNumber = BigDecimal.valueOf(0.01);
			if (amount.compareTo(minNumber) == -1) {

				pay_choice_money.setText("0.01" );
			} else {
				pay_choice_money.setText(amount.toString() );
			}
		}
		// 通过WXAPIFactory工厂，获取IWXAPI的实例

		// api.handleIntent(getIntent(), this);
	}

	@Override
	public void backButton() {
		super.backButton();
		this.finish();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.pay_choice_btn:
			if (isWX || isZFB) {
				if (isWX) {
					WX_Pay();
				} else {
					ZFB_pay();
				}
			} else {
				BaseToast.makeShortToast(mContext, "请选择支付方式");
			}
			break;
		case R.id.callPhone:
			Intent intent1 = new Intent();
			intent1.setAction("android.intent.action.DIAL");
			intent1.setData(Uri.parse("tel:" + callPhone.getText().toString()));
			startActivity(intent1);
			break;
		case R.id.pay_choice_zfb:
			pay_choice_wx.setBackground(getResources().getDrawable(
					R.drawable.pay_choice_false));
			pay_zfb_img.setVisibility(View.VISIBLE);
			pay_wx_img.setVisibility(View.GONE);
			pay_choice_zfb.setBackground(getResources().getDrawable(
					R.drawable.pay_choice_true));
			isZFB = true;
			isWX = false;
			break;
		case R.id.pay_choice_wx:
			pay_choice_zfb.setBackground(getResources().getDrawable(
					R.drawable.pay_choice_false));
			pay_wx_img.setVisibility(View.VISIBLE);
			pay_zfb_img.setVisibility(View.GONE);
			pay_choice_wx.setBackground(getResources().getDrawable(
					R.drawable.pay_choice_true));
			isZFB = false;
			isWX = true;
			break;

		default:
			break;
		}

	}

	private void ZFB_pay() {
		String voucherId = "";
		if (detail != null) {
			voucherId = detail.getId();
		}
		userAction.ZFB_Pay(mContext,
				UserData.getSettingString(mContext, UserData.access_token),
				publised.getId(), voucherId, new AsyncHandler() {

					@Override
					public void onSuccess(Object obj) {

						final String payInfo = (String) obj;
						Log.d("", "");
						Runnable payRunnable = new Runnable() {

							@Override
							public void run() {
								// 构造PayTask 对象
								PayTask alipay = new PayTask(
										PayChoiceActivity.this);
								// 调用支付接口，获取支付结果
								String result = alipay.pay(payInfo, true);

								Message msg = new Message();
								msg.what = SDK_PAY_FLAG;
								msg.obj = result;
								mHandler.sendMessage(msg);
							}
						};

						// 必须异步调用
						Thread payThread = new Thread(payRunnable);
						payThread.start();
					}

					@Override
					public void onFailure(int statusCode, String message) {

					}
				});
	}

	private void WX_Pay() {
		String voucherId = "";
		if (detail != null) {
			voucherId = detail.getId();
		}

		userAction.WX_Pay(mContext,
				UserData.getSettingString(mContext, UserData.access_token),
				publised.getId(), voucherId, new AsyncHandler() {

					@Override
					public void onSuccess(Object obj) {
						if (obj != null && !"".equals(obj)) {
							String payInfo = (String) obj;
							JSONObject object;
							try {

								JSONObject json = new JSONObject(payInfo);
								PayReq req = new PayReq();
								// req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
								req.appId = json.getString("appid");
								req.partnerId = json.getString("partnerid");
								req.prepayId = json.getString("prepayid");
								req.nonceStr = json.getString("noncestr");
								req.timeStamp = json.getString("timestamp");
								req.packageValue = json.getString("package");
								req.sign = json.getString("sign");
								req.extData = "app data"; // optional
								Bundle bundle = new Bundle();
								bundle.putSerializable("publised", publised);
								req.toBundle(bundle);
								// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
								api.sendReq(req);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						Log.d("", "");

					}

					@Override
					public void onFailure(int statusCode, String message) {

					}
				});

	}

	private static final int SDK_PAY_FLAG = 1;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayChoiceActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();

					UserData.setSettingString(mContext, UserData.state,
							"CANCEL");
					UserData.setSettingBoolean(mContext, UserData.isPublish,
							false);
					PayChoiceActivity.this.finish();
					PayActivity.isFinsh.finish();
					Intent payOK = new Intent(PayChoiceActivity.this,
							Pay_OKActivity.class);
					payOK.putExtra("publised", publised);
					startActivity(payOK);
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayChoiceActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayChoiceActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();
						Intent payOK = new Intent(PayChoiceActivity.this,
								Pay_NOActivity.class);

						startActivity(payOK);
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};

	protected void onNewIntent(Intent intent) {
		if (intent != null) {
			boolean ispay = intent.getBooleanExtra("ispay", false);
			if (ispay) {

				Toast.makeText(PayChoiceActivity.this, "支付成功",
						Toast.LENGTH_SHORT).show();

				UserData.setSettingString(mContext, UserData.state, "CANCEL");
				UserData.setSettingBoolean(mContext, UserData.isPublish, false);
				PayChoiceActivity.this.finish();
				PayActivity.isFinsh.finish();
				Intent payOK = new Intent(PayChoiceActivity.this,
						Pay_OKActivity.class);
				payOK.putExtra("publised", publised);
				startActivity(payOK);

			} else {

				// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
				Toast.makeText(PayChoiceActivity.this, "支付失败",
						Toast.LENGTH_SHORT).show();
				Intent payOK = new Intent(PayChoiceActivity.this,
						Pay_NOActivity.class);

				startActivity(payOK);

			}
		}

	};

	// @Override
	// public void onReq(BaseReq arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onResp(BaseResp arg0) {
	// switch (arg0.getType()) {
	// case BaseResp.ErrCode.ERR_OK:
	//
	// Toast.makeText(PayChoiceActivity.this, "支付成功", Toast.LENGTH_SHORT)
	// .show();
	//
	// UserData.setSettingString(mContext, UserData.state, "CANCEL");
	// UserData.setSettingBoolean(mContext, UserData.isPublish, false);
	// PayChoiceActivity.this.finish();
	// PayActivity.isFinsh.finish();
	// Intent payOK = new Intent(PayChoiceActivity.this,
	// Pay_OKActivity.class);
	// payOK.putExtra("publised", publised);
	// startActivity(payOK);
	//
	// default:
	// break;
	// }
	//
	// }
}
