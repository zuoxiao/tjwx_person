package com.example.tjwx_person.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.android.phone.mrpc.core.r;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.adapter.Repair_TypeAdapter;
import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.bean.OrderData;
import com.example.tjwx_person.bean.TypeData;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.bean.skillData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.tool.TypePicker;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.UserData;

/**
 * 普通维修
 *
 * @author zuo
 */
public class RepairActivity extends BaseActivity {

    private Context mContext;
    UserAction userAction;
    private List<skillData> typeDatas = new ArrayList<skillData>();
    private List<skillData> allTypeDatas = new ArrayList<skillData>();
    private HashMap<Integer, Boolean> isSelected = new HashMap<>();

    private Repair_TypeAdapter adapter;
    // 上传的信息
    AddressData address = null;
    OrderData orderData = null;
    private PopupWindow popupDay;
    private int isTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_repair, 3, "维修信息填写");
        mContext = this;
        initView();

    }

    private ListView repair_type;
    private RelativeLayout repair_gpsAddress;
    private TextView repair_gpsAddress_text, repair_order, repair_pulish,
            callPhone;
    private EditText repair_comment;
    private String typeId = "";
    private int typeNumber = -1;

    private void initView() {

        userAction = new UserAction();
        repair_gpsAddress_text = (TextView) findViewById(R.id.repair_gpsAddress_text);
        repair_gpsAddress = (RelativeLayout) findViewById(R.id.repair_gpsAddress);
        repair_gpsAddress.setOnClickListener(this);
        repair_type = (ListView) findViewById(R.id.repair_type);
        repair_comment = (EditText) findViewById(R.id.repair_comment);
        adapter = new Repair_TypeAdapter(mContext, typeDatas, isSelected);
        repair_type.setAdapter(adapter);
        repair_type.setItemsCanFocus(false);
        repair_order = (TextView) findViewById(R.id.repair_order);
        repair_order.setOnClickListener(this);
        repair_pulish = (TextView) findViewById(R.id.repair_pulish);
        repair_pulish.setOnClickListener(this);
        callPhone = (TextView) findViewById(R.id.callPhone);
        callPhone.setOnClickListener(this);
        repair_type.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                typeNumber = position;
                if (typeDatas.get(position).isHaveNode()) {
                    initCityPop(view);
                    List<skillData> datas = new ArrayList<skillData>();
                    for (skillData skillData : allTypeDatas) {
                        if (skillData.getFatherId() != null && skillData.getFatherId().equals(allTypeDatas.get(position).getId())) {
                            datas.add(skillData);
                        }
                    }

                    citypicker.setData(datas);
                } else {
                    if (isSelected.get(position) == false) {
                        init();
                        isSelected.put(position, true);
                        isTrue = position;
                    } else {
                        init();
                        isSelected.put(position, false);
                        isTrue = -1;
                    }
                    adapter.notifyDataSetChanged();
                }


            }
        });
        if (getIntent().getSerializableExtra("addresDetail") != null) {
            address = (AddressData) getIntent().getSerializableExtra(
                    "addresDetail");

        }
        if (address != null && !"".equals(address.getAddress())) {
            repair_gpsAddress_text.setText(address.getAddress());
        }
        userAction.getTypes(mContext,
                UserData.getSettingString(mContext, UserData.xg_token),
                UserData.getSettingString(mContext, UserData.access_token), false,
                true, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        List<skillData> typeDatass = (List<skillData>) obj;

                        if (typeDatas != null) {
                            typeDatas.clear();
                            allTypeDatas.clear();
                        }
                        allTypeDatas.addAll(typeDatass);
                        for (skillData skillData : typeDatass) {
                            if (skillData.getFatherId() == null)
                                typeDatas.add(skillData);
                        }
                        repair_type.measure(0, 0);
                        int height = repair_type.getMeasuredHeight();
                        LayoutParams params = new LayoutParams(
                                LayoutParams.FILL_PARENT, height
                                * typeDatas.size());
                        repair_type.setLayoutParams(params);
                        for (int i = 0; i < typeDatas.size(); i++) {
                            isSelected.put(i, false);
                        }
                        Log.d("", "");
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        // TODO Auto-generated
                        // method stub

                    }
                });
    }

    public void init() {

        for (int i = 0; i < isSelected.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.repair_gpsAddress:

                Intent addressActivity = new Intent(this, AddressActivity.class);
                startActivityForResult(addressActivity, 1);
                break;
            case R.id.repair_order:
                Intent order = new Intent(this, OrderDetailActivity.class);
                order.putExtra("orderData", orderData);
                startActivityForResult(order, 2);
                break;
            case R.id.repair_pulish:
                String skillIds = null;
                for (int i = 0; i < typeDatas.size(); i++) {
                    if (isSelected.get(i)) {
                        if (typeDatas.get(i).isHaveNode()) {
                            skillIds = typeId;
                        } else {
                            skillIds = typeDatas.get(i).getId();
                        }

                    }
                }
                if (skillIds != null && address != null) {
                    if (Utils.isNetworkAvailable(mContext)) {
                        if (!"".equals(UserData
                                .getSettingString(mContext, UserData.user_ID))) {
                            publish();
                        } else {
                            Intent regist = new Intent(this, RegistActivity.class);
                            regist.putExtra("Regist_type", 1);
                            startActivityForResult(regist, 4);

                        }

                    } else {
                        BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                    }

                } else {
                    if (skillIds == null) {
                        BaseToast.makeLongToast(mContext, "请选择维修类型");
                    } else {
                        BaseToast.makeLongToast(mContext, "请选择维修地点");
                    }
                }


                break;
            case R.id.callPhone:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.DIAL");
                intent1.setData(Uri.parse("tel:" + callPhone.getText().toString()));
                startActivity(intent1);
                break;
            case R.id.city_background:
                popupDay.dismiss();


                break;
            case R.id.wancheng:
                typeId = citypicker.getTypeId();
                try {
                    if (!typeId.equals("")) {

                        init();
                        isSelected.put(typeNumber, true);

                        try {
                            List<skillData> typeDatass = new ArrayList<>();
                            typeDatass.addAll(typeDatas);
                            //  String[] typeData = typeDatass.get(typeNumber).getDescription().split(":");
                            typeDatass.get(typeNumber).setDescription(citypicker.getTypeName());
                            typeDatas.clear();
                            typeDatas.addAll(typeDatass);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();
                        popupDay.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("isTrue", isTrue);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int isTrue = savedInstanceState.getInt("isTrue");
        if (isTrue != -1) {
            isSelected.put(isTrue, true);
        }

    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            address = (AddressData) data.getSerializableExtra("address");
            repair_gpsAddress_text.setText(address.getAddress());
            Log.d("", "");
        }
        if (resultCode == 3) {
            orderData = (OrderData) data.getSerializableExtra("orderData");

        }
        if (resultCode == 4) {//未登陆时登陆以后返回的
            if (!"".equals(UserData
                    .getSettingString(mContext, UserData.user_ID))) {
                published();
            } else {
                BaseToast.makeShortToast(mContext, "自动提交失败，请重试");
            }
        }
    }

    File[] pictures = null;

    /**
     * 发布
     */
    private void publish() {

        File audio = null;
        String Comment = null;
        String skillIds = null;
        if (orderData != null) {

            if (orderData.getAudio() != null
                    && !"".equals(orderData.getAudio())) {
                audio = new File(orderData.getAudio());
            }
            if (orderData.getPictures() != null) {
                pictures = new File[orderData.getImgNumber()];
                for (int i = 0; i < orderData.getImgNumber(); i++) {
                    pictures[i] = new File(orderData.getPictures().get(i)
                            .replace("file:", ""));
                }
            }
            if (orderData.getComment() != null
                    && !"".equals(orderData.getComment())) {
                Comment = orderData.getComment();
            }
        }
        for (int i = 0; i < typeDatas.size(); i++) {
            if (isSelected.get(i)) {
                if (typeDatas.get(i).isHaveNode()) {
                    skillIds = typeId;
                } else {
                    skillIds = typeDatas.get(i).getId();
                }

            }
        }
        if (skillIds != null && address != null) {
            // 判断数据是否可以提交
            startProgressDialog();
            userAction.publish(mContext, pictures, audio, Comment, false,
                    skillIds, address.getAddress()
                            + repair_comment.getText().toString(),
                    address.longitude, address.getLatitude(),
                    UserData.getSettingString(mContext, UserData.access_token), false,
                    new AsyncHandler() {

                        @Override
                        public void onSuccess(Object obj) {
                            stopProgressDialog();
                            com.example.tjwx_person.utils.BaseToast
                                    .makeLongToast(mContext, "发布成功");
                            RepairActivity.this.finish();
                            UserData.setSettingBoolean(mContext,
                                    UserData.isPublish, true);
                            UserData.setSettingString(mContext, UserData.state,
                                    "PUBLISH");
                            MainActivity.isRush = true;
                            Log.d("", "");
                            try {
                                for (int i = 0; i < pictures.length; i++) {
                                    pictures[i].delete();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String message) {

                            if (message != null && !"".equals(message)) {
                                BaseToast.makeShortToast(mContext, message);
                            } else {
                                com.example.tjwx_person.utils.BaseToast
                                        .makeLongToast(mContext, "发布失败，请稍后再试");
                            }

                            stopProgressDialog();
                            Log.d("", "");

                        }
                    });
        } else {
            if (skillIds == null) {
                BaseToast.makeLongToast(mContext, "请选择维修类型");
            } else {
                BaseToast.makeLongToast(mContext, "请选择维修地点");
            }
        }

    }


    /**
     * 查询订单
     */
    private void published() {
        String access_token = UserData.getSettingString(mContext,
                UserData.access_token);
/*        if (access_token != null
                && !"".equals(UserData.getSettingString(mContext,
                UserData.access_token))) {*/
        startProgressDialog();
        userAction.published(mContext, access_token, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        stopProgressDialog();
                        if (obj != null && !"".equals(obj)) {
                            publishedData publised = (publishedData) obj;
                            String state = publised.getState();
                            if (state != null && !"".equals(state)) {
                                if (state.equals("PICKER")) {
                                    Intent intent = new Intent(RepairActivity.this,
                                            DisSuccessActivity.class);
                                    intent.putExtra("publised", publised);
                                    startActivity(intent);
                                } else if (state.equals("COMMIT")) {
                                    Intent pay = new Intent(RepairActivity.this,
                                            PayActivity.class);
                                    pay.putExtra("publised", publised);
                                    startActivity(pay);
                                }
                            }
                            UserData.setSettingBoolean(mContext,
                                    UserData.isPublish, true);
                            RepairActivity.this.finish();
                            BaseToast.makeShortToast(mContext, "您有未完成订单");
                        } else {
                            publish();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        stopProgressDialog();
                        Log.d("published", "onFailure");
                    }
                }

        );

    }
    //   }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            for (int i = 0; i < pictures.length; i++) {
                pictures[i].delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    TypePicker citypicker;

    private void initCityPop(View v) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(this);
        View view = layoutInflater.inflate(R.layout.popwindow_type, null);
        popupDay = new PopupWindow(view, LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        ColorDrawable color = new ColorDrawable(0x55000000);
        popupDay.setBackgroundDrawable(color);
        popupDay.setOutsideTouchable(true);
        popupDay.setFocusable(true);
        popupDay.setAnimationStyle(R.style.popwin_anim_style);
        popupDay.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        citypicker = new TypePicker(view, this);
        RelativeLayout rela = (RelativeLayout) view
                .findViewById(R.id.city_background);
        rela.setOnClickListener(this);
        TextView confirm = (TextView) view.findViewById(R.id.wancheng);
        confirm.setOnClickListener(this);
    }


}
