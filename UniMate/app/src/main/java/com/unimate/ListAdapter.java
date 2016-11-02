package com.unimate;

/**
 * Created by Hans Vader on 01.11.2016.
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> names;
    private ArrayList<String> counts;
    private static LayoutInflater inflater=null;


    public ListAdapter(Activity a, ArrayList<String> names, ArrayList<String> counts) {
        activity = a;
        this.names = names;
        this.counts = counts;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return names.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_element, null);

        TextView event_name_text = (TextView)vi.findViewById(R.id.event_name_text); // title
        TextView member_counter_text = (TextView)vi.findViewById(R.id.member_counter_text); // artist name

        String name = names.get(position);
        String count = counts.get(position);

        // Setting all values in listview
        event_name_text.setText(name);
        member_counter_text.setText(count);
        return vi;
    }
}
