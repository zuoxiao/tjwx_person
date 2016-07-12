package com.example.tjwx_person.tool;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.cxcl.property.customer.R;
import com.example.tjwx_person.bean.skillData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuo on 2016/7/6.
 */
public class TypePicker {

    /**
     * 滑动控件
     */
    private ScrollerNumberPicker typePicker;

    // private ScrollerNumberPicker counyPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private int tempCounyIndex = -1;


    private static List<skillData> typeData = new ArrayList<skillData>();

    // private static ArrayList<String> couny_list_code = new
    // ArrayList<String>();


    private String city_code_string;
    private String city_string;

    private View view;
    private Context context;

    public TypePicker(View view, Context context) {

        this.view = view;
        this.context = context;
        initView();

    }


    public void setData( List<skillData> typeData){
        this.typeData = typeData;
        try {
            ArrayList<String> typeString = new ArrayList<>();
            for (skillData skillData : typeData) {
                typeString.add(skillData.getDescription());
            }
            typePicker.setData(typeString);
            typePicker.setDefault(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initView() {


        // 获取控件引用
        typePicker = (ScrollerNumberPicker) view
                .findViewById(R.id.type);





        typePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                System.out.println("id-->" + id + "text----->" + text);
                if (text.equals("") || text == null)
                    return;
                if (tempProvinceIndex != id) {
                    System.out.println("endselect");
                    String selectDay = typePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;

                    // counyPicker.setData(citycodeUtil.getCouny(couny_map,
                    // citycodeUtil.getCity_list_code().get(1)));
                    // counyPicker.setDefault(1);


                }
                tempProvinceIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });


    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity_code_string() {
        return city_code_string;
    }


    public interface OnSelectingListener {

        public void selected(boolean selected);
    }

    public String getTypeId(){

        try {
            if (tempProvinceIndex!=-1&&typeData.get(tempProvinceIndex)!=null){
                return typeData.get(tempProvinceIndex).getId();
            }else {
                return typeData.get(0).getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getTypeName(){

        try {
            if (tempProvinceIndex!=-1&&typeData.get(tempProvinceIndex)!=null){
                return typeData.get(tempProvinceIndex).getDescription();
            }else {
                return typeData.get(0).getDescription();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
