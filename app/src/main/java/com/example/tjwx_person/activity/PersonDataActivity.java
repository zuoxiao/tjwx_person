package com.example.tjwx_person.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.UserData;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zuo on 2016/6/19.
 */
public class PersonDataActivity extends BaseActivity {
    UserAction userAction;
    private RelativeLayout repair_gpsAddress;
    private LinearLayout person_data_number;
    private TextView person_phone;
    private TextView person_address_text;
    private TextView person_addresss1_text;
    private ImageView imageView1;
    private EditText person_addresss2_text;
    public AddressData address = new AddressData();
    Context mContext;

    private boolean isAddAddress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_person_data, 6, "个人信息");
        userAction = UserAction.getInstance();
        initView();
        selectAddress();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.repair_gpsAddress:

                Intent addressIntent = new Intent(this, AddressActivity.class);
                startActivityForResult(addressIntent, 1);
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2) {
            AddressData addres = (AddressData) data
                    .getSerializableExtra("address");
            address.setAddress(addres.getAddress());
            address.setCity(addres.getCity());
            address.setDistrict(addres.getDistrict());
            address.setLatitude(addres.getLatitude());
            address.setLongitude(addres.getLongitude());
            address.setProvince(addres.getProvince());
            address.setStreet(addres.getStreet());
            address.setStreetNumber(addres.getStreetNumber());
            address.setName(addres.getName());
            if (person_addresss1_text.getText().toString().equals("")) {
                //    addAddress();
                isAddAddress = true;
            } else {
                isAddAddress = false;
                //   updateAddress();
            }
            String addresText = "";
            if (address.getProvince() != null
                    && !address.getProvince().equals("")) {
                addresText = address.getProvince();
            }
            if (address.getCity() != null
                    && !address.getCity().equals("")) {
                if (!address.getCity().equals(address.getProvince())) {
                    addresText = addresText + address.getCity();
                }

            }
            if (address.getDistrict() != null
                    && !address.getDistrict().equals("")) {
                addresText = addresText + address.getDistrict();
            }
            if (address.getStreet() != null
                    && !address.getStreet().equals("")) {
                addresText = addresText + address.getName();
            }

            person_addresss1_text.setText("");
            person_addresss1_text.setText(address.getAddress());
        }

    }

    private void dialog() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入楼门号")
                .setIcon(android.R.drawable.ic_dialog_info)

                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.cancel();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        }).show();
    }

    private void updateAddress() {
        stopProgressDialog();
        userAction.updateAddress(mContext,
                UserData.getSettingString(mContext, UserData.access_token),
                address, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        stopProgressDialog();
                        BaseToast.makeLongToast(mContext, "修改成功");
                        String addres = "";
                        if (address.getProvince() != null
                                && !address.getProvince().equals("")) {
                            addres = address.getProvince();
                        }
                        if (address.getCity() != null
                                && !address.getCity().equals("")) {
                            if (!address.getCity().equals(address.getProvince())) {
                                addres = addres + address.getCity();
                            }

                        }
                        if (address.getDistrict() != null
                                && !address.getDistrict().equals("")) {
                            addres = addres + address.getDistrict();
                        }
                        if (address.getStreet() != null
                                && !address.getStreet().equals("")) {
                            addres = addres + address.getName();
                        }


                        person_addresss1_text.setText(addres);
                        person_addresss2_text.setText(address.getComment());
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        if (message != null && !message.equals("")) {
                            BaseToast.makeLongToast(mContext, message);
                        } else {
                            BaseToast.makeLongToast(mContext, "修改失败，请稍后再试！");
                        }
                        stopProgressDialog();

                    }
                });
    }

    private void addAddress() {

        userAction.AddAddress(mContext,
                UserData.getSettingString(mContext, UserData.access_token),
                address, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        person_addresss1_text.setText(address.getAddress());
                        BaseToast.makeLongToast(mContext, "修改成功");

                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        if (message != null && !message.equals("")) {
                            BaseToast.makeLongToast(mContext, message);
                        } else {
                            BaseToast.makeLongToast(mContext, "修改失败，请稍后再试！");
                        }

                    }
                });

    }

    private void initView() {
        mContext = this;
        repair_gpsAddress = (RelativeLayout) findViewById(R.id.repair_gpsAddress);
        repair_gpsAddress.setOnClickListener(this);
        person_data_number = (LinearLayout) findViewById(R.id.person_data_number);
        person_data_number.setOnClickListener(this);
        person_phone = (TextView) findViewById(R.id.person_phone);
        person_phone.setOnClickListener(this);
        person_address_text = (TextView) findViewById(R.id.person_address_text);
        person_addresss1_text = (TextView) findViewById(R.id.person_addresss1_text);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        person_addresss2_text = (EditText) findViewById(R.id.person_addresss2_text);
        person_addresss2_text.setOnClickListener(this);

        person_phone.setText(UserData.getSettingString(mContext,
                UserData.user_phone));
    }

    private void selectAddress() {
        userAction.selectAddress(mContext,
                UserData.getSettingString(mContext, UserData.access_token),
                new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        if (obj != null && !"".equals(obj)) {
                            address = (AddressData) obj;
                            if (address != null) {
                                String addres = "";
                                if (address.getProvince() != null
                                        && !address.getProvince().equals("")) {
                                    addres = address.getProvince();
                                }
                                if (address.getCity() != null
                                        && !address.getCity().equals("")) {
                                    if (!address.getCity().equals(address.getProvince())) {
                                        addres = addres + address.getCity();
                                    }
                                }
                                if (address.getDistrict() != null
                                        && !address.getDistrict().equals("")) {
                                    addres = addres + address.getDistrict();
                                }
                                if (address.getStreet() != null
                                        && !address.getStreet().equals("")) {
                                    addres = addres + address.getStreet();
                                }
                                if (address.getStreetNumber() != null
                                        && !address.getStreetNumber()
                                        .equals("")) {
                                    addres = addres + address.getStreetNumber();
                                }

                                person_addresss1_text.setText(addres);
                                if (address.getComment() != null
                                        && !address.getComment().equals("")) {
                                    person_addresss2_text.setText(address
                                            .getComment());
                                }
                                PersonDataActivity.this.address.setId(address
                                        .getId());
                            }
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        Log.d("", "");

                    }
                });
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    @Override
    public void keep() {
        super.keep();
        address.setComment(person_addresss2_text.getText().toString());
        if (isAddAddress) {
            addAddress();
        } else {
            updateAddress();
        }

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
