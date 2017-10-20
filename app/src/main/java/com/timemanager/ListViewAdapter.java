package com.timemanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<ActivitiesModal> activityItems;

    public ListViewAdapter(Context context, ArrayList<ActivitiesModal> items) {
        this.context = context;
        this.activityItems = items;
    }


    private class ViewHolder {
        TextView title_tv;
        TextView date_tv;
        TextView place_tv;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.title_tv = (TextView) convertView.findViewById(R.id.tv_title);
            holder.date_tv = (TextView) convertView.findViewById(R.id.tv_date);
            holder.place_tv = (TextView) convertView.findViewById(R.id.tv_place);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ActivitiesModal activitiesModal = (ActivitiesModal) getItem(position);

        holder.title_tv.setText(activitiesModal.getTitle());
        holder.date_tv.setText(activitiesModal.getDate());
        holder.place_tv.setText(activitiesModal.getPlace());

        return convertView;
    }

    @Override
    public int getCount() {
        return activityItems.size();
    }

    @Override
    public Object getItem(int position) {
        return activityItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return activityItems.indexOf(getItem(position));
    }
}