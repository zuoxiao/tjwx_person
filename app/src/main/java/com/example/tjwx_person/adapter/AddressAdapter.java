package com.example.tjwx_person.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cxcl.property.customer.R;
import com.example.tjwx_person.bean.AddressData;

public class AddressAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<AddressData> items;

	public AddressAdapter(List<AddressData> address, Context context) {
		inflater = LayoutInflater.from(context);
		items = address;
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
			convertView = inflater.inflate(R.layout.address_item, null);
			holder = new ViewHolder();
			holder.address = (TextView) convertView
					.findViewById(R.id.address_address);
			holder.name = (TextView) convertView
					.findViewById(R.id.address_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.address.setText(items.get(position).getAddress());
		holder.name.setText(items.get(position).getName());
		return convertView;
	}

	public class ViewHolder {
		TextView address, name;

	}
}
