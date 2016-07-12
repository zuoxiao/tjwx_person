package com.example.tjwx_person.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.adapter.MessageAdapter;
import com.example.tjwx_person.bean.MessageData;
import com.example.tjwx_person.bean.MessageListData;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.http.AsyncHandler;
import com.example.tjwx_person.http.Utils;
import com.example.tjwx_person.tool.listView.XListView;
import com.example.tjwx_person.tool.listView.XListView.IXListViewListener;
import com.example.tjwx_person.utils.BaseToast;
import com.example.tjwx_person.utils.UserData;
import com.google.gson.Gson;

public class MessageActivity extends BaseActivity implements IXListViewListener {

    XListView message_listView;
    MessageAdapter adapter;
    List<MessageData> message = new ArrayList<MessageData>();
    UserAction userAction;
    Context mContext;
    EditText message_edt;
    publishedData publised = null;
    TextView message_pulish;
    String orderId;
    MessageListData meassgeList;
    int page;
    boolean isLoding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_message, 4, "留言信息");


        message_listView = (XListView) findViewById(R.id.message_listView);
        message_listView.setPullLoadEnable(false);// 初始化时候 loadmore 不可见
        message_listView.setPullRefreshEnable(false);
        message_listView.setXListViewListener(this);

        userAction = UserAction.getInstance();
        mContext = this;
        message_edt = (EditText) findViewById(R.id.message_edt);
        message_pulish = (TextView) findViewById(R.id.message_pulish);
        message_pulish.setOnClickListener(this);
        if (getIntent().getSerializableExtra("publised") != null) {
            publised = (publishedData) getIntent().getSerializableExtra("publised");
            adapter = new MessageAdapter(message, this, publised.getProcessorName());
            message_listView.setAdapter(adapter);
            selectOldMessage();
        } else {
            published();
        }

    }

    private void selectMessage() {
        // String.valueOf(message.size())
        userAction.conversation(mContext,
                UserData.getSettingString(mContext, UserData.access_token),
                publised.getId(), "", "10", page, new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        stopLoad();
                        MessageListData messa = (MessageListData) obj;
                        if (messa.getContent() != null) {
                            List<MessageData> messages = message;
                            //message.clear();
                            //  messages.addAll(messa.getContent());
                            message.addAll(messa.getContent());

                            page++;


                        }

                        adapter.notifyDataSetChanged();
                        message_listView.setSelection(message_listView.getBottom());
                        if (messa.isLast()) {
                            message_listView.setPullLoadEnable(false);
                        } else {
                            message_listView.setPullLoadEnable(true);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        stopLoad();
                    }
                });

    }


    private void publis() {
        startProgressDialog();
        userAction.conversationPublish(mContext, UserData.getSettingString(
                mContext, UserData.access_token), publised.getId(), publised
                        .getProcessorId(), message_edt.getText().toString(),
                new AsyncHandler() {

                    @Override
                    public void onSuccess(Object obj) {
                        stopProgressDialog();
                        BaseToast.makeShortToast(mContext, "发送成功");
                        message_edt.setText("");
                        page = 0;
                        if (message != null) {
                            message.clear();
                        }
                        selectOldMessage();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(
                                message_edt.getWindowToken(), 0); // 强制隐藏键盘
                    }

                    @Override
                    public void onFailure(int statusCode, String message) {
                        BaseToast.makeShortToast(mContext, "发送失败，请重试！");
                        stopProgressDialog();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.message_pulish:
                if (Utils.isNetworkAvailable(mContext)) {
                    if (!"".equals(message_edt.getText().toString())) {
                        publis();
                    } else {
                        BaseToast.makeShortToast(mContext, "请您填写信息");
                    }

                } else {
                    BaseToast.makeShortToast(mContext, "网络连接失败，请检查您的网络连接");
                }

                break;

            default:
                break;
        }
    }

    private void selectOldMessage() {
        Gson gson = new Gson();
        MessageListData messages = gson.fromJson(
                UserData.getSettingString(mContext, UserData.Message),
                MessageListData.class);
        if (messages != null) {
            message.clear();
            message.addAll(messages.getContent());
            adapter.notifyDataSetChanged();
            message_listView.setSelection(message_listView.getBottom());
        }

        selectMessage();
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    @Override
    public void Refresh() {
        super.Refresh();
        page = 0;
        if (message != null) {
            message.clear();
        }
        selectMessage();


    }

    @Override
    public void onRefresh() {
        page = 0;

        if (message != null) {
            message.clear();
        }
        selectMessage();

    }

    @Override
    public void onLoadMore() {

        isLoding = true;
        selectMessage();
    }

    // 停止加载动画
    private void stopLoad() {
        message_listView.stopRefresh();
        message_listView.stopLoadMore();
        String dat = Utils.formatDate(new Date().getTime());
        message_listView.setRefreshTime(dat);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
        selectMessage();
    }

    /**
     * 查询订单
     */
    private void published() {

        String access_token = UserData.getSettingString(mContext,
                UserData.access_token);
        if (access_token != null
                && !"".equals(UserData.getSettingString(mContext,
                UserData.access_token))) {
            startProgressDialog();
            userAction.published(mContext, access_token, new AsyncHandler() {

                @Override
                public void onSuccess(Object obj) {
                    stopProgressDialog();
                    if (obj != null && !"".equals(obj)) {
                        publised = (publishedData) obj;
                        UserData.setSettingBoolean(mContext,
                                UserData.isPublish, true);
                    } else {

                        UserData.setSettingBoolean(mContext,
                                UserData.isPublish, false);
                    }
                    adapter = new MessageAdapter(message, MessageActivity.this, publised.getProcessorName());
                    message_listView.setAdapter(adapter);
                    selectOldMessage();
                }

                @Override
                public void onFailure(int statusCode, String message) {
                    stopProgressDialog();
                    Log.d("published", "onFailure");
                }
            });

        }
    }
}
