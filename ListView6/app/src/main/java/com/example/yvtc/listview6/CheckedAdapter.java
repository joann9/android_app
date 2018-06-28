package com.example.yvtc.listview6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by YVTC on 2018/3/29.
 */

public class CheckedAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private final List<Map<String, Object>> mItemList;
    private final int mLayoutID;

    public CheckedAdapter(Context context, List<Map<String,Object>> itemList, int layoutID) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemList = itemList;
        mLayoutID = layoutID;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView name;
        CheckedTextView price;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if(convertView == null){
            convertView = mLayoutInflater.inflate(mLayoutID,null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.price = (CheckedTextView) convertView.findViewById(R.id.price);
            holder.image = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String,Object> item = mItemList.get(position);
        int imageID = (int) item.get("image");
        holder.image.setImageResource(imageID);

        String txt = (String) item.get("name");
        holder.name.setText(txt);

        String price =  (String) item.get("price").toString(); //要加toString(),不然會當掉
        holder.price.setText(price);

        return convertView;
    }
}
