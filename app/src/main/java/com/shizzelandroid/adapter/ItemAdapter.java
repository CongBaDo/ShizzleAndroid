package com.shizzelandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shizzelandroid.R;
import com.shizzelandroid.utils.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congba on 7/30/15.
 */
public class ItemAdapter extends BaseAdapter {
    private static final String TAG = "ItemAdapter";
    boolean canLoadMore=true;
    private Context context;

    private List<Listing> values;

    public ItemAdapter(Context context, List<Listing> values){
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_view, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.imgThumb = (ImageView) convertView.findViewById(R.id.img_thumb);
            holder.txtCategory = (TextView) convertView.findViewById(R.id.tv_category);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(values.get(position).getTitle());
        holder.txtCategory.setText(values.get(position).getCurrentPrice());
        ImageLoader.getInstance().displayImage(values.get(position).getImageUrl(), holder.imgThumb);

        return convertView;
    }

    private class ViewHolder {
        TextView txtTitle;
        ImageView imgThumb;
        TextView txtCategory;
    }
}
