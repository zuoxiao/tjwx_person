package com.example.tjwx_person.activity;

import android.content.Context;
import android.os.Bundle;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.adapter.NoticeAdapter;
import com.example.tjwx_person.bean.MessageData;
import com.example.tjwx_person.bean.MessageListData;
import com.example.tjwx_person.bean.NoticeDatas;
import com.example.tjwx_person.bean.NoticeDate;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.tool.listView.XListView;
import com.example.tjwx_person.utils.UserData;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zuo on 2016/6/28.
 */
public class NoticeActivity extends BaseActivity implements XListView.IXListViewListener {

    private XListView notice_list;
    private NoticeAdapter adapter;
    private List<NoticeDate> list =  new ArrayList<>();
    UserAction userAction;
    int page;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_notice, 1, "消息中心");
        initView();
        selectMessage();
    }


    private void initView() {
        notice_list = (XListView) findViewById(R.id.notice_list);
        adapter = new NoticeAdapter(this, list);
        notice_list.setAdapter(adapter);

        notice_list.setPullLoadEnable(false);// 初始化时候 loadmore 不可见
        notice_list.setPullRefreshEnable(false);
        notice_list.setXListViewListener(this);
        mContext = this;
        userAction = UserAction.getInstance();
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    @Override
    public void onRefresh() {
        page = 0;

        if (list != null) {
            list.clear();
        }
        selectMessage();

    }

    @Override
    public void onLoadMore() {

        //    isLoding = true;
        selectMessage();
    }

    // 停止加载动画
    private void stopLoad() {
        notice_list.stopRefresh();
        notice_list.stopLoadMore();
        String dat = Utils.formatDate(new Date().getTime());
        notice_list.setRefreshTime(dat);
    }

    private void selectMessage() {
        // String.valueOf(message.size())
        userAction.getNotice(mContext, 10, page,
                UserData.getSettingString(mContext, UserData.access_token),
                new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        try {
                            stopLoad();
                            NoticeDatas lists = (NoticeDatas) obj;
                            if (lists != null) {
                                //   List<MessageData> messages = message;
                                //message.clear();
                                //  messages.addAll(messa.getContent());
                                list.addAll(lists.getContent());

                                page++;


                            }

                            adapter.notifyDataSetChanged();
                            notice_list.setSelection(notice_list.getBottom());
                            if (lists.getContent().size()<10) {
                                notice_list.setPullLoadEnable(false);
                            } else {
                                notice_list.setPullLoadEnable(true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        stopLoad();
                    }
                });

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
