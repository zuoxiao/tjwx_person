package com.example.tjwx_person.adapter;

import java.util.List;

import com.cxcl.property.customer.R;
import com.example.tjwx_person.adapter.AddressAdapter.ViewHolder;
import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.bean.CouponDetailData;
import com.example.tjwx_person.utils.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CouponAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<CouponDetailData> items;
    private Context context;

    public CouponAdapter(List<CouponDetailData> CouponData, Context context) {
        inflater = LayoutInflater.from(context);
        items = CouponData;
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
            convertView = inflater.inflate(R.layout.coupon_item, null);
            holder = new ViewHolder();
            holder.bottom_time = (TextView) convertView
                    .findViewById(R.id.bottom_time);
            holder.coupon_money = (TextView) convertView
                    .findViewById(R.id.coupon_money);
            holder.coupon_comment = (TextView) convertView
                    .findViewById(R.id.coupon_comment);
            holder.top_line = (View) convertView.findViewById(R.id.top_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bottom_time.setText("有限期至\b\b" + DateUtil.date2StringShort(items.get(position).getExpiryTime()))
        ;
        holder.coupon_money.setText(items.get(position).getAmount());
        holder.coupon_comment.setText(items.get(position).getDescription());
        if (items.get(position).isUsable()) {
            holder.top_line.setBackgroundColor(context.getResources().getColor(R.color.title_color));
            holder.bottom_time.setBackgroundColor(context.getResources().getColor(R.color.title_color));
        } else {
            holder.top_line.setBackgroundColor(context.getResources().getColor(R.color.gray));
            holder.bottom_time.setBackgroundColor(context.getResources().getColor(R.color.gray));
        }

        return convertView;
    }

    public class ViewHolder {
        TextView bottom_time, coupon_money, coupon_comment;
        View top_line;
    }

}
