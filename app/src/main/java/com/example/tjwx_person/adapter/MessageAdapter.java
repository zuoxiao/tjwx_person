package com.example.tjwx_person.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;
import com.example.tjwx_person.adapter.AddressAdapter.ViewHolder;
import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.bean.MessageData;
import com.example.tjwx_person.utils.DateUtil;
import com.example.tjwx_person.utils.UserData;

public class MessageAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<MessageData> items;
	String processorName;
	Context context;

	public MessageAdapter(List<MessageData> message, Context context,
			String processorName) {
		inflater = LayoutInflater.from(context);
		items = message;
		this.processorName = processorName;
		this.context = context;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void removeItem(int position) {
		items.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.message_item, null);
			holder = new ViewHolder();
			holder.massage_name = (TextView) convertView
					.findViewById(R.id.massage_name);
			holder.massage_time = (TextView) convertView
					.findViewById(R.id.massage_time);
			holder.message_message = (TextView) convertView
					.findViewById(R.id.message_message);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (items
				.get(position)
				.getSenderId()
				.equals(UserData.getSettingString(context,
						UserData.user_phone))) {
			holder.massage_name.setText("我");
		} else {
			holder.massage_name.setText(processorName);
		}

		holder.massage_time.setText(DateUtil.date2StringLong(items.get(position).getCreateTime()));
		holder.message_message.setText(items.get(position).getContent());
		return convertView;
	}

	public class ViewHolder {
		TextView massage_name, massage_time, message_message;

	}

}
