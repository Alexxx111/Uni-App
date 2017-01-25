package com.unimate;

/**
 * Created by Hans Vader on 01.11.2016.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unimate.model.Event;


import static com.unimate.R.drawable.ic_brightness_1_fifth_24dp;
import static com.unimate.R.drawable.ic_brightness_1_first_24dp;
import static com.unimate.R.drawable.ic_brightness_1_second_24dp;
import static com.unimate.R.drawable.ic_brightness_1_third_24dp;

public class ListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Event> events;
    private static LayoutInflater inflater=null;


    public ListAdapter(Activity a, ArrayList<Event> events) {
        activity = a;
        this.events = events;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return events.size();
    }

    public Object getItem(int position) {
        return events.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_element, null);

        TextView event_name_text = (TextView)vi.findViewById(R.id.message_text); // title
        TextView member_counter_text = (TextView)vi.findViewById(R.id.member_counter_text);
        TextView modul_name = (TextView) vi.findViewById(R.id.modul_name);

        String modul = events.get(position).getModulSymbol();
        String modulID = events.get(position).getModulID();
        String name = events.get(position).getName();
        String count = String.valueOf(events.get(position).getMemberCount());

        // Setting all values in listview
        event_name_text.setText(name);
        int nummer = modulID.charAt(6) - '0';
        modul_name.setText(modul);
        switch(nummer){

            case 1:
                modul_name.setBackground(ContextCompat.getDrawable(this.activity, ic_brightness_1_first_24dp));
                break;
            case 2:
                modul_name.setBackground(ContextCompat.getDrawable(this.activity, ic_brightness_1_second_24dp));
                break;
            case 3:
                modul_name.setBackground(ContextCompat.getDrawable(this.activity, ic_brightness_1_third_24dp));
                break;
            case 4:
                modul_name.setBackground(ContextCompat.getDrawable(this.activity, ic_brightness_1_fifth_24dp));
                break;
        }
        member_counter_text.setText(count);
        return vi;
    }

}
