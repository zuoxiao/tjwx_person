package com.example.tjwx_person.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.adapter.person_orderAdapter;
import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.bean.selectOrders;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.tool.listView.XListView;
import com.example.tjwx_person.tool.listView.XListView.IXListViewListener;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.UserData;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class PersonAcivity extends BaseActivity {

    UserAction userAction;
    Context mContext;
    public AddressData address = new AddressData();
    List<publishedData> publishedData = new ArrayList<publishedData>();

    int page = 0;
    private LinearLayout person_gerenxinxi;
    private LinearLayout person_wodedingdan;
    private LinearLayout person_youhuiquan;
    private LinearLayout person_xiaoxi;
    private LinearLayout person_jiamubiao;
    private LinearLayout person_guanyuwomen;
    private LinearLayout person_fenxiang;
    private LinearLayout person_shezhi;
    private TextView person_signout;
    private TextView person_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_person, 1, "个人信息");

        mContext = this;
        initView();
        //       selectOrder();

    }


    private void initView() {
        //       share = (TextView) findViewById(R.id.share);

//        person_phone = (TextView) findViewById(R.id.person_phone);
//        person_addresss1_text = (TextView) findViewById(R.id.person_addresss1_text);
//        person_addresss2_text = (TextView) findViewById(R.id.person_addresss2_text);
//        person_discount = (TextView) findViewById(R.id.person_discount);
        //        person_address2_img = (ImageView) findViewById(R.id.person_address2_img);
//        person_address1_img = (ImageView) findViewById(R.id.person_address1_img);
//        share.setOnClickListener(this);
//        callPhone = (TextView) findViewById(R.id.callPhone);

//        person_list = (XListView) findViewById(R.id.person_list);
//        person_list.setXListViewListener(this);
//        person_discount.setOnClickListener(this);
//        callPhone.setOnClickListener(this);
//        person_address2_img.setOnClickListener(this);
//        person_address1_img.setOnClickListener(this);
        userAction = UserAction.getInstance();
//        person_list.setPullLoadEnable(false);// 初始化时候 loadmore 不可见
//        person_list.setPullRefreshEnable(false);
//
//        adaper = new person_orderAdapter(publishedData, mContext);
//        person_list.setAdapter(adaper);
        person_phone = (TextView) findViewById(R.id.person_phone);
        person_phone.setText(UserData.getSettingString(mContext,
                UserData.user_phone));
        person_gerenxinxi = (LinearLayout) findViewById(R.id.person_gerenxinxi);
        person_gerenxinxi.setOnClickListener(this);
        person_wodedingdan = (LinearLayout) findViewById(R.id.person_wodedingdan);
        person_wodedingdan.setOnClickListener(this);
        person_youhuiquan = (LinearLayout) findViewById(R.id.person_youhuiquan);
        person_youhuiquan.setOnClickListener(this);
        person_xiaoxi = (LinearLayout) findViewById(R.id.person_xiaoxi);
        person_xiaoxi.setOnClickListener(this);
        person_jiamubiao = (LinearLayout) findViewById(R.id.person_jiamubiao);
        person_jiamubiao.setOnClickListener(this);
        person_guanyuwomen = (LinearLayout) findViewById(R.id.person_guanyuwomen);
        person_guanyuwomen.setOnClickListener(this);
        person_fenxiang = (LinearLayout) findViewById(R.id.person_fenxiang);
        person_fenxiang.setOnClickListener(this);
        person_shezhi = (LinearLayout) findViewById(R.id.person_shezhi);
        person_shezhi.setOnClickListener(this);
        person_signout = (TextView) findViewById(R.id.person_signout);
        person_signout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.person_youhuiquan:
                Intent coupon = new Intent(this, CouponActivity.class);
                startActivityForResult(coupon, 1);
                break;
//            case R.id.callPhone:
//                Intent intent1 = new Intent();
//                intent1.setAction("android.intent.action.DIAL");
//                intent1.setData(Uri.parse("tel:" + callPhone.getText().toString()));
//            startActivity(intent1);
//            break;
//            case R.id.person_address2_img:
//                dialog();
//                break;
//            case R.id.person_address1_img:
//                Intent addressIntent = new Intent(this, AddressActivity.class);
//                startActivityForResult(addressIntent, 1);
//                break;

            case R.id.person_fenxiang:
                showShare(this, null, true);
                break;
            case R.id.person_signout:
                showDialog();
                break;
            case R.id.person_gerenxinxi:
                Intent gerenxinxi = new Intent(this, PersonDataActivity.class);
                startActivity(gerenxinxi);
                break;
            case R.id.person_guanyuwomen:
                Intent guanyuwomen = new Intent(this, Person_AboutWeActivity.class);
                startActivity(guanyuwomen);
                break;
            case R.id.person_wodedingdan:
                Intent wodedingdan = new Intent(this, OrderListActivity.class);
                startActivity(wodedingdan);
                break;
            case R.id.person_jiamubiao:
                Intent jiamubiao = new Intent(this, PricelistActivity.class);
                startActivity(jiamubiao);
                break;
            case R.id.person_shezhi:
                Intent setUp = new Intent(this, SetUpActivity.class);
                startActivity(setUp);
                break;
            case R.id.person_xiaoxi:
                Intent notice = new Intent(this, NoticeActivity.class);
                startActivity(notice);
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


    public static void showShare(Context context, String platformToShare, boolean showContentEdit) {

        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }
        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        //oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
        oks.setTitle("中房我来修修");
        oks.setTitleUrl("http://mob.com");
        oks.setText(" 最好用的物业维修APP  -- 中房我来修修 闪亮登场！邻居们，家里水、电、暖出了问题不用急，“中房我来修修”为您搭桥牵线解难题！赶快下载吧！");
        //oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
        //oks.setImageUrl(randomPic()[0]);
        oks.setUrl("http://www.mob.com"); //微信不绕过审核分享链接
        //oks.setFilePath("/sdcard/test-pic.jpg");  //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
        oks.setComment("分享"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
        oks.setSite("ShareSDK");  //QZone分享完之后返回应用时提示框上显示的名称
        oks.setSiteUrl("http://mob.com");//QZone分享参数
        oks.setVenueName("ShareSDK");
        oks.setVenueDescription("This is a beautiful place!");
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        String label = "ShareSDK";
        // 启动分享
        oks.show(context);
    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonAcivity.this);
        builder.setMessage("确认退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                UserData.setSettingString(PersonAcivity.this, UserData.clientSecret, "");
                UserData.setSettingString(PersonAcivity.this, UserData.user_ID, "");
                UserData.setSettingString(PersonAcivity.this, UserData.user_phone,"");
                UserData.setSettingString(PersonAcivity.this, UserData.access_token,"");
                XGPushManager.registerPush(mContext, "*");
                PersonAcivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void signOut() {
        super.signOut();
        showDialog();
    /*    Intent loging = new Intent(this, RegistActivity.class);
        startActivity(loging);*/
    }


}
