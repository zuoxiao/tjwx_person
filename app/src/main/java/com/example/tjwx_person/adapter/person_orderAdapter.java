package com.example.tjwx_person.adapter;

import java.util.List;

import com.cxcl.property.customer.R;
import com.example.tjwx_person.adapter.AddressAdapter.ViewHolder;
import com.example.tjwx_person.bean.AddressData;
import com.example.tjwx_person.bean.publishedData;
import com.example.tjwx_person.utils.DateUtil;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class person_orderAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<publishedData> items;
    private Context mContext;

    public person_orderAdapter(List<publishedData> address, Context context) {
        inflater = LayoutInflater.from(context);
        items = address;
        mContext = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.person_order_item, null);
            holder = new ViewHolder();
            holder.type = (TextView) convertView.findViewById(R.id.order_type);
            holder.time = (TextView) convertView.findViewById(R.id.order_time);
            holder.money = (TextView) convertView
                    .findViewById(R.id.order_money);
            holder.detail = (TextView) convertView.findViewById(R.id.order_detail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder.type.setText(items.get(position).getSkill());
        holder.time.setText(DateUtil.date2StringLong(items.get(position).getPublishTime()));
        try {
            if (items.get(position).getState().equals("PICKER")) {
                holder.type.setText("已处理");
            } else if (items.get(position).getState().equals("COMMIT")) {
                holder.type.setText("待支付");
            } else if (items.get(position).getState().equals("PAYMENT") && items.get(position).isCommentState()) {
                holder.type.setText("已完成");
                holder.type.setTextColor(mContext.getResources().getColor(R.color.green1));
            } else if (items.get(position).getState().equals("PAYMENT") && !items.get(position).isCommentState()) {
                holder.type.setText("待评价");
                holder.type.setTextColor(mContext.getResources().getColor(R.color.green1));
            } else if (items.get(position).getState().equals("REFUSAL")) {
                holder.type.setText("拒绝付款");
                holder.type.setTextColor(mContext.getResources().getColor(R.color.sub_textColor));
            } else if (items.get(position).getState().equals("CANCEL")) {
                holder.type.setText("已取消");
                holder.type.setTextColor(mContext.getResources().getColor(R.color.sub_textColor));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (items.get(position).isAudioOrder()){
                holder.detail.setText("语音");
            }else {
                holder.detail.setText(items.get(position).getSkills().get(0).getDescription());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.money.setText(items.get(position).getAmount());
        return convertView;
    }

    public class ViewHolder {
        TextView type, time, money, detail;

    }

}
