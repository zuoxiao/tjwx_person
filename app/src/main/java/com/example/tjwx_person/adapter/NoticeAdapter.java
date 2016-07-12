package com.example.tjwx_person.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cxcl.property.customer.R;
import com.example.tjwx_person.bean.NoticeDate;
import com.example.tjwx_person.bean.TypeData;
import com.example.tjwx_person.utils.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zuo on 2016/6/27.
 */
public class NoticeAdapter extends BaseAdapter{
    private Context context = null;
    private LayoutInflater inflater = null;
    private List<NoticeDate> list = new ArrayList<>();


    public NoticeAdapter(Context context, List<NoticeDate> list
                             ) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            if (view == null) {
                view = inflater.inflate(R.layout.item_notice, null);
            }
            holder.time = (TextView) view.findViewById(R.id.notice_time);
            holder.data = (TextView) view.findViewById(R.id.notice_data);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.time.setText(DateUtil.date2StringLong(list.get(position).getCreateTime()));
        holder.data.setText(list.get(position).getContent());

        return view;
    }

    public class ViewHolder {
        public TextView time = null;
        public TextView data = null;
    }

}
