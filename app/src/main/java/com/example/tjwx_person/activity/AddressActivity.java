package com.example.tjwx_person.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.baidu.location.b.f;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.adapter.AddressAdapter;
import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.tool.listView.XListView;
import com.example.tjwx_person.utils.UserData;
import com.umeng.analytics.MobclickAgent;

public class AddressActivity extends BaseActivity implements
        OnGetPoiSearchResultListener, OnGetGeoCoderResultListener {
    private PoiSearch mPoiSearch = null;
    private int load_Index = 0;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private List<AddressData> address = new ArrayList<AddressData>();
    private AddressData addresDetail = new AddressData();
    private AddressAdapter adapter;
    String addressName;

    // 定义搜索服务类

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_address, 3, "编辑地址");
        mContext = this;
        initView();
    }

    XListView address_list;
    private EditText address_edt;
    private ImageView address_search;
    private TextView address_city;
    private TextView address_common1, address_common2;

    private void initView() {

        address_list = (XListView) findViewById(R.id.address_list);
        address_edt = (EditText) findViewById(R.id.address_edt);
        address_search = (ImageView) findViewById(R.id.address_search);
        address_city = (TextView) findViewById(R.id.address_city);
        address_common1 = (TextView) findViewById(R.id.address_common1);
        address_common2 = (TextView) findViewById(R.id.address_common2);
        address_search.setOnClickListener(this);
        address_common1.setOnClickListener(this);
        address_common2.setOnClickListener(this);
        address_list.setPullLoadEnable(false);// 初始化时候 loadmore 不可见
        address_list.setPullRefreshEnable(false);
        address_common1.setText(UserData.getSettingString(this,
                UserData.User_address1));
        address_common2.setText(UserData.getSettingString(this,
                UserData.User_address2));
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        adapter = new AddressAdapter(address, getApplicationContext());
        address_list.setAdapter(adapter);
        address_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                addresDetail.setLatitude(address.get(position-1).getLatitude());
                addresDetail.setLongitude((address.get(position-1).getLongitude()));
                addresDetail.setName(address.get(position-1).getName());
                addresDetail.setAddress(address.get(position-1).getAddress());
                addressName = address.get(position-1).getName();
                LatLng ptCenter = new LatLng((address.get(position-1)
                        .getLatitude()), (address.get(position-1).getLongitude()));
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));

            }
        });

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        address_edt.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) address_edt.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(AddressActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city(address_city.getText().toString())
                            .keyword(address_edt.getText().toString())
                            .pageNum(load_Index));
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.address_search:
                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city(address_city.getText().toString())
                        .keyword(address_edt.getText().toString())
                        .pageNum(load_Index))


                ;
                break;
            case R.id.address_common1:
                if (!"".equals(UserData.getSettingString(mContext,
                        UserData.User_address1_lat))) {
                    addresDetail
                            .setLatitude(Double.valueOf(UserData.getSettingString(
                                    mContext, UserData.User_address1_lat)));
                    addresDetail
                            .setLongitude(Double.valueOf(UserData.getSettingString(
                                    mContext, UserData.User_address1_lot)));
                    addresDetail.setAddress(UserData.getSettingString(mContext,
                            UserData.User_address1));
                    addresDetail.setCity(UserData.getSettingString(mContext,
                            UserData.User_address1_city));
                    addresDetail.setProvince(UserData.getSettingString(mContext,
                            UserData.User_address1_province));
                    addresDetail.setDistrict(UserData.getSettingString(mContext,
                            UserData.User_address1_district));
                    addresDetail.setStreet(UserData.getSettingString(mContext,
                            UserData.User_address1_street));
                    addresDetail.setStreetNumber(UserData.getSettingString(
                            mContext, UserData.User_address1_streetNumber));
                    addresDetail.setName(UserData.getSettingString(mContext,
                            UserData.User_address1_name));
                    addresDetail.setComment(UserData.getSettingString(mContext,
                            UserData.User_address1_comment));
                    Intent repair1 = new Intent();
                    repair1.putExtra("address", addresDetail);
                    AddressActivity.this.setResult(2, repair1);
                    this.finish();
                }

                break;
            case R.id.address_common2:
                if (!"".equals(UserData.getSettingString(mContext,
                        UserData.User_address2_lat))) {
                    addresDetail
                            .setLatitude(Double.valueOf(UserData.getSettingString(
                                    mContext, UserData.User_address2_lat)));
                    addresDetail
                            .setLongitude(Double.valueOf(UserData.getSettingString(
                                    mContext, UserData.User_address2_lot)));
                    addresDetail.setAddress(UserData.getSettingString(mContext,
                            UserData.User_address2));
                    addresDetail.setCity(UserData.getSettingString(mContext,
                            UserData.User_address2_city));
                    addresDetail.setProvince(UserData.getSettingString(mContext,
                            UserData.User_address2_province));
                    addresDetail.setDistrict(UserData.getSettingString(mContext,
                            UserData.User_address2_district));
                    addresDetail.setStreet(UserData.getSettingString(mContext,
                            UserData.User_address2_street));
                    addresDetail.setStreetNumber(UserData.getSettingString(
                            mContext, UserData.User_address2_streetNumber));
                    addresDetail.setComment(UserData.getSettingString(mContext,
                            UserData.User_address2_comment));
                    addresDetail.setName(UserData.getSettingString(mContext,
                            UserData.User_address2_name));
                    Intent repair2 = new Intent();
                    repair2.putExtra("address", addresDetail);
                    AddressActivity.this.setResult(2, repair2);
                    this.finish();
                }
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

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    @Override
    public void onGetPoiResult(PoiResult result) {

        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(AddressActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(AddressActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
        List<PoiInfo> addre = result.getAllPoi();
        address.clear();
        if (addre != null) {
            for (int i = 0; i < addre.size(); i++) {
                if (addre.get(i).location != null && addre.get(i).type.name().equals("POINT")) {
                    AddressData addres = new AddressData();
                    addres.setAddress(addre.get(i).address);
                    addres.setName(addre.get(i).name);
                    addres.setLatitude(addre.get(i).location.latitude);
                    addres.setLongitude(addre.get(i).location.longitude);
                    address.add(addres);
                }


            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

        AddressComponent detail = result.getAddressDetail();
        String city = detail.city;
        String district = detail.district;
        String province = detail.province;
        String street = detail.street;
        String streetNumber = detail.streetNumber;
        Intent repair = new Intent();
        addresDetail.setCity(city);
        addresDetail.setDistrict(district);
        addresDetail.setProvince(province);
        addresDetail.setStreet(street);
        addresDetail.setStreetNumber(streetNumber);
        String address="";
        if(!addressName.contains(city)){
            address=city;
        }
        if (!addressName.contains(district)){
            address=address+district;
        }

        addresDetail.setAddress(address+ addressName);
        repair.putExtra("address", addresDetail);

        if (UserData.getSettingBoolean(mContext, UserData.User_isOneAddress,
                true)) {
            UserData.setSettingString(mContext, UserData.User_address1,
                    addresDetail.getAddress());
            UserData.setSettingString(mContext, UserData.User_address1_lat,
                    String.valueOf(addresDetail.getLatitude()));
            UserData.setSettingString(mContext, UserData.User_address1_lot,
                    String.valueOf(addresDetail.getLongitude()));
            UserData.setSettingString(mContext, UserData.User_address1_city,
                    String.valueOf(addresDetail.getCity()));
            UserData.setSettingString(mContext,
                    UserData.User_address1_province,
                    String.valueOf(addresDetail.getProvince()));
            UserData.setSettingString(mContext,
                    UserData.User_address1_district,
                    String.valueOf(addresDetail.getDistrict()));
            UserData.setSettingString(mContext, UserData.User_address1_street,
                    String.valueOf(addresDetail.getStreet()));
            UserData.setSettingString(mContext,
                    UserData.User_address1_streetNumber,
                    String.valueOf(addresDetail.getStreetNumber()));
            UserData.setSettingString(mContext, UserData.User_address1_comment,
                    String.valueOf(addresDetail.getComment()));
            UserData.setSettingString(mContext, UserData.User_address1_name,
                    String.valueOf(addresDetail.getName()));
            UserData.setSettingBoolean(mContext, UserData.User_isOneAddress,
                    false);
        } else {
            UserData.setSettingString(mContext, UserData.User_address2,
                    addresDetail.getAddress());
            UserData.setSettingString(mContext, UserData.User_address2_lat,
                    String.valueOf(addresDetail.getLatitude()));
            UserData.setSettingString(mContext, UserData.User_address2_lot,
                    String.valueOf(addresDetail.getLongitude()));
            UserData.setSettingString(mContext, UserData.User_address2_city,
                    String.valueOf(addresDetail.getCity()));
            UserData.setSettingString(mContext,
                    UserData.User_address2_province,
                    String.valueOf(addresDetail.getProvince()));
            UserData.setSettingString(mContext,
                    UserData.User_address2_district,
                    String.valueOf(addresDetail.getDistrict()));
            UserData.setSettingString(mContext, UserData.User_address2_street,
                    String.valueOf(addresDetail.getStreet()));
            UserData.setSettingString(mContext,
                    UserData.User_address2_streetNumber,
                    String.valueOf(addresDetail.getStreetNumber()));
            UserData.setSettingString(mContext, UserData.User_address2_comment,
                    String.valueOf(addresDetail.getComment()));
            UserData.setSettingString(mContext, UserData.User_address2_name,
                    String.valueOf(addresDetail.getName()));
            UserData.setSettingBoolean(mContext, UserData.User_isOneAddress,
                    true);
        }

        // 保存信息
        // UserData.setSettingString(mContext, UserData.User_address,
        // addresDetail.getAddress());
        // UserData.setSettingString(mContext, UserData.User_lat,
        // String.valueOf(addresDetail.getLat()));
        // UserData.setSettingString(mContext, UserData.User_lot,
        // String.valueOf(addresDetail.getLot()));
        AddressActivity.this.setResult(2, repair);
        this.finish();
    }

    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    //
    //
    // if (keyCode ==EditorInfo.IME_ACTION_SEARCH ) {// 修改回车键功能
    //
    // mPoiSearch.searchInCity((new PoiCitySearchOption())
    // .city(address_city.getText().toString())
    // .keyword(address_edt.getText().toString())
    // .pageNum(load_Index));
    // }
    // return super.onKeyDown(keyCode, event);
    // }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
