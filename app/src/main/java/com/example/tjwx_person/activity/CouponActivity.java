package com.example.tjwx_person.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.adapter.CouponAdapter;
import com.example.tjwx_person.bean.CouponData;
import com.example.tjwx_person.bean.CouponDetailData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.tool.listView.XListView;
import com.example.tjwx_person.tool.listView.XListView.IXListViewListener;
import com.example.tjwx_person.utils.UserData;

/**
 * 优惠券
 * 
 * @author zuo
 * 
 */
public class CouponActivity extends BaseActivity implements IXListViewListener {

	XListView couponList;
	CouponAdapter adapter;
	List<CouponDetailData> couponData = new ArrayList<CouponDetailData>();
	UserAction action;
	Context mContext;
	int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_coupon, 1, "我的优惠券");
		couponList = (XListView) findViewById(R.id.couponList);
		couponList.setPullLoadEnable(false);// 初始化时候 loadmore 不可见
		couponList.setPullRefreshEnable(true);
		couponList.setXListViewListener(this);
		adapter = new CouponAdapter(couponData, this);
		couponList.setAdapter(adapter);
		action = action.getInstance();
		mContext = this;
		select();
	}

	@Override
	public void onRefresh() {
		page = 0;
		if (couponData != null) {
			couponData.clear();
		}
		select();

	}

	@Override
	public void onLoadMore() {
		select();
	}

	@Override
	public void backButton() {
		super.backButton();
		this.finish();

	}

	// 停止加载动画
	private void stopLoad() {
		couponList.stopRefresh();
		couponList.stopLoadMore();
		String dat = Utils.formatDate(new Date().getTime());
		couponList.setRefreshTime(dat);
	}

	private void select() {
		action.getCoupon(this,
				UserData.getSettingString(this, UserData.access_token),
				String.valueOf(page), new AsyncHandler() {

					@Override
					public void onSuccess(Object obj) {
						if (obj != null && !"".equals(obj)) {
							CouponData data = (CouponData) obj;

							UserData.setSettingString(mContext,
									UserData.totalElements,
									data.getTotalElements());
							couponData.addAll(data.getContent());
							adapter.notifyDataSetChanged();
							if (data.isLast()) {
								couponList.setPullLoadEnable(false);// 初始化时候
																	// loadmore
																	// 不可见
							} else {
								couponList.setPullLoadEnable(true);// 初始化时候
																	// loadmore
																	// 不可见
								page++;
							}
						}

						stopLoad();
					}

					@Override
					public void onFailure(int statusCode, String message) {
						stopLoad();
					}
				});
	}

}
