package com.example.tjwx_person.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.bean.publishedData;
import com.umeng.analytics.MobclickAgent;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class Pay_OKActivity extends BaseActivity {

	TextView callPhone, pay_ok_compares, share;
	publishedData publised = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addContentView(R.layout.activity_pay_ok, 1, "支付状态");
		publised = (publishedData) getIntent().getSerializableExtra("publised");
		callPhone = (TextView) findViewById(R.id.callPhone);
		pay_ok_compares = (TextView) findViewById(R.id.pay_ok_compares);
		share = (TextView) findViewById(R.id.share);
		callPhone.setOnClickListener(this);
		pay_ok_compares.setOnClickListener(this);
		share.setOnClickListener(this);
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
		case R.id.pay_ok_compares:
			Intent compares = new Intent(this, ComparesActivity.class);
			compares.putExtra("publised", publised);
			startActivity(compares);
			this.finish();
			break;
		case R.id.share:
			showShare(this, null, true);
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

	public static void showShare(Context context, String platformToShare,
			boolean showContentEdit) {

		OnekeyShare oks = new OnekeyShare();
		oks.setSilent(!showContentEdit);
		if (platformToShare != null) {
			oks.setPlatform(platformToShare);
		}
		// ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC 第二个是SKYBLUE
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		// oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
		oks.setTitle("中房我来修修");
		oks.setTitleUrl("http://mob.com");
		oks.setText(" 最好用的物业维修APP  -- 中房我来修修 闪亮登场！邻居们，家里水、电、暖出了问题不用急，“中房我来修修”为您搭桥牵线解难题！赶快下载吧！");
		// oks.setImagePath("/sdcard/test-pic.jpg"); //分享sdcard目录下的图片
		// oks.setImageUrl(randomPic()[0]);
		oks.setUrl("http://www.mob.com"); // 微信不绕过审核分享链接
		// oks.setFilePath("/sdcard/test-pic.jpg");
		// //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
		oks.setComment("分享"); // 我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
		oks.setSite("ShareSDK"); // QZone分享完之后返回应用时提示框上显示的名称
		oks.setSiteUrl("http://mob.com");// QZone分享参数
		oks.setVenueName("ShareSDK");
		oks.setVenueDescription("This is a beautiful place!");
		// 将快捷分享的操作结果将通过OneKeyShareCallback回调
		// oks.setCallback(new OneKeyShareCallback());
		// 去自定义不同平台的字段内容
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());
		// 在九宫格设置自定义的图标
		Bitmap logo = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
		String label = "ShareSDK";
		// OnClickListener listener = new OnClickListener() {
		// public void onClick(View v) {
		//
		// }
		// };
		// oks.setCustomerLogo(logo, label, listener);

		// 为EditPage设置一个背景的View
		// oks.setEditPageBackground(getPage());
		// 隐藏九宫格中的新浪微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);

		// String[] AVATARS = {
		// "http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
		// "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
		// "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
		// "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
		// "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
		// "http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
		// };
		// oks.setImageArray(AVATARS); //腾讯微博和twitter用此方法分享多张图片，其他平台不可以

		// 启动分享
		oks.show(context);
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
