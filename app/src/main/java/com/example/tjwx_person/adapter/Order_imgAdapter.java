package com.example.tjwx_person.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cxcl.property.customer.R;
import com.example.tjwx_person.activity.OrderDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class Order_imgAdapter extends BaseAdapter {

    public List<String> listImg = new ArrayList<String>();
    private LayoutInflater inflater;
    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public Order_imgAdapter(Context context, List<String> listImg) {
        inflater = LayoutInflater.from(context);
        this.listImg = listImg;
        options = new DisplayImageOptions.Builder()

                .cacheInMemory(false).displayer(new RoundedBitmapDisplayer(20))
                .cacheOnDisc(false).considerExifParams(true).build();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listImg.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listImg.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_img_item, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.order_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        imageLoader.displayImage(listImg.get(position), holder.img, options);
        if (OrderDetailActivity.imgNumber == position) {
            holder.img.setBackgroundResource(R.drawable.order_img_add);
        }

        return convertView;
    }

    public class ViewHolder {
        ImageView img;

    }

    /**
     * 加载本地图片 http://bbs.3gstdy.com
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
