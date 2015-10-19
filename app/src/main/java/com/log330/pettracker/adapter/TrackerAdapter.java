package com.log330.pettracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.log330.pettracker.R;
import com.log330.pettracker.model.SquareZone;
import com.log330.pettracker.model.Tracker;
import com.log330.pettracker.model.Zone;

import java.util.ArrayList;

/**
 * Created by marc-antoinehinse on 2015-10-19.
 */
public class TrackerAdapter extends ArrayAdapter<Tracker> {
    private Context mContext;
    private LayoutInflater mInflater = null;

    public TrackerAdapter(Context context, ArrayList<Tracker> trackers) {
        super(context, R.layout.tracker_item, trackers);
        mContext = context;
    }

    static class ViewHolder {
        public TextView name;
        public ImageView avatar;
    }

    private LayoutInflater getInflater(){
        if(mInflater == null)
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return mInflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;

        if(convertView == null){ // Only inflating if necessary is great for performance
            rowView = getInflater().inflate(R.layout.tracker_item, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.name = (TextView) rowView.findViewById(R.id.name);
            holder.avatar = (ImageView) rowView.findViewById(R.id.avatar);
            rowView.setTag(holder);
        } else{
            rowView = convertView;
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        Tracker r = getItem(position);

        holder.name.setText(r.getName());


        if(r.getAvatar() != null) {
            holder.avatar.setImageBitmap(r.getAvatar());
        }

        return rowView;
    }
}
