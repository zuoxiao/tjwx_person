package com.example.tjwx_person.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cxcl.property.customer.R;
import com.example.tjwx_person.activity.RepairActivity;
import com.example.tjwx_person.bean.TypeData;
import com.example.tjwx_person.bean.skillData;

public class Repair_TypeAdapter extends BaseAdapter {

    private Context context = null;
    private LayoutInflater inflater = null;
    private List<skillData> list = null;
    HashMap<Integer, Boolean> isSelected;

    public Repair_TypeAdapter(Context context, List<skillData> list,
                              HashMap<Integer, Boolean> isSelected) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.isSelected = isSelected;
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
                view = inflater.inflate(R.layout.repair_type_item, null);
            }
            holder.tv = (TextView) view.findViewById(R.id.repair_text);
            holder.cb = (CheckBox) view.findViewById(R.id.repair_checbox);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (list != null && list.get(position).getFatherId() == null) {

            holder.tv.setText(list.get(position).getName() + ":"
                    + list.get(position).getDescription());
        }

        //	holder.cb.setChecked(isSelected.get(position));
        try {
            if (isSelected != null && isSelected.size() > 0) {
                if (isSelected.get(position)) {
                    holder.cb.setBackgroundResource(R.drawable.checkbox_on);
                } else {
                    holder.cb.setBackgroundResource(R.drawable.checkbox_off);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return view;
    }

    public class ViewHolder {
        public TextView tv = null;
        public CheckBox cb = null;
    }
}
