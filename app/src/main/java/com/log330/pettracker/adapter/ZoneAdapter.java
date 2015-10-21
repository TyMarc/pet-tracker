package com.log330.pettracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.log330.pettracker.R;
import com.log330.pettracker.model.Zone;

import java.util.ArrayList;

/**
 * Created by marc-antoinehinse on 2015-10-19.
 */
public class ZoneAdapter extends ArrayAdapter<Zone> {
    private Context mContext;
    private LayoutInflater mInflater = null;

    public ZoneAdapter(Context context, ArrayList<Zone> zones) {
        super(context, R.layout.zone_item, zones);
        mContext = context;
    }

    static class ViewHolder {
        public TextView name;
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
            rowView = getInflater().inflate(R.layout.zone_item, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.name = (TextView) rowView.findViewById(R.id.name);
            rowView.setTag(holder);
        } else{
            rowView = convertView;
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        Zone r = getItem(position);

        holder.name.setText("Zone " + (position+1));

        return rowView;
    }
}
