package com.example.tjwx_person.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.action.UserAction;
import com.example.tjwx_person.bean.UpdateDown;
import com.example.tjwx_person.http.AsyncHandler;

/**
 * Created by zuo on 2016/7/3.
 */
public class FunctionActivity extends BaseActivity {
    private TextView function_text;
    private Context mContext;
    UserAction userAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_function, 1, "功能介绍");
        userAction = UserAction.getInstance();
        mContext=this;
        initView();
        update();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }


    /**
     * 查询更新
     */
    private void update() {


        userAction.updateDownLoad(mContext, getVersion(), new AsyncHandler() {
            @Override
            public void onSuccess(Object obj) {
                if (obj != null) {

                    UpdateDown url = (UpdateDown) obj;
                    function_text.setText(url.getUpdateLog());
                }

            }

            @Override
            public void onFailure(int statusCode, String message) {

            }
        });


    }

    private void initView() {
        function_text = (TextView) findViewById(R.id.function_text);
    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
