package com.example.tjwx_person.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zuo on 2016/6/24.
 */
public class CompanyClauseActivity extends BaseActivity {
    private TextView clause_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_company_clause, 1, "法律条款");
        initView();
    }

    @Override
    public void backButton() {
        super.backButton();
        this.finish();
    }

    private void initView() {

        clause_text = (TextView) findViewById(R.id.clause_text);
        try {
            InputStream is = getAssets().open("clause.txt");
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            String text = new String(buffer, "UTF-8");
            clause_text.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
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
