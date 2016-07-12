package com.example.tjwx_person.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.adapter.person_orderAdapter;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.bean.selectOrders;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.tool.listView.XListView;
import com.example.tjwx_person.utils.UserData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zuo on 2016/6/23.
 */
public class OrderListActivity extends BaseActivity implements XListView.IXListViewListener {
    person_orderAdapter adaper;
    XListView person_list;
    private Context mContext;
    UserAction userAction;
    int page = 0;
    List<publishedData> publishedData = new ArrayList<publishedData>();
    boolean isRefresh=false;
    public static  String ACTION_NAME="OrderListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_order, 1, "我的订单");
        //注册广播
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        mContext = this;
        userAction = UserAction.getInstance();
        person_list = (XListView) findViewById(R.id.order_list);
        person_list.setXListViewListener(this);
        person_list.setPullLoadEnable(false);// 初始化时候 loadmore 不可见
        person_list.setPullRefreshEnable(false);
        adaper = new person_orderAdapter(publishedData, mContext);
        person_list.setAdapter(adaper);
        person_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (publishedData.get(position - 1).getState().equals("REFUSAL")) {
                    Intent bounced = new Intent(OrderListActivity.this, OrderBouncedActivity.class);
                    bounced.putExtra("publishedData", publishedData.get(position - 1));
                    startActivity(bounced);
                } else if (publishedData.get(position - 1).getState().equals("CANCEL")) {
                    Intent bounced = new Intent(OrderListActivity.this, OrderBouncedActivity.class);
                    bounced.putExtra("publishedData", publishedData.get(position - 1));
                    startActivity(bounced);
                } else if (publishedData.get(position-1).getState().equals("PAYMENT")) {
                    Intent bounced = new Intent(OrderListActivity.this, OrderFinishedActivity.class);
                    bounced.putExtra("publishedData", publishedData.get(position - 1));
                    startActivity(bounced);

                }
            }
        });
        ImageView emptyView = new ImageView(this);
        emptyView.setImageDrawable(getResources().getDrawable(R.drawable.icon_list_empty));
        person_list.setEmptyView(emptyView);
        selectOrder();
    }


    private void selectOrder() {
        userAction.selectOrder(mContext, String.valueOf(page),
                UserData.getSettingString(mContext, UserData.access_token),
                new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        if (obj != null && !"".equals(obj)) {
                            selectOrders publishedData_new = (selectOrders) obj;
                            publishedData.addAll(publishedData_new.getContent());
                            adaper.notifyDataSetChanged();
                            if (publishedData_new.getLast().equals("true")) {
                                person_list.setPullLoadEnable(false);
                            } else {
                                person_list.setPullLoadEnable(true);
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

    // 停止加载动画
    private void stopLoad() {
        person_list.stopRefresh();
        person_list.stopLoadMore();
        String dat = Utils.formatDate(new Date().getTime());
        person_list.setRefreshTime(dat);
    }

    @Override
    public void onRefresh() {
        stopLoad();
        selectOrder();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isRefresh){
            page = 0;
            selectOrder();
        }

    }

    @Override
    public void onLoadMore() {
        selectOrder();
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_NAME)){
                isRefresh=true;
            }
        }
    };
}
