package com.unimate;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimate.model.Event;
import com.unimate.model.Message;

import java.util.ArrayList;

/**
 * Created by Hans Vader on 09.12.2016.
 */

public class MessageAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Message> messages;
    private static LayoutInflater inflater=null;


    public MessageAdapter(Activity a, ArrayList<Message> messages) {
        activity = a;
        this.messages = messages;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return messages.size();
    }

    public Object getItem(int position) {
        return messages.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        if(messages.get(position).getImage() == 1) {
            vi = inflater.inflate(R.layout.message_element_image, null);

            //TextView messageText = (TextView)vi.findViewById(R.id.message_text); // title
            ImageView messageImage = (ImageView)vi.findViewById(R.id.message_image);
            String name = messages.get(position).getMessageText();

            messageImage.setImageBitmap(messages.get(position).getBmp());

            System.out.println("yolo: " + messages.get(position).getBmp());

            //messageImage.setVisibility(View.VISIBLE);
            // Setting all values in listview
            //messageText.setText("");
            System.out.println("asking for position: ..  " +position +" .. image: " + messages.get(position).getImage());
        }
        else if(messages.get(position).getImage() == 0){
            // Setting all values in listview
            vi = inflater.inflate(R.layout.message_element, null);

            TextView messageText = (TextView)vi.findViewById(R.id.message_text); // title
            //ImageView messageImage = (ImageView)vi.findViewById(R.id.message_image);
            String name = messages.get(position).getMessageText();

            System.out.println("yolo: " + messages.get(position).getBmp());

            //messageImage.getLayoutParams().height = (int) parent.getResources().getDimension(R.dimen.imageview_height_0);
            //messageImage.getLayoutParams().width = (int) parent.getResources().getDimension(R.dimen.imageview_width_0);

            //messageImage.setVisibility(View.INVISIBLE);
            messageText.setText(name);
        }

        return vi;
    }

}
