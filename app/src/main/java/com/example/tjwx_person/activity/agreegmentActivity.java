package com.example.tjwx_person.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;

public class agreegmentActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_agreegment, 1, "服务协议");
		TextView agreegment_text = (TextView) findViewById(R.id.agreegment_text);
		agreegment_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				agreegmentActivity.this.finish();

			}
		});
	}
@Override
public void backButton() {
	super.backButton();
	this.finish();
}
}
